package panda.host;

import panda.host.config.general.MySQLConfigs;
import panda.host.config.general.Panda;
import panda.host.model.models.MySQLConfig;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("-- PandaHost Console --");

        inputInitConfigs();

        // Initializing Panda from the configurations' file
        Panda.init(MySQLConfigs.get());

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
}

