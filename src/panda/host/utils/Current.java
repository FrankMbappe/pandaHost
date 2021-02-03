package panda.host.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import panda.host.config.database.MySQLConnection;
import panda.host.model.models.ClientProcess;
import panda.host.model.models.PandaFile;
import panda.host.model.models.Post;
import panda.host.model.models.User;
import panda.host.server.SyncChannel;

public class Current {
    /**
     * The MySQL database connection that will be used throughout the execution
     */
    public static MySQLConnection dbConnection;

    /**
     * The set of clients PandaGuest connected to the server
     */
    public static ObservableMap<ClientProcess, SyncChannel> registeredClients = FXCollections.emptyObservableMap();

    /**
     * The list of post retrieved from the database
     */
    public static ObservableList<Post> postList = FXCollections.observableArrayList();

    /**
     * The list of files extracted from the posts retrieved from the database
     */
    public static ObservableList<PandaFile> fileList = FXCollections.observableArrayList();

    /**
     * The list of users retrieved from the database
     */
    public static ObservableList<User> userList = FXCollections.observableArrayList();

}
