package panda.host.model.models;

import panda.host.utils.Panda;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PandaFile {
    private String id;
    private LocalDateTime date;
    private String fileName;
    private String type;
    private String sizeToString;
    private long size;
    private String uploaderId;

    public PandaFile(String id, LocalDateTime date, String fileName, String type, long size, String uploaderId) {
        this.id = id;
        this.date = date;
        this.fileName = fileName;
        this.type = type;
        this.size = size;
        this.sizeToString = getSizeToString(size);
        this.uploaderId = uploaderId;
    }

    public PandaFile(String id, Timestamp date, String fileName, String type, long size, String uploaderId) {
        this.id = id;
        this.date = date.toLocalDateTime();
        this.fileName = fileName;
        this.type = type;
        this.size = size;
        this.sizeToString = getSizeToString(size);
        this.uploaderId = uploaderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return Panda.getFormattedDate(date);
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSizeToString(){
        return sizeToString;
    }

    public String getSizeToString(long size) {
        return Panda.convertLongSizeToString(size);
    }

    public long getSize(){
        return size;
    }

    public void setSize(long size) {
        this.size = size;
        this.sizeToString = getSizeToString(size);
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }
}
