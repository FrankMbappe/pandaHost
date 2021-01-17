package panda.host.model.models;

import java.time.LocalDateTime;

public class Authentication {
    private int code; // 0 = Credentials don't match any user, 1 = User found
    private User user;
    private LocalDateTime date;

    public Authentication(int code) {
        this.code = code;
    }

    public Authentication(int code, User user, LocalDateTime date) {
        this.code = code;
        this.user = user;
        this.date = date;
    }

    public int getCode() {
        return code;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
