package panda.host.model.models;

import panda.host.utils.Panda;

import java.sql.Timestamp;
import java.time.Instant;

public class Authentication {
    public enum Status { REVOKED, GRANTED }
    public enum Type { NORMAL, GUEST }

    private int statusCode; // 0 = Credentials don't match any user, 1 = User found
    private User user;
    private Timestamp date;

    public Authentication(){}

    public Authentication(Status status, Type type) {
        this.statusCode = getStatusCode(status);
        if (type == Type.GUEST) this.user = new User(Panda.DEFAULT_USER_GUEST_SESSION_NAME);
        this.date = Timestamp.from(Instant.now());
    }

    public Authentication(Status status, User user, Timestamp date) {
        this.statusCode = getStatusCode(status);
        this.user = user;
        this.date = date;
    }

    @Override
    public String toString() {
        if (user != null) {
            return "Authentication {" +
                    " status = " + getStatus() +
                    ", type = " + getType() +
                    ", user = " + user.getUsername() +
                    ", date = " + date.toString() +
                    ", valid = " + isValid() +
                    " }";
        } else {
            return "Authentication {" +
                    " status = " + getStatus() +
                    ", type = " + getType() +
                    ", valid = " + isValid() +
                    " }";
        }
    }

    public Status getStatus() {
        if (statusCode == 1) {
            return Status.GRANTED;
        } else {
            return Status.REVOKED;
        }
    }

    public Type getType(){
        if (user.getUsername().equalsIgnoreCase(Panda.DEFAULT_USER_GUEST_SESSION_NAME)){
            return Type.GUEST;
        }
        else return Type.NORMAL;
    }

    public boolean isValid(){
        // An authentication is only valid when its code is equal to 1 (Guest sessions are not considered as valid)
        return getType() == Type.NORMAL && getStatus() == Status.GRANTED;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public int getStatusCode(Status status){
        if (status == Status.GRANTED) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
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
