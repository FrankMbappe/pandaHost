package panda.host.model.models.filters;

import java.util.ArrayList;

public class PostFilter implements Filter {
    private boolean all;
    private String fileType;
    private String schoolClassId;

    public PostFilter(boolean all, String fileType, String schoolClassId) {
        this.all = all;
        this.fileType = fileType;
        this.schoolClassId = schoolClassId;
    }

    public PostFilter(ArrayList<String> filtersSet){
        this.all = Boolean.parseBoolean(filtersSet.get(0));
        this.fileType = filtersSet.get(1);
        this.schoolClassId = filtersSet.get(2);
    }

    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(String schoolClassId) {
        this.schoolClassId = schoolClassId;
    }
}
