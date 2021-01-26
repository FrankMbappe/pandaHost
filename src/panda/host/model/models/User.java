package panda.host.model.models;

import panda.host.model.models.filters.Credentials;

public class User {
    private String username;
    private String password;
    private int permissions;
    private String permissionMean;

    public User(String username, String password, int permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.permissionMean = getPermissionMean(permissions);
    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.permissions = 0;
        this.permissionMean = getPermissionMean(permissions);
    }
    public User(String username, int permissions){
        this.username = username;
        this.permissions = permissions;
        this.password = "";
        this.permissionMean = getPermissionMean(permissions);
    }
    public User(String username){
        this.username = username;
        this.permissions = 0;
        this.password = "";
        this.permissionMean = getPermissionMean(permissions);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass().equals(User.class)){
            return username.equals(((User) obj).getUsername());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("{\n\tUsername: %s,\n\tPassword: %s,\n\tPermissions: %s\n}",
                username, password, permissionMean);
    }

    public boolean matchesCredentials(Credentials credentials){
        return username.equalsIgnoreCase(credentials.getUsername())
                && password.equals(credentials.getPassword());
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

    public String getPermissionMean(){
        return permissionMean;
    }

    public String getPermissionMean(int permission){
        switch (permission){
            case 0 -> {
                return "None";
            }
            case 1 -> {
                return "ReadOnly";
            }
            case 2 -> {
                return "Read/Write";
            }
            default -> {
                return "Invalid permission";
            }
        }
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
        this.permissionMean = getPermissionMean(permissions);
    }
}
