package panda.host.model.data;

import panda.host.config.database.MySQLConnection;
import panda.host.model.models.User;
import panda.host.model.models.filters.Filter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Data<User> {
    MySQLConnection mySQLConn;

    public UserData(MySQLConnection mySQLConn){
        this.mySQLConn = mySQLConn;
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
    public ArrayList<User> getMatchingData(Filter filter) {
        return null;
    }

    @Override
    public String getMatchingDataToJson(Filter filter) {
        return null;
    }

    @Override
    public User get(Object username) {
        List<User> users = getAll();
        return users.get(users.indexOf(new User((String) username)));
    }

    @Override
    public void add(User user) {
        String sql = String.format("INSERT INTO users(username, password, permissions) VALUES ('%s', '%s', %d)",
                user.getUsername(),user.getPassword(), user.getPermissions());
        mySQLConn.executeUpdateStatement(sql, true);
        System.out.println(String.format("[UserData, add()] | User '%s' was added.", user.getUsername()));
    }

    @Override
    public String getMatchingDataFromPandaCode(String marshalledCode) {
        return null;
    }
}
