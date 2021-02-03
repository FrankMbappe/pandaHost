package panda.host.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Class used by clients to synchronize their data with the server
 * Each time the server updates its data, it uses the methods of this class to perform Callbacks
 * i.e inform all the client that the data has been changed.
 */
public interface SyncChannel extends Remote {
    void updatePosts(String postListToJson) throws RemoteException;

    void updateServerStatus(boolean serverIsRunning) throws RemoteException;
}
