package panda.host.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PandaRemote extends Remote {
    String getPosts(String postRequest) throws RemoteException;

    boolean addPost(String addPostRequest) throws RemoteException;

}
