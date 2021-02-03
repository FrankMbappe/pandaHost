package panda.host.model.models;

import org.jetbrains.annotations.NotNull;
import panda.host.model.models.filters.Credentials;

public class User {
    private String username;
    private String password;
    private int permissions;
    private int status; // 0 = Logged out, 1 = Logged In
    private String permissionMean;
    private String statusMean;

    public User(String username, String password, int permissions) {
        this.username = username;
        this.password = password;
        this.permissions = permissions;
        this.status = 0;
        this.permissionMean = getPermissionMean(permissions);
        this.statusMean = getStatusMean(status);
    }
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.permissions = 0;
        this.status = 0;
        this.permissionMean = getPermissionMean(permissions);
        this.statusMean = getStatusMean(status);
    }
    public User(String username, int permissions){
        this.username = username;
        this.password = "";
        this.status = 0;
        this.permissions = permissions;
        this.statusMean = getStatusMean(status);
        this.permissionMean = getPermissionMean(permissions);
    }
    public User(String username, int permissions, int status){
        this.username = username;
        this.password = "";
        this.status = status;
        this.permissions = permissions;
        this.statusMean = getStatusMean(status);
        this.permissionMean = getPermissionMean(permissions);
    }
    public User(String username){
        this.username = username;
        this.password = "";
        this.permissions = 0;
        this.status = 0;
        this.permissionMean = getPermissionMean(permissions);
        this.statusMean = getStatusMean(status);
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
        return String.format("{\n\tUsername: %s,\n\tPassword: %s,\n\tPermissions: %s,\n\tStatus: %s\n}",
                username, password, permissionMean, statusMean);
    }

    public boolean matchesCredentials(@NotNull Credentials credentials){
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

    public int getStatus(){
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMean(){
        return statusMean;
    }

    public String getStatusMean(int status){
        if(status == 1){
            return "Logged in";
        } else return "Logged out";
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
