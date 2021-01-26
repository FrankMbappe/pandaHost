package panda.host.server;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import panda.host.config.Configs;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.models.MySQLConfig;
import panda.host.model.models.Post;
import panda.host.utils.Panda;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PandaRemoteImpl extends UnicastRemoteObject implements PandaRemote {
    public PandaRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public String getPosts(String pandaCode) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, getPosts()] | Method remotely requested.");
        MySQLConfig mySQLConfiguration = Configs.getMySQLConfig();

        if (mySQLConfiguration != null) {

            System.out.println("[PandaRemote, getPosts()] | Attempting to access the database...");

            // I create a mysql connection
//            MySQLConnection connection = new MySQLConnection(mySQLConfiguration);

            // I get the matching data converted to json
            String matchingDataToJson = new PostData().getMatchingDataFromPandaCode(pandaCode);
            System.out.println(String.format("[PandaRemote, getPosts()] | Data retrieved and converted to JSON: '%s'.",
                    matchingDataToJson));

            // I close the connection, I don't need it anymore
//            connection.close();

            System.out.println("PandaRemote, getPosts()] | Task ended.");

            return matchingDataToJson;
        }
        return null;
    }

    @Override
    public boolean addPost(String postToJson) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, addPost()] | Method remotely requested.");
        MySQLConfig mySQLConfiguration = Configs.getMySQLConfig();

        // The variable that will determine if a post has been actually added.
        boolean successfullyAdded = false;

        if (mySQLConfiguration != null) {

            System.out.println("[PandaRemote, addPost()] | Attempting to access the database...");

            // I create a mysql connection
//            MySQLConnection connection = new MySQLConnection(mySQLConfiguration);

            // I deserialize the post object from JSON
            Post deserializedPost = new Gson().fromJson(postToJson, Post.class);

            // I add the post to the database
            successfullyAdded = new PostData().add(deserializedPost);
            System.out.println("[PandaRemote, addPost()] | Successfully added: " + successfullyAdded);

            // I close the connection, I don't need it anymore
//            connection.close();

            System.out.println("PandaRemote, addPost()] | Task ended.");

        }
        return successfullyAdded;
    }

    @Override
    public byte[] downloadPostFile(String userId, String fileId, String fileExt) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, downloadPostFile()] | Method remotely requested.");

        // The file path the request is looking for
        String expectedPath = Panda.generateAPandaFilePath(userId, fileId, fileExt);
        System.out.println(String.format("[PandaRemote, downloadPostFile()] | The file to be downloaded: '%s'.", expectedPath));
        File expectedFile = new File(expectedPath);

        try{
            if(expectedFile.exists()){
                return FileUtils.readFileToByteArray(expectedFile);
            }

        } catch (IOException e){
            e.printStackTrace();
        }
        System.out.println(String.format("[PandaRemote, downloadPostFile()] |Error! Something went wrong while downloading: '%s' " +
                        "from the server.", expectedPath));
        return null;
    }

    @Override
    public String logUserIn(String pandaCode) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, logUserIn()] | Method remotely requested.");
        MySQLConfig mySQLConfiguration = Configs.getMySQLConfig();

        if (mySQLConfiguration != null) {

            System.out.println("[PandaRemote, logUserIn()] | Attempting to access the database..");

            // I create a mysql connection
//            MySQLConnection connection = new MySQLConnection(mySQLConfiguration);

            // I get the authentication object generated from the credentials
            String authObjectToJson = new UserData().getAuthObjectFromPandaCodeToJson(pandaCode);
            System.out.println(String.format("[PandaRemote, logUserIn()] | Auth object generated and converted to JSON: '%s'.",
                    authObjectToJson));

            // I close the connection, I don't need it anymore
//            connection.close();

            System.out.println("PandaRemote, logUserIn()] | Task ended.");

            return authObjectToJson;
        }
        return null;
    }
}
