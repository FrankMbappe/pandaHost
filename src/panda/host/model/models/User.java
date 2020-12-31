package panda.host.model.models;

public class User {
    private String username;
    private String password;
    private int permissions;

    public User(String username, String password, int permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
    }
    public User(String username){
        this.username = username;
        this.password = "";
        this.permissions = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(User.class)){
            return username.equals(((User) obj).getUsername());
        }
        return false;
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

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }
}
