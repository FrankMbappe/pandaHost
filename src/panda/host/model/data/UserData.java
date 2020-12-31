package panda.host.model.data;

import panda.host.config.databases.MySQLConnection;
import panda.host.model.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    public List<User> getAll() {
        try {
            List<User> users = new ArrayList<>();
            ResultSet rs = mySQLConn.getStatement(true).executeQuery("SELECT * FROM users");
            while(rs.next()){
                users.add(new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getInt("permissions")
                ));
            }
            return users;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User get(String username) {
        List<User> users = getAll();
        return users.get(users.indexOf(new User(username)));
    }

    @Override
    public void add(User user) {
        String sql = String.format("INSERT INTO users(username, password, permissions) VALUES ('%s', '%s', %d)",
                user.getUsername(),user.getPassword(), user.getPermissions());
        mySQLConn.executeUpdateStatement(sql, true);
    }
}
