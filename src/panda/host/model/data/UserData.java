package panda.host.model.data;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import panda.host.config.database.MySQLConnection;
import panda.host.model.models.Authentication;
import panda.host.model.models.User;
import panda.host.model.models.filters.Credentials;
import panda.host.model.models.filters.Filter;
import panda.host.utils.Current;
import panda.host.utils.Panda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Data<User> {
    MySQLConnection mySQLConn;

    public UserData(){
        this.mySQLConn = Current.dbConnection;
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
    public User get(Object username) {
        List<User> users = getAll();
        return users.get(users.indexOf(new User((String) username)));
    }

    @Override
    public boolean add(@NotNull User user) {
        String sql = String.format("INSERT INTO users(username, password, permissions) VALUES ('%s', '%s', %d)",
                user.getUsername(),user.getPassword(), user.getPermissions());
        try {
            mySQLConn.getStatement(true).executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(String.format("[UserData, add()] | Error ! Failed to add '%s' in the database.", user.getUsername()));
            return false;
        }
        System.out.println(String.format("[UserData, add()] | User '%s' was added.", user.getUsername()));
        return true;
    }

    public boolean addAll(@NotNull ArrayList<User> users){
        boolean allUsersHaveBeenAdded = true;
        for (User user : users){
            boolean userAdded = add(user);
            allUsersHaveBeenAdded = allUsersHaveBeenAdded && userAdded;
        }
        return allUsersHaveBeenAdded;
    }

    @Override
    public String getMatchingDataFromPandaCode(String pandaCode) {
        // Here I assume that the panda code sent by the client matches the pattern of PANDAOP_REQUEST_GET_CONNECTION
        // Therefore I can freely create a Credential object using the ID and password contained in the panda code.
        ArrayList<String> credentialProperties = Panda.extractFiltersFromPandaCode(pandaCode);

        // I create my credential variable, using the ctor that converts these params from String to their normal type.
        Credentials credentials = new Credentials(credentialProperties);

        // I return the matching object, applying the filters beforehand extracted
        return getMatchingItemToJson(credentials);
    }

    public Authentication getAuthObjectFromPandaCode(String pandaCode){
        // I create a credential object from the panda code:
        Credentials credentials = new Credentials(Panda.extractFiltersFromPandaCode(pandaCode));
        System.out.println(String.format("[UserData, getAuthObjectFromPandaCode()] | Credentials' object retrieved: '%s'.",
                credentials.toString()));

        // I find the user matching these credentials in the db:
        User matchingUser = getMatchingItem(credentials);

        // Depending on the value of matching user, I return an authentication object:
        if (matchingUser != null){
            System.out.println(String.format("[UserData, getAuthObjectFromPandaCode()] | The matching user is '%s'.",
                    matchingUser.toString()));
            return new Authentication(1, matchingUser, Timestamp.valueOf(LocalDateTime.now()));
        } else {
            System.out.println("[UserData, getAuthObjectFromPandaCode()] | No user matched the credentials.");
            return new Authentication(0);
        }
    }

    public String getAuthObjectFromPandaCodeToJson(String pandaCode){
        return new Gson().toJson(getAuthObjectFromPandaCode(pandaCode));
    }
}
