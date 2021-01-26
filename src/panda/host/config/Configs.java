package panda.host.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import panda.host.config.database.MySQLConnection;
import panda.host.model.models.MySQLConfig;
import panda.host.utils.Current;
import panda.host.utils.Panda;

import java.io.File;
import java.io.IOException;

public class Configs {
    static ObjectMapper mapper = new ObjectMapper();
    static File file = new File(Panda.PATH_TO_CONFIG_FILE);

    /**
     * Deserializes the configurations' file
     * @return The MySQL configuration object
     */
    public static MySQLConfig getMySQLConfig(){
        try{
            //System.out.println("[Configs, get()] | Configurations' file content:\n" + config.toString());
            return mapper.readValue(file, MySQLConfig.class);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("[Configs, getMySQLConfig()] | The configuration file doesn't exist or is not valid.");
        }
        return null;
    }

    /**
     * Serializes a config object into the configurations' file
     * @param config The MySQL configuration object
     * @return A boolean: Operation succeeded
     */
    public static boolean setMySQLConfig(MySQLConfig config){
        try {
            mapper.writeValue(file, config);
            Current.dbConnection = new MySQLConnection(config);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
