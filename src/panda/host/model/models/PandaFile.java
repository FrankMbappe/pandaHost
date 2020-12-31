package panda.host.model.models;

public class PandaFile {
    private int id;
    private String name;
    private String extension;
    private double size;

    public PandaFile(int id, String name, String extension, double size) {
        this.id = id;
        this.name = name;
        this.extension = extension;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
