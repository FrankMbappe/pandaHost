package panda.host;

import panda.host.model.models.User;
import panda.host.server.app.PandaServer;

import java.rmi.RemoteException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("-- PandaHost Console --");

        // Initializing PandaHost from scratch
//        Panda.init(null);

        try {
            PandaServer server = new PandaServer();
            server.run();

        } catch (RemoteException e) {
            e.printStackTrace();
        }

//        System.out.println("\nTest done.");
    }



    static User getUser(){
        String username, password;
        int permissions;
        Scanner sc = new Scanner(System.in);

        System.out.println("Username: ");
        username = sc.nextLine();
        System.out.println("Password: ");
        password = sc.nextLine();
        System.out.println("Permissions (0=No,1=R,2=RW): ");
        permissions = sc.nextInt();

        return new User(username, password, permissions);
    }
}

