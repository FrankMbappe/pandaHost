package panda.host.server.app;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class PandaServerSocket {
    private HashMap<String, Socket> usersLoggedIn;
    private final int port;

    public PandaServerSocket(int port, HashMap<String, Socket> usersLoggedIn){
        this.port = port;
        this.usersLoggedIn = usersLoggedIn;
    }

    public void launch(){
        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("[PandaServerSocket, launch()] | The Panda server is running...");

//            while (true){
//                Socket clientSocket = serverSocket.accept();
//                PandaServerThread pandaThread = new PandaServerThread(clientSocket);
//                String userId = pandaThread.getPandaUserId();
//
//                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());
//
//                if(!usersLoggedIn.containsKey(userId)){
//                    usersLoggedIn.put(userId, clientSocket);
//                    outputStream.writeUTF(userId + ", you are now logged in.");
//                    System.out.println("[PandaServerSocket, launch()] | User added with ID: " + userId);
//
//                } else {
//                    outputStream.writeUTF(userId + ", you are are already logged in.");
//                    System.out.println("[PandaServerSocket, launch()] | User already exists with ID: " + userId);
//                }
//
//            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
