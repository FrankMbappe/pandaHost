package panda.host.utils;

import panda.host.config.database.MySQLConnection;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.models.MySQLConfig;

/**
 * The type Panda.
 */
public class Panda {
    /**
     * The My sql conn.
     */
    static MySQLConnection mySQLConn;
    /**
     * The User data.
     */
    static UserData userData;
    /**
     * The Post data.
     */
    static PostData postData;

    /**
     * Init.
     *
     * @param config the config
     */
    public static void init(MySQLConfig config){
        if(config != null){
            // Establishing the connection
            mySQLConn = new MySQLConnection(config);

            if(mySQLConn.exists()){
                userData = new UserData(mySQLConn);
                postData = new PostData(mySQLConn);

                // Database creation
                mySQLConn.executeUpdateStatement("CREATE DATABASE IF NOT EXISTS " + config.getDbName(), false);
                System.out.println(String.format("\nDatabase '%s' created.\n", config.getDbName()));

                // Users' table creation
                userData.init();

                // Posts' table creation
                postData.init();

                // Closing the connection
                mySQLConn.close();

                System.out.println("[Panda, init()] | Panda was successfully configured.");
            } else {
                System.out.println("[Panda, init()] | Error: The connection doesn't exist.");
            }
        } else {
            System.out.println("[Panda, init()] | The configuration data is null.");
        }
    }
}
