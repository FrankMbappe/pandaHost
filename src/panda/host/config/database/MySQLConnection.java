package panda.host.config.database;

import panda.host.config.Configs;
import panda.host.model.models.MySQLConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * MySQL connection helper for PandaHost.
 */
public class MySQLConnection implements AutoCloseable {
    Connection connection;

    public MySQLConnection(MySQLConfig config) {
        try {
            System.out.println("[MySQLConnection] | Connecting to the database...");
            connection = DriverManager.getConnection(config.getDbUrl(), config.getUsername(), config.getPassword());
            System.out.println("[MySQLConnection] | Successfully connected.");

        } catch (SQLException e) {
            System.out.println("[MySQLConnection] | Error: Failed to connect to the database.");
//            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if(connection != null){
                connection.close();
                System.out.println("[MySQLConnection, close()] | Connection closed.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection get(boolean dbExists){
        MySQLConfig config = Configs.getMySQLConfig();
        try{
            if(config != null){
                if(dbExists){
                    return DriverManager.getConnection(config.existingDbUrl(), config.getUsername(), config.getPassword());
                } else {
                    return connection;
                }
            } else {
                System.out.println("[MySQLConnection, get()] | Error: The configuration object is null.");
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Statement getStatement(boolean dbExists){
        try{
            return get(dbExists).createStatement();

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists(){
        return connection != null;
    }

    public void executeUpdateStatement(String sql, boolean dbExists){
        try{
            getStatement(dbExists).executeUpdate(sql);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
