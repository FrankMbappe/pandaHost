package panda.host.model.models.filters;

import java.util.ArrayList;

public class Credentials implements Filter {
    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Deprecated
    public Credentials(ArrayList<String> filtersSet){
        this.username = filtersSet.get(0);
        this.password = filtersSet.get(1);
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
