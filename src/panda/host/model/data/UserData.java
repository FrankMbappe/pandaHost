package panda.host.model.data;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import panda.host.config.database.MySQLConnection;
import panda.host.model.models.Authentication;
import panda.host.model.models.User;
import panda.host.model.models.filters.Credentials;
import panda.host.model.models.filters.Filter;
import panda.host.utils.Current;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Data<User, String> {
    MySQLConnection mySQLConn;

    public UserData(){
        this.mySQLConn = Current.dbConnection;
    }

    private void updateUserObservableList(){
        Current.userList.setAll(getAll());
    }

    @Override
    public void init(){
        if(mySQLConn.exists()){
            mySQLConn.executeUpdateStatement("CREATE TABLE IF NOT EXISTS `Users` (" +
                    "`username` VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "`password` VARCHAR(255) NOT NULL," +
                    "`permissions` INT" +
            ")", true);
            System.out.println("[UserData, init()] | Table 'Users' created.");

            // Update observable
            updateUserObservableList();

        }else{
            System.out.println("[UserData, init()] | Error: The connection doesn't exist.");
        }
    }

    @Override
    public ArrayList<User> getAll() {
        try {
            ArrayList<User> users = new ArrayList<>();
            ResultSet rs = mySQLConn.getStatement(true).executeQuery("SELECT * FROM users");
            int i = 0;
            while(rs.next()){
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("permissions")
                )); i++;
            }
            System.out.println(String.format("[UserData, getAll()] | %d row(s) retrieved.", i));
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<User> getMatchingData(Filter credentials) {
        ArrayList<User> users = new ArrayList<>();

        for (User user : getAll()){
            if(user.matchesCredentials((Credentials) credentials)){
                users.add(user);
            }
        }

        System.out.println(String.format("[UserData, getMatchingData()] | The matching data array size is %d", users.size()));

        return users;
    }

    @Override
    public String getMatchingDataToJson(Filter credentials) {
        return new Gson().toJson(getMatchingData(credentials));
    }

    @Override
    public User getMatchingItem(Filter credentials) {
        ArrayList<User> matchingData = getMatchingData(credentials);

        if(matchingData.size() < 1){
            return null;
        } else {
            return matchingData.get(0);
        }
    }

    @Override
    public String getMatchingItemToJson(Filter credentials) {
        return new Gson().toJson(getMatchingItem(credentials));
    }

    @Override
    public User get(String username) {
        List<User> users = getAll();
        return users.get(users.indexOf(new User(username)));
    }

    @Override
    public boolean add(@NotNull User user) {
        String sql = String.format("INSERT INTO users(username, password, permissions) VALUES ('%s', '%s', %d)",
                user.getUsername(),user.getPassword(), user.getPermissions());
        try {
            mySQLConn.getStatement(true).executeUpdate(sql);

            // Update observable
            updateUserObservableList();

        } catch (Exception e) {
            System.out.println(String.format("[UserData, add()] | Error ! Failed to add '%s' in the database.", user.getUsername()));
            return false;
        }
        System.out.println(String.format("[UserData, add()] | User '%s' was added.", user.getUsername()));
        return true;
    }

    @Override
    public boolean remove(String userId) {
        String sql = String.format("DELETE FROM users WHERE username = '%s'", userId);
        try {
            mySQLConn.getStatement(true).executeUpdate(sql);

            // Update observable
            updateUserObservableList();

        } catch (Exception e) {
            System.out.println(String.format("[UserData, remove()] | Error ! Failed to remove '%s' from the database.", userId));
            return false;
        }
        System.out.println(String.format("[UserData, remove()] | User '%s' was removed.", userId));
        return true;
    }

    @Override
    public boolean update(@NotNull User user) {
        String sql = String.format("UPDATE users SET password='%s', permissions=%d WHERE username='%s'",
                user.getPassword(), user.getPermissions(), user.getUsername());
        try {
            mySQLConn.getStatement(true).executeUpdate(sql);

            // Update observable
            updateUserObservableList();

        } catch (Exception e) {
            System.out.println(String.format("[UserData, edit()] | Error ! Failed to edit '%s' in the database.", user.getUsername()));
            return false;
        }
        System.out.println(String.format("[UserData, edit()] | User '%s' was edited.", user.getUsername()));
        return true;
    }

    @Override
    public String getJsonMatchingDataFromJsonFilter(String filterToJson) {
        // I create my credential (also a filter) variable, from the one converted in JSON
        Credentials credentials = new Gson().fromJson(filterToJson, Credentials.class);

        // I return the matching object, applying the filters beforehand extracted
        return getMatchingItemToJson(credentials);
    }

    public boolean addAll(@NotNull ArrayList<User> users){
        boolean allUsersHaveBeenAdded = true;
        for (User user : users){
            boolean userAdded = add(user);
            allUsersHaveBeenAdded = allUsersHaveBeenAdded && userAdded;
        }
        return allUsersHaveBeenAdded;
    }

    public Authentication getAuthFromJsonCredentials(String credentialsToJson){
        // I create a credential object from the panda code:
        Credentials credentials = new Gson().fromJson(credentialsToJson, Credentials.class);
        System.out.println(String.format("[UserData, getAuthFromJsonCredentials()] | Credentials' object retrieved: '%s'.",
                credentials.toString()));

        // I find the user matching these credentials in the db:
        User matchingUser = getMatchingItem(credentials);

        // Depending on the value of matching user, I return an authentication object:
        if (matchingUser != null){
            System.out.println(String.format("[UserData, getAuthFromJsonCredentials()] | The matching user is '%s'.",
                    matchingUser.toString()));
            return new Authentication(Authentication.Status.GRANTED, matchingUser, Timestamp.valueOf(LocalDateTime.now()));
        } else {
            System.err.println("[UserData, getAuthFromJsonCredentials()] | No user matched the credentials.");
            return new Authentication(Authentication.Status.REVOKED, Authentication.Type.NORMAL);
        }
    }

    public String getJsonAuthFromJsonCredentials(String credentialsToJson){
        return new Gson().toJson(getAuthFromJsonCredentials(credentialsToJson));
    }
}
