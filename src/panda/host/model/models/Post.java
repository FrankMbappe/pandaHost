package panda.host.model.models;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import panda.host.model.models.filters.PostFilter;
import panda.host.utils.Panda;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

public class Post {
    private int id;
    private String authorId;
    private String message;
    private String tags; // List of tags separated by the Panda.DEFAULT_SPLIT_CHAR

    private String fileId; // Automatically generated

    private String fileName;
    private String fileExt;
    private long fileSize;
    private byte[] fileToBytes;

    private Timestamp uploadDate;
    private Timestamp lastUpdate;

    public Post(int id, String authorId, String message, String tags) {
        this.authorId = authorId;
        this.message = message;
        this.tags = tags;
    }

    public Post(String authorId, String message, String tags, File file){
        this.authorId = authorId;
        this.message = message;
        this.tags = tags;
        try{
            // e.g: If the file is "src/host/files/folder/document.txt
            this.fileName = FilenameUtils.getBaseName(file.getName()); // fileName = "document"
            this.fileExt = FilenameUtils.getExtension(file.getName()); // fileExt = "txt"
            this.fileSize = file.length(); // fileSize = Size of document.txt in bytes
            // Converts the file to an array of bytes (byte[]), then store it in the fileToBytes property
            this.fileToBytes = FileUtils.readFileToByteArray(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Post(int id, String authorId, String message, String tags, String fileId, String fileName, String fileExt, long fileSize, Timestamp uploadDate, Timestamp lastUpdate) {
        this.id = id;
        this.authorId = authorId;
        this.message = message;
        this.tags = tags;
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.uploadDate = uploadDate;
        this.lastUpdate = lastUpdate;
    }

    public Post(int id, String authorId, String message, String tags, String fileId, String fileName, String fileExt, long fileSize, byte[] fileToBytes, Timestamp uploadDate, Timestamp lastUpdate) {
        this.id = id;
        this.authorId = authorId;
        this.message = message;
        this.tags = tags;
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.fileSize = fileSize;
        this.fileToBytes = fileToBytes;
        this.uploadDate = uploadDate;
        this.lastUpdate = lastUpdate;
    }

    public Post(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(Post.class)) {
            return id == ((Post) obj).getId();
        }
        return false;
    }

    public boolean matchesFilter(PostFilter filter) {
        // TODO: Add the school class filter
        // If the filter ask for all, therefore this condition will be always true
        return filter.isAll() || fileExt.equalsIgnoreCase(filter.getFileType());
    }

    public boolean containsAFile(){
        return fileToBytes != null && fileName != null;
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

    public String getTags() {
        return tags;
    }

    public ArrayList<String> getTagsToList(){
        return (ArrayList<String>) Arrays.asList(tags.split(Panda.DEFAULT_SPLIT_CHAR));
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileToBytes() {
        return fileToBytes;
    }

    public void setFileToBytes(byte[] fileToBytes) {
        this.fileToBytes = fileToBytes;
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
