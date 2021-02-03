package panda.host.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PandaRemote extends Remote {
    // A client use notifies the server that it exists
    void register(String clientId, SyncChannel syncChannel) throws RemoteException;

    // A client use notifies the server that it is dead
    void unregister(String clientId) throws RemoteException;

    // Dummy method to check if the server is accessible
    boolean serverIsAccessible() throws RemoteException;

    // Get a list of posts from the db under a JSON format, using a set of filters embedded in the request.
    String getPosts(String filterToJson) throws RemoteException;

    // Add a new post to the db, and returns true if the post has been effectively added.
    boolean addPost(String postToJson) throws RemoteException;

    // Get a post' file stored in the server
    byte[] downloadPostFile(String userId, String fileId, String fileExt) throws RemoteException;

    // Get an authentication object which contains the user's info and the auth status.
    String logUserIn(String credentialsToJson) throws RemoteException;

    // Log a user out
    boolean logUserOut(String username) throws RemoteException;

}
