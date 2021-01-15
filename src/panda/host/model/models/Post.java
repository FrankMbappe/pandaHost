package panda.host.model.models;

import panda.host.model.models.filters.PostFilter;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Post {
    private int id;
    private String authorId;
    private String message;
    private String fileName;
    private String fileExt;
    private double fileSize;
    private Timestamp uploadDate;
    private Timestamp lastUpdate;

    public Post(int id, String authorId, String message, String fileName, String fileExt, double fileSize, Timestamp uploadDate, Timestamp lastUpdate) {
        this.id = id;
        this.authorId = authorId;
        this.message = message;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.lastUpdate = lastUpdate;
    }

    public Post(int id){
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(Post.class)){
            return id == ((Post) obj).getId();
        }
        return false;
    }

    public boolean matchesFilter(PostFilter filter){
        // TODO: Add the school class filter
        // If the filter ask for all, therefore this condition will be always true
        return filter.isAll() || fileExt.equalsIgnoreCase(filter.getFileType());
    }

    public ArrayList<Object> getMarshalledForm() {
        return new ArrayList<>(){{
            add(id);
            add(authorId);
            add(message);
            add(fileName);
            add(fileExt);
            add(fileSize);
            add(uploadDate);
            add(lastUpdate);
        }};
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public Timestamp getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Timestamp uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
