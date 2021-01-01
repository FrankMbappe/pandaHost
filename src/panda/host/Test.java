package panda.host;

import panda.host.config.database.MySQLConnection;
import panda.host.config.database.MySQLConfigs;
import panda.host.model.data.UserData;
import panda.host.model.models.MySQLConfig;
import panda.host.model.models.User;

import java.util.Objects;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("-- PandaHost Console --");

        // Initializing Panda from the configurations' file
//        Panda.init(MySQLConfigs.get());
//        UserData userData = new UserData(new MySQLConnection(Objects.requireNonNull(MySQLConfigs.get())));
//        userData.add(getUser());

        System.out.println("\nTest done.");
    }


    static void inputInitConfigs(){
        String server, username, password;
        Scanner sc = new Scanner(System.in);

        // Server config
        System.out.println("Enter the server name: ");
        server = sc.nextLine();

        // Getting credentials
        System.out.println("Username: ");
        username = sc.nextLine();
        System.out.println("Password: ");
        password = sc.nextLine();

        MySQLConfig config = new MySQLConfig(username, password, server);
        MySQLConfigs.set(config);
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

