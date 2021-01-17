package panda.host.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PandaRemote extends Remote {
    // Get a list of posts from the db under a JSON format, using a set of filters embedded in the request.
    String getPosts(String postRequest) throws RemoteException;

    // Add a new post to the db, and returns true if the post has been effectively added.
    boolean addPost(String addPostRequest) throws RemoteException;

    // Get an authentication object which contains the user's info and the auth status.
    String logUserIn(String userRequest) throws RemoteException;

}
