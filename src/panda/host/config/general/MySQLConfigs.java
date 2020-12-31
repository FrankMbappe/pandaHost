package panda.host.config.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import panda.host.model.models.MySQLConfig;

import java.io.File;
import java.io.IOException;

public class MySQLConfigs {
    static ObjectMapper mapper = new ObjectMapper();
    static File file = new File("src/panda/host/config/configs.json");

    /**
     * Deserializes the configurations' file
     * @return A configuration object
     */
    public static MySQLConfig get(){
        try{
            MySQLConfig config = mapper.readValue(file, MySQLConfig.class);
            //System.out.println("[Configs, get()] | Configurations' file content:\n" + config.toString());
            return config;
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
