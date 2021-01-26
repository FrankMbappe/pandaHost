package panda.host.utils;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import panda.host.config.Configs;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.exceptions.BadPandaConfigsException;
import panda.host.model.models.MySQLConfig;
import panda.host.ui.scenes.PandaScene;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
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
                Pattern.compile("panda@connect\\?user='(.*?)'&password='(.*?)'"));
        put(PandaOperation.PANDAOP_REQUEST_GET_POSTS,
               Pattern.compile("panda@getpost\\?all='(.*?)'&filetype='(.*?)'&class='(.*?)'"));
        put(PandaOperation.PANDAOP_RESPONSE_GET_CONNECTION,
                Pattern.compile("panda@connect\\?rescode='(.*?)'&user='(.*?)'&grant='(.*?)'"));
        put(PandaOperation.PANDAOP_RESPONSE_GET_POSTS,
               Pattern.compile("panda@getpost\\?rescode='(.*?)'&data='(.*?)'"));
    }};
    public static final HashMap<PandaOperation, String> PANDA_ENCODING_PATTERNS = new HashMap<>(){{
        put(PandaOperation.PANDAOP_REQUEST_GET_CONNECTION,
                "panda@connect?user='%s'&password='%s'");
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
    public static final String DEFAULT_FILES_LOCATION = "src/panda/host/files"; // Notice that there's no '/' at the end

    // Default values
    public static final String DEFAULT_SPLIT_CHAR = ";";
    public static final String DEFAULT_DATE_FORMAT = "E, dd MMM yyyy";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm";

    // Initializing PandaHost
    public static void init(MySQLConfig configurations) throws BadPandaConfigsException {
        // I changed what's below since I'm now dealing with GUI but no more console.
        // 'config == null' means that we are initializing PandaHost from scratch
        if(configurations == null){
            throw new BadPandaConfigsException();
        }

        // Establishing the connection
        if(Current.dbConnection.exists()){
            UserData userData = new UserData();
            PostData postData = new PostData();

            // Database creation
            Current.dbConnection.executeUpdateStatement("CREATE DATABASE IF NOT EXISTS " + configurations.getDbName(), false);
            System.out.println(String.format("\nDatabase '%s' created.\n", configurations.getDbName()));

            // Users' table creation
            userData.init();

            // Posts' table creation
            postData.init();


            System.out.println("[Panda, init()] | Panda was successfully configured.");

        } else {
            System.out.println("[Panda, init()] | Error: The connection doesn't exist.");
            throw new BadPandaConfigsException();
        }
    }

    // Input configuration' properties to initialize PandaHost
    static void consoleInputInitConfigs(){
        String server, username, password;
        Scanner sc = new Scanner(System.in);

        // Getting MySQL Db credentials
        System.out.println("# connection@MySQL | Username: ");
        username = sc.nextLine();
        System.out.println("# connection@MySQL | Password: ");
        password = sc.nextLine();

        // Server config
        System.out.println("\nEnter a name for the new server: ");
        server = sc.nextLine();

        MySQLConfig config = new MySQLConfig(username, password, server);
        Configs.setMySQLConfig(config);
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
    private static @NotNull
    ArrayList<String> getGroupsFromMatcher(Matcher matcher) {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            data.add(matcher.group(i));
            System.out.println("[Panda, getGroupsFromMatcher()] | Group extracted: " + data.get(i - 1));
        }
        return data;
    }

    // Getting a random file ID for a user
    public static String getARandomFileId(String folderName, String fileExt){
        // I get the random ID for the file
        String randomFileId = UUID.randomUUID().toString();

        // I build the path using it
        String generatedPath = generateAPandaFilePath(folderName, randomFileId, fileExt);

        // While the randomly generated file ID already exists, I generate a new one
        while (idAlreadyExists(generatedPath)){
            randomFileId = UUID.randomUUID().toString();
            System.out.println("[Panda, getARandomFileId()] | ID was already existing. New generated file ID: " + randomFileId + "\r");
            generatedPath = generateAPandaFilePath(folderName, randomFileId, fileExt);
        }

        System.out.println("[Panda, getARandomFileId()] | Final fileId generated: " + randomFileId);

        return randomFileId;
    }

    // Getting a panda file path (Panda files are stored in Panda.DEFAULT_FILES_LOCATION)
    public static String generateAPandaFilePath(String folderName, String fileName, String fileExt){
        // I build the path using the args    (e.g: "     src/host/files    / xyz@mail.com/ 4e5s68emp.pdf")
        //                                           |DEFAULT_FILES_LOCATION|  AuthorId  |   fileId  | fileExt
        return String.format("%s/%s/%s.%s", DEFAULT_FILES_LOCATION, folderName, fileName, fileExt);
    }

    // Checking if a random file ID already exist
    private static boolean idAlreadyExists(String pathToCheck){
        System.out.println("[Panda, idAlreadyExists()] | The following path has been checked: " + pathToCheck);
        return new File(pathToCheck).exists();
    }

    // Getting a formatted date
    public static String getFormattedDate(@NotNull LocalDateTime dateTime){

        LocalDate date = dateTime.toLocalDate();

        // Initializing the prefix that will contains either 'Today', 'Yesterday' or the corresponding date
        String prefix = dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        String time = dateTime.format(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT));

//        System.out.println(String.format("[Panda, getFormattedDate()] | DateTime to date: '%s'.", prefix));

        if (LocalDate.now().equals(date)) {
            prefix = "Today";
        } else if (LocalDate.now().minusDays(1).equals(date)) {
            prefix = "Yesterday";
        }

        return String.format("%s, %s", prefix, time);
    }

    // Converting a size from long to KB
    public static double convertToKB(long size){
        return size >> 10;
    }

    // Converting a size from long to MB
    public static double convertToMB(long size){
        return size >> 20;
    }

    // Converting a size from long whether KB or MB, depending on the value
    public static String convertLongSizeToString(long size){
        double sizeInMB = convertToMB(size);
        if(sizeInMB >= 1){
            return sizeInMB + "MB";
        } else {
            return convertToKB(size) + "KB";
        }
    }



    /*     FXML     */

    // Hiding nodes in a scene
    public static void setNodeVisibility(boolean isVisible, Node... nodes){
        for (Node node: nodes) {
            // Associating the fact that a node is visible, to the fact that it currently exists
            node.managedProperty().bind(node.visibleProperty());
            // Hiding the node
            node.setVisible(isVisible);
        }
    }

    // Switching scenes
    public static void switchScene(Scene currentScene, PandaScene nextScene){
        // To switch scenes, I firstly get the window
        Stage window = (Stage) currentScene.getWindow();

        try{
            // Then I switch to a new scene
            window.setScene(nextScene.get());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // Iterating through a set of textFields to check if at least one of them is empty
    public static boolean txtsAreNotEmpty(TextField... textFields){
        boolean txtsAreNotEmpty = true;
        for(var txt : textFields){
            // A textfield is not empty if when deleting all spaces, the text isn't equal to ""
            txtsAreNotEmpty = txtsAreNotEmpty && !txt.getText().replaceAll("\\s", "").equals("");
        }
        return txtsAreNotEmpty;
    }

    // Closing the whole application
    public static void closePanda(Node aNode){
        Stage stage = (Stage) aNode.getScene().getWindow();
        stage.close();
        Current.dbConnection.close();
        System.exit(0);
    }

    // Getting a file from a FileDialog
    public static File openFileDialog(Node aNode, FileChooser.ExtensionFilter... filters){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a file");

        fileChooser.getExtensionFilters().addAll(filters);

        return fileChooser.showOpenDialog(aNode.getScene().getWindow());
    }
    // Getting a file extension filter
    public static  FileChooser.ExtensionFilter genFileFilter(String name, String extension){
        if (extension.equals("")){
           return  new FileChooser.ExtensionFilter(name, "*");
        }
        return new FileChooser.ExtensionFilter(name, "*." + extension);
    }
}
