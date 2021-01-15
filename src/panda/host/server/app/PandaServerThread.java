package panda.host.server.app;

import panda.host.utils.Panda;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class PandaServerThread extends Thread {
    private final Socket socket;
    private String clientRequest;

    public PandaServerThread(Socket socket){
        super("PandaServerThread");
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            clientRequest = new DataInputStream(socket.getInputStream()).readUTF();
            var data = Panda.extractFiltersFromPandaCode(clientRequest);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPandaUserId(){
        return null;
    }

}
