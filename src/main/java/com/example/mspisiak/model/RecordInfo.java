package com.example.mspisiak.model;

import java.time.LocalDateTime;

/**
 * @author Martin Spisiak (mspisiak@redhat.com) on 2019-03-06.
 */
public class RecordInfo {
  private RecordStatus recordStatus;
  private LocalDateTime created;
  private LocalDateTime updated;
  private LocalDateTime deleted;
  private String recordData;

  public RecordInfo() {}

  public RecordInfo(LocalDateTime created, String recordData) {
    this.recordStatus = RecordStatus.NEW;
    this.created = created;
    this.recordData = recordData;
  }

  public RecordStatus getRecordStatus() {
    return recordStatus;
  }

  public void setRecordStatus(RecordStatus recordStatus) {
    this.recordStatus = recordStatus;
  }

  public LocalDateTime getCreated() {
    return created;
  }

  public void setCreated(LocalDateTime created) {
    this.created = created;
  }

  public LocalDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(LocalDateTime updated) {
    this.updated = updated;
  }

  public LocalDateTime getDeleted() {
    return deleted;
  }

  public void setDeleted(LocalDateTime deleted) {
    this.deleted = deleted;
  }

  public String getRecordData() {
    return recordData;
  }

  public void setRecordData(String recordData) {
    this.recordData = recordData;
  }

  @Override
  public String toString() {
    return "RecordInfo{" +
      "recordStatus=" + recordStatus +
      ", created=" + created +
      ", updated=" + updated +
      ", deleted=" + deleted +
      ", recordData=" + recordData +
      '}';
  }
}
