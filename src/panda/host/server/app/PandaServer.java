package panda.host.server.app;

import panda.host.server.PandaRemoteImpl;
import panda.host.utils.Panda;

import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class PandaServer extends UnicastRemoteObject {
    private static HashMap<String, Socket> usersLoggedIn;

    public PandaServer() throws RemoteException {

    }

    public void launch() {
        try{
            System.setProperty("java.rmi.server.hostname","192.168.173.1");
            LocateRegistry.createRegistry(Panda.DEFAULT_PORT);
            Naming.rebind(Panda.DEFAULT_REMOTE_URL, new PandaRemoteImpl());
            System.out.println("Object registered: " + Panda.DEFAULT_REMOTE_URL);

            System.out.println("The Panda server is running...");
            System.out.println("-----------------------------------------");

        } catch (RemoteException | MalformedURLException e){
            e.printStackTrace();
        }
    }
}
