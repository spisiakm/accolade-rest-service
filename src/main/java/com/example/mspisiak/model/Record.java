package com.example.mspisiak.model;

/**
 * @author Martin Spisiak (mspisiak@redhat.com) on 2019-03-06.
 */
public class Record {
  private String recordId;
  private RecordInfo info;

  public Record() {}

  public Record(String recordId, RecordInfo info) {
    this.recordId = recordId;
    this.info = info;
  }

  public String getRecordId() {
    return recordId;
  }

  public void setRecordId(String recordId) {
    this.recordId = recordId;
  }

  public RecordInfo getInfo() {
    return info;
  }

  public void setInfo(RecordInfo info) {
    this.info = info;
  }

  @Override
  public String toString() {
    return "Record{" +
      "recordId=" + recordId +
      ", info=" + info +
      '}';
  }
}
