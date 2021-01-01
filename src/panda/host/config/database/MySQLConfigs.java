package panda.host.config.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import panda.host.model.models.MySQLConfig;

import java.io.File;
import java.io.IOException;

public class MySQLConfigs {
    public static final String PATH_TO_CONFIG_FILE = "src/panda/host/config/configs.json";

    static ObjectMapper mapper = new ObjectMapper();
    static File file = new File(PATH_TO_CONFIG_FILE);

    /**
     * Deserializes the configurations' file
     * @return A configuration object
     */
    public static MySQLConfig get(){
        try{
            //System.out.println("[Configs, get()] | Configurations' file content:\n" + config.toString());
            return mapper.readValue(file, MySQLConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serializes a config object into the configurations' file
     * @param config A configuration object
     * @return A boolean: Operation succeeded
     */
    public static boolean set(MySQLConfig config){
        try {
            mapper.writeValue(file, config);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
