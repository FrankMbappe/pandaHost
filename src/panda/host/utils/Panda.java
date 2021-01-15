package panda.host.utils;

import panda.host.config.database.MySQLConnection;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.models.MySQLConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Panda.
 */
public class Panda {
    // Default remote object URL
    public static final String DEFAULT_REMOTE_URL = "rmi://localhost/panda";

    // Default app port
    public static final int DEFAULT_PORT = 77;

    // Marshalling and Unmarshalling patterns
    public enum PandaOperation {
        PANDAOP_REQUEST_GET_POSTS, PANDAOP_RESPONSE_GET_POSTS,
        PANDAOP_REQUEST_GET_CONNECTION, PANDAOP_RESPONSE_GET_CONNECTION
    }
    public static final HashMap<PandaOperation, Pattern> PANDA_DECODING_PATTERNS = new HashMap<>(){{
        put(PandaOperation.PANDAOP_REQUEST_GET_CONNECTION,
                Pattern.compile("panda@connect\\?guest='(.*?)'&user='(.*?)'&password='(.*?)'"));
        put(PandaOperation.PANDAOP_REQUEST_GET_POSTS,
               Pattern.compile("panda@getpost\\?all='(.*?)'&filetype='(.*?)'&class='(.*?)'"));
        put(PandaOperation.PANDAOP_RESPONSE_GET_CONNECTION,
                Pattern.compile("panda@connect\\?rescode='(.*?)'&user='(.*?)'&grant='(.*?)'"));
        put(PandaOperation.PANDAOP_RESPONSE_GET_POSTS,
               Pattern.compile("panda@getpost\\?rescode='(.*?)'&data='(.*?)'"));
    }};
    public static final HashMap<PandaOperation, String> PANDA_ENCODING_PATTERNS = new HashMap<>(){{
        put(PandaOperation.PANDAOP_REQUEST_GET_CONNECTION,
                "panda@connect?guest='%s'&user='%s'&password='%s'");
        put(PandaOperation.PANDAOP_REQUEST_GET_POSTS,
                "panda@getpost?all='%s'&filetype='%s'&class='%s'");
        put(PandaOperation.PANDAOP_RESPONSE_GET_CONNECTION,
                "panda@connect?rescode='%s'&user='%s'&grant='%s'");
        put(PandaOperation.PANDAOP_RESPONSE_GET_POSTS,
                "panda@getpost?rescode='%s'&data='%s'");
    }};

    // Default file paths
    public static final String PATH_TO_CONFIG_FILE = "src/panda/host/config/configs.json";
    public static final String DEFAULT_XLSX_FILE_PATH = "src/panda/host/config/sample.xlsx";

    // Initializing PandaHost
    public static void init(MySQLConfig config){
        if(config != null){
            // Establishing the connection
            MySQLConnection mySQLConn = new MySQLConnection(config);

            if(mySQLConn.exists()){
                UserData userData = new UserData(mySQLConn);
                PostData postData = new PostData(mySQLConn);

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

    // Extract filter list from a panda code
    public static ArrayList<String> extractFiltersFromPandaCode(String pandaCode) {
        if (pandaCode != null) {
            // I get the panda operation type
            PandaOperation pandaOperation = getOperationTypeFromPandaCode(pandaCode);
            // Then the corresponding pattern
            Pattern matchingPattern = PANDA_DECODING_PATTERNS.get(pandaOperation);
            // Then I create a matcher from that pattern
            Matcher matcher = matchingPattern.matcher(pandaCode);
            // If pattern matches (It must do so, obviously)
            if (matcher.find()) {
                System.out.println(String.format("[Panda, extractFiltersFromPandaCode()] | The panda code: \"%s\"\n" +
                        "matches the pattern of '%s' operation.", pandaCode, pandaOperation));
                // I return the data into an ArrayList<String>
                return getGroupsFromMatcher(matcher);

            } else {
                System.out.println("[Panda, extractFiltersFromPandaCode()] | Error! Unknown response pattern.");
            }
        }
        return null;
    }

    // Get the panda operation type from a panda code
    public static PandaOperation getOperationTypeFromPandaCode(String pandaCode){
        if (pandaCode != null) {
            // Since all the patterns are unique, we just have to iterate through them till one matches
            // If not, the server response would have simply been malformed
            for (var decodingPattern : PANDA_DECODING_PATTERNS.entrySet()) {
                // I retrieve the value (Pattern) from the entry set <PandaOperation, Pattern>
                Pattern pattern = decodingPattern.getValue();
                // If marshalledCode matches the pattern retrieved
                boolean matches = pandaCode.matches(pattern.pattern());
                if (matches) {
                    System.out.println("[Panda, extractFiltersFromPandaCode()] | A pattern matched.");
                    // I return the marshall operation type
                    return decodingPattern.getKey();
                }
            }
        }
        return null;
    }

    // Extracting groups from a matcher object
    private static ArrayList<String> getGroupsFromMatcher(Matcher matcher) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            data.add(matcher.group(i));
            System.out.println("[Panda, getGroupsFromMatcher()] | Group extracted: " + data.get(i - 1));
        }
        return data;
    }

}
