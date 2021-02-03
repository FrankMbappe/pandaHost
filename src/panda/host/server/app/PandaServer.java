package panda.host.server.app;

import panda.host.server.PandaRemoteImpl;
import panda.host.utils.Panda;

import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PandaServer extends UnicastRemoteObject {
    private static HashMap<String, Socket> usersLoggedIn;

    public PandaServer() throws RemoteException {

    }

    public void run() {
        try{
            PandaRemoteImpl pandaRemote = new PandaRemoteImpl();

            System.setProperty("java.rmi.server.hostname","192.168.173.1");
            Registry registry = LocateRegistry.createRegistry(Panda.DEFAULT_PORT);
            registry.rebind(Panda.DEFAULT_REMOTE_URL, pandaRemote);
            System.out.println("\n-----------------------------------------");
            System.out.println("+              PANDA@SERVER             +");
            System.out.println("-----------------------------------------");
            System.out.println("Object registered: " + Panda.DEFAULT_REMOTE_URL);

            System.out.println("The Panda server is running...");
            System.out.println("-----------------------------------------");

            // Creating a thread whereby clients will be synced, using their SyncChannel
            new Thread(pandaRemote, "SyncServer").start();

        } catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
