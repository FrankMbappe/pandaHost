package panda.host.ui.controllers;

import com.google.gson.Gson;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import panda.host.config.Configs;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.models.PandaFile;
import panda.host.model.models.Post;
import panda.host.model.models.User;
import panda.host.server.PandaRemoteImpl;
import panda.host.server.app.PandaServer;
import panda.host.utils.Current;
import panda.host.utils.Panda;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Objects;

import static panda.host.utils.Panda.convertLongSizeToString;
import static panda.host.utils.Panda.openFileDialog;

public class HomeController {
    PostData postData = new PostData();
    UserData userData = new UserData();

    public VBox vbox_flow;
    public Label lb_pageIsLoading;

    public Label lb_serverStatus;
    public StackPane img_serverStatus;
    public Button btn_launchServer;
    public Label lb_devicesConnected;
    public Label lb_serverId;
    public Label lb_fileStats;
    public Label lb_userStats;


    public TableView<PandaFile> table_files;
        public TableColumn<PandaFile, String> column_files_id;
        public TableColumn<PandaFile, String> column_files_date;
        public TableColumn<PandaFile, String> column_files_filename;
        public TableColumn<PandaFile, String> column_files_type;
        public TableColumn<PandaFile, String> column_files_size;
        public TableColumn<PandaFile, String> column_files_uploader;
        public StackPane btn_filesAdd;
        public StackPane btn_filesRemove;
        public StackPane btn_filesRefresh;


    public TableView<User> table_users;
        public TableColumn<User, String> column_users_username;
        public TableColumn<User, String> column_users_permissions;
        public TableColumn<User, String> column_users_status;
        public StackPane btn_usersAdd;
        public StackPane btn_usersRemove;
        public StackPane btn_usersRefresh;


    public void initialize(){
        // Initializing the current lists
        Current.fileList.setAll(postData.getAllFiles());
        Current.userList.setAll(userData.getAll());

        // Initially, the server isn't running
        setServerIsRunning(false);

        // Initializing tables
        initTableViews();

        // Updating the content in realtime
        updateContentInRealtime();

    }


    // @REALTIME
    void updateContentInRealtime(){
        // Adding listener that will refresh the file table content automatically
        Current.fileList.addListener((InvalidationListener) observable -> {
            System.out.println("[HomeCtrl, init()] | An action has been performed in the files' database.");
            updateFileTableContent();
        });

        // Adding listener that will refresh the user table content automatically
        Current.userList.addListener((InvalidationListener) observable -> {
            System.out.println("[HomeCtrl, init()] | An action has been performed in the users' database.");
            updateUserTableContent();
        });

        // TODO: Current.registeredClients.addListener() to display the connected devices in realtime

    }


    // ACTION EVENTS
    public void btn_launch(ActionEvent actionEvent) {
        // I launch the server
        try {

            PandaServer server = new PandaServer();
            server.run();

            // Updating the scene to show that the server is running
            setServerIsRunning(true);

        } catch (RemoteException e) {
            setServerIsRunning(false);
            e.printStackTrace();
        }
    }

    public void addFile(MouseEvent mouseEvent) {
        // I get the file from the FileDialog
        File file = openFileDialog(btn_filesRefresh, Panda.genFileFilter("All files", ""));

        if(file != null){
            // I create a post with the server name as the authorId and the file retrieved
            Post post = new Post(Objects.requireNonNull(Configs.getMySQLConfig()).getDbName(), file);
            try{
                new PandaRemoteImpl().addPost(new Gson().toJson(post));

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void addUser(MouseEvent mouseEvent) {
        // TODO: Add user from UserData
    }

    public void removeFile(MouseEvent mouseEvent) {
        PandaFile selectedFile = table_files.getSelectionModel().getSelectedItem();
        if(selectedFile != null){
            Post post = postData.get(selectedFile.getPostId());
            post.deleteFileProperties();
            // TODO: Edit post (Delete file in post) from PandaRemoteImpl
        }
    }

    public void removeUser(MouseEvent mouseEvent) {
        // TODO
    }

    public void refreshFiles(MouseEvent mouseEvent) {
        updateFileTableContent();
    }

    public void refreshUsers(MouseEvent mouseEvent) {
        updateUserTableContent();
    }



    // LOCAL METHODS
    void initTableViews(){
        /*      TABLE FILES     */
        // Initializing Table View column
        column_files_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_files_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        column_files_filename.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        column_files_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        column_files_size.setCellValueFactory(new PropertyValueFactory<>("sizeToString"));
        column_files_uploader.setCellValueFactory(new PropertyValueFactory<>("uploaderId"));
        table_files.setEditable(true);
        updateFileTableContent();

        /*      TABLE USERS     */
        // Initializing Table View column
        column_users_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_users_permissions.setCellValueFactory(new PropertyValueFactory<>("permissionMean"));
        column_users_status.setCellValueFactory(new PropertyValueFactory<>("statusMean"));
        table_users.setEditable(true);
        updateUserTableContent();

    }

    void updateFileTableContent(){
        // Loading data into table files
        table_files.setItems(Current.fileList);
        // Displaying stats
        lb_fileStats.textProperty().setValue(String.format("(%d files/%s)",
                Current.fileList.size(),
                convertLongSizeToString(postData.getTotalFilesSize())) // e.g: 8.5MB
        );
    }

    void updateUserTableContent(){
        // Loading data into table users
        table_users.setItems(Current.userList);
        // Displaying stats
        lb_userStats.setText(String.format("(%d registered)", Current.userList.size()));
    }

    void setServerIsRunning(boolean launched){
        if(launched){
            disableLaunchButton();
            lb_serverStatus.setText("The server is running...");
            img_serverStatus.getStyleClass().setAll("icon-ok");

        } else {
            enableLaunchButton();
            lb_serverStatus.setText("The server hasn't been launched yet.");
            img_serverStatus.getStyleClass().setAll("info-logo");
        }
    }

    void disableLaunchButton(){
        btn_launchServer.getStyleClass().add("inactive");
        btn_launchServer.setDisable(true);
        btn_launchServer.setText("Launched");
    }

    void enableLaunchButton(){
        btn_launchServer.getStyleClass().clear();
        btn_launchServer.getStyleClass().add("button");
        btn_launchServer.getStyleClass().add("submit-button");
        btn_launchServer.setText("Launch server");
        btn_launchServer.setDisable(false);
    }

    void displayLoading(){
        Panda.setNodeVisibility(false, vbox_flow);
        Panda.setNodeVisibility(true, lb_pageIsLoading);
    }

    void hideLoading(){
        Panda.setNodeVisibility(true, vbox_flow);
        Panda.setNodeVisibility(false, lb_pageIsLoading);
    }
}
