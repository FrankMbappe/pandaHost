package panda.host.model.models;

import java.sql.Timestamp;

public class Authentication {
    private int code; // 0 = Credentials don't match any user, 1 = User found
    private User user;
    private Timestamp date;

    public Authentication(int code) {
        this.code = code;
    }

    public Authentication(int code, User user, Timestamp date) {
        this.code = code;
        this.user = user;
        this.date = date;
    }

    public int getCode() {
        return code;
    }

    public String getCodeMeaning(){
        switch (code){
            case 0 -> { return "Credentials don't match any user"; }
            case 1 -> { return "User found"; }
            default -> { return "Unknown code"; }
        }
    }

    public void setCode(int code) {
        this.code = code;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
