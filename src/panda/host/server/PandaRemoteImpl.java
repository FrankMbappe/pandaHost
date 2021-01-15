package panda.host.server;

import panda.host.config.database.MySQLConfigs;
import panda.host.config.database.MySQLConnection;
import panda.host.model.data.PostData;
import panda.host.model.models.MySQLConfig;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PandaRemoteImpl extends UnicastRemoteObject implements PandaRemote {
    public PandaRemoteImpl() throws RemoteException {
        super();
    }

    @Override
    public String getPosts(String pandaCode) {
        System.out.println("\n\n[PandaRemote, getPosts()] | Method remotely requested.");
        MySQLConfig mySQLConfiguration = MySQLConfigs.get();

        if (mySQLConfiguration != null) {

            System.out.println("[PandaRemote, getPosts()] | Attempting to access the database");

            // I create a mysql connection
            MySQLConnection connection = new MySQLConnection(mySQLConfiguration);

            // I get the matching data converted to json
            String matchingDataToJson = new PostData(connection).getMatchingDataFromPandaCode(pandaCode);
            System.out.println(String.format("[PandaRemote, getPosts()] | Data retrieved and converted to JSON: '%s'.",
                    matchingDataToJson));

            // I close the connection, I don't need it anymore
            connection.close();

            System.out.println("PandaRemote, getPosts()] | Task ended.");

            return matchingDataToJson;
        }
        return null;
    }

    @Override
    public boolean addPost(String addPostRequest){
        return false;
    }
}
