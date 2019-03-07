package com.example.mspisiak.api;

import com.example.mspisiak.model.Record;
import com.example.mspisiak.model.RecordInfo;
import com.example.mspisiak.model.RecordStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Martin Spisiak (mspisiak@redhat.com) on 2019-03-06.
 */
@Controller
public class RecordsApi {

  private final ObjectMapper objectMapper;

  @Autowired
  public RecordsApi(ObjectMapper objectMapper, HttpServletRequest request) {
    this.objectMapper = objectMapper;
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,true);
  }

  @RequestMapping(value = "/record/{recordId}", produces = { "application/json" } , method = RequestMethod.GET)
  ResponseEntity<Record> getRecord(@NotNull @PathVariable("recordId") String id){
    Record record = getRecordFromJson(id);
    if (record == null) return new ResponseEntity<>(record, HttpStatus.NOT_FOUND);
    return new ResponseEntity<>(record, HttpStatus.OK);
  }

  @RequestMapping(value = "/record", method = RequestMethod.POST)
  ResponseEntity<Void> postRecord(@Valid @RequestBody String payload){
    Record record = new Record(UUID.randomUUID().toString(), new RecordInfo(
        LocalDateTime.now(), payload
    ));

    try {
      String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(record);
      File file = new File("records.txt");
      if (!file.exists()) {
        if (!file.createNewFile()) throw new IOException("Could not create the file for records.");
      }
      Files.write(file.toPath(), Arrays.asList(json), StandardOpenOption.APPEND);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/record/{recordId}", consumes = { "application/json" }, method = RequestMethod.PATCH)
  ResponseEntity<Void> patchRecord(@NotNull @PathVariable("recordId") String id, @Valid @RequestBody JsonPatch body){
//    List<Record> records = getAllRecords();
//    if (records.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    records.forEach(item -> {
//      if (item.getRecordId().equals(id)) {
//        item.getInfo().setUpdated(LocalDateTime.now());
//        item.getInfo().setRecordStatus(RecordStatus.UPDATED);
//        try {
//          final JsonNode jsonNode = objectMapper.writeValue(new JsonFactory().);
//        } catch (JsonProcessingException e) {
//          e.printStackTrace();
//        }
//      }
//    });
//    body.apply();

    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  @RequestMapping(value = "/record/{recordId}", method = RequestMethod.DELETE)
  ResponseEntity<Void> deleteRecord(@NotNull @PathVariable("recordId") String id){
    List<Record> records = getAllRecords();
    if (records.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    records.forEach(item -> {
      if (item.getRecordId().equals(id)) {
        item.getInfo().setDeleted(LocalDateTime.now());
        item.getInfo().setRecordStatus(RecordStatus.DELETED);
      }
    });

    try {
      File file = new File("records.txt");
      if (!file.exists()) {
        throw new IOException("File doesn't exist.");
      }
      Files.write(
          file.toPath(),
          records.stream().map(record -> {
            try {
              return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(record);
            } catch (JsonProcessingException e) {
              e.printStackTrace();
            }
            return null;
          }).collect(Collectors.toList()),
          StandardOpenOption.TRUNCATE_EXISTING
      );
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/records", produces = { "application/json" } , method = RequestMethod.GET)
  ResponseEntity<List<Record>> getRecords(){
    return new ResponseEntity<>(getAllRecords(), HttpStatus.OK);
  }

  private Record getRecordFromJson(String id) {
    Record record = null;
    Optional<Record> maybeRecord = getAllRecords().stream().filter(item -> item.getRecordId().equals(id)).findFirst();
    if (maybeRecord.isPresent()) record = maybeRecord.get();
    return record;
  }

  private List<Record> getAllRecords() {
    List<Record> records = new ArrayList<>();
    StringBuilder json = new StringBuilder();
    int occurence = 0;
    try {
      List<String> jsonData = Files.readAllLines(Paths.get("records.txt"), StandardCharsets.UTF_8);
      for (String s : jsonData) {
        json.append(s);
        if (s.equals("{")) occurence++;
        if (s.equals("}")) occurence--;
        if (occurence == 0) {
          records.add(objectMapper.readValue(json.toString(), Record.class));
          json = new StringBuilder();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return records;
  }
}