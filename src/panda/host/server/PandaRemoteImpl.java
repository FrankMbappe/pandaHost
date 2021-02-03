package panda.host.server;

import com.google.gson.Gson;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import org.apache.commons.io.FileUtils;
import panda.host.config.Configs;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.models.ClientProcess;
import panda.host.model.models.Post;
import panda.host.model.models.User;
import panda.host.server.app.WideActionPerformer;
import panda.host.utils.Current;
import panda.host.utils.Panda;
import panda.host.utils.WideActions;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class PandaRemoteImpl extends UnicastRemoteObject implements PandaRemote, Runnable {
    public PandaRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public synchronized void register(String clientId, SyncChannel syncChannel) {
        // I add a new client registration to the current ones
        ClientProcess clientProcess = new ClientProcess(clientId);

        if (!Current.registeredClients.containsKey(clientProcess)) {
            // Adding the client registration
            Map<ClientProcess, SyncChannel> map = new HashMap<>(Current.registeredClients);
            map.put(clientProcess, syncChannel);
            Current.registeredClients = FXCollections.observableMap(map);
            System.out.println("[PandaRemote, register()] | Added: CLIENT@REGISTRATION - " + clientId);

            // Notifying the new client
            WideActionPerformer.perform(WideActions.ServerStatusUpdate, true);
        }
    }

    @Override
    public synchronized void unregister(String clientId) {
        // I remove a client registration from the current ones
        ClientProcess clientProcess = new ClientProcess(clientId);

        Current.registeredClients.remove(clientProcess);

        System.out.println("[PandaRemote, register()] | Removed: CLIENT@REGISTRATION: - " + clientId);
    }

    @Override
    public synchronized boolean serverIsAccessible() throws RemoteException {
        return true;
    }

    @Override
    public synchronized String getPosts(String filterToJson) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, getPosts()] | Method remotely requested.");

        if (Configs.fileIsValid()) {

            System.out.println("[PandaRemote, getPosts()] | Attempting to access the post database...");

            // I get the matching data converted to json
            String matchingDataToJson = new PostData().getJsonMatchingDataFromJsonFilter(filterToJson);
            System.out.println(String.format("[PandaRemote, getPosts()] | Data retrieved and converted to JSON: '%s'.",
                    matchingDataToJson));

            System.out.println("PandaRemote, getPosts()] | Task ended.");

            return matchingDataToJson;
        }
        return null;
    }

    @Override
    public synchronized boolean addPost(String postToJson) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, addPost()] | Method remotely requested.");

        // The variable that will determine if a post has been actually added.
        boolean successfullyAdded = false;

        if (Configs.fileIsValid()) {

            System.out.println("[PandaRemote, addPost()] | Attempting to access the database...");

            // I deserialize the post object from JSON
            Post deserializedPost = new Gson().fromJson(postToJson, Post.class);

            // I add the post to the database
            successfullyAdded = new PostData().add(deserializedPost);
            System.out.println("[PandaRemote, addPost()] | Successfully added: " + successfullyAdded);

            System.out.println("PandaRemote, addPost()] | Task ended.");

        }
        return successfullyAdded;
    }

    @Override
    public synchronized byte[] downloadPostFile(String userId, String fileId, String fileExt) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, downloadPostFile()] | Method remotely requested.");

        // The file path the request is looking for
        String expectedPath = Panda.generateAPandaFilePath(userId, fileId, fileExt);
        System.out.println(String.format("[PandaRemote, downloadPostFile()] | The file to be downloaded: '%s'.", expectedPath));
        File expectedFile = new File(expectedPath);

        try {
            if (expectedFile.exists()) {
                return FileUtils.readFileToByteArray(expectedFile);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(String.format("[PandaRemote, downloadPostFile()] |Error! Something went wrong while downloading: '%s' " +
                "from the server.", expectedPath));
        return null;
    }

    @Override
    public synchronized String logUserIn(String credentialsToJson) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, logUserIn()] | Method remotely requested.");

        if (Configs.fileIsValid()) {

            System.out.println("[PandaRemote, logUserIn()] | Attempting to access the database..");

            // I get the authentication object generated from the credentials
            String generatedAuthToJson = new UserData().getJsonAuthFromJsonCredentials(credentialsToJson);
            System.out.println(String.format("[PandaRemote, logUserIn()] | Auth object generated and converted to JSON: '%s'.",
                    generatedAuthToJson));

            System.out.println("PandaRemote, logUserIn()] | Task ended.");

            return generatedAuthToJson;
        }
        return null;
    }

    @Override
    public synchronized boolean logUserOut(String username) throws RemoteException {
        System.out.println("\n#\tPANDA@OPERATION");
        System.out.println("-----------------------");
        System.out.println("[PandaRemote, logUserIn()] | Method remotely requested.");

        UserData userData = new UserData();
        boolean userHasLoggedOut = false;

        if (Configs.fileIsValid()) {
            // I get the complete user, from the username argument
            User user = userData.get(username);

            // Then I set its status to 0 (Or 'Logged Out')
            user.setStatus(0);

            // I update the user in the database
            // TODO: Change the Db form so that a user has a Status property that I can edit
            userData.update(user);

            // When everything is done
            userHasLoggedOut = true;
        }

        return userHasLoggedOut;
    }

    @Override
    public void run() {
        // Notifying all the clients that the server is running
        WideActionPerformer.perform(WideActions.ServerStatusUpdate, true);

        // Adding a listener to notify all the clients when the post list has been changed
        Current.postList.addListener((InvalidationListener) observable -> {
            System.out.println("[CHANNEL@SYNC] | The post list has been updated.");

            // Notifying all the clients and updating their post list
            WideActionPerformer.perform(WideActions.PostListUpdate, new Gson().toJson(Current.postList));

        });

    }
}