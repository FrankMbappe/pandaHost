package panda.host.ui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import panda.host.model.data.PostData;
import panda.host.model.data.UserData;
import panda.host.model.models.PandaFile;
import panda.host.model.models.User;
import panda.host.server.app.PandaServer;
import panda.host.utils.Panda;

import java.rmi.RemoteException;

import static panda.host.utils.Panda.convertLongSizeToString;

public class HomeController {

    public VBox vbox_flow;
    public Label lb_pageIsLoading;

    public Label lb_serverStatus;
    public StackPane img_serverStatus;
    public Button btn_launchServer;
    public Label lb_devicesConnected;
    public Label lb_serverId;

    public TableView<PandaFile> table_files;
        public TableColumn<PandaFile, String> column_files_id;
        public TableColumn<PandaFile, String> column_files_date;
        public TableColumn<PandaFile, String> column_files_filename;
        public TableColumn<PandaFile, String> column_files_type;
        public TableColumn<PandaFile, String> column_files_size;
        public TableColumn<PandaFile, String> column_files_uploader;

    public TableView<User> table_users;
        public TableColumn<User, String> column_users_username;
        public TableColumn<User, String> column_users_permissions;
    public Label lb_fileStats;
    public Label lb_userStats;


    public void initialize(){
        setServerIsLaunched(false);
        initFilesTable();
        initUsersTable();
    }

    public void btn_launch(ActionEvent actionEvent) {
        // I launch the server
        try {
            PandaServer server = new PandaServer();
            server.launch();
            // This change the page to show the user that the server is running
            setServerIsLaunched(true);

        } catch (RemoteException e) {
            setServerIsLaunched(false);
            e.printStackTrace();
        }
    }

    ObservableList<PandaFile> getFileListFromDb(){
        ObservableList<PandaFile> fileObservableList = FXCollections.observableArrayList();
        fileObservableList.addAll(new PostData().getAllFiles());
        return fileObservableList;
    }
    ObservableList<User> getUserListFromDb(){
        ObservableList<User> fileObservableList = FXCollections.observableArrayList();
        fileObservableList.addAll(new UserData().getAll());
        return fileObservableList;
    }
    void initFilesTable(){
        /*      TABLE FILES     */
        // Initializing Table View column
        column_files_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_files_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        column_files_filename.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        column_files_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        column_files_size.setCellValueFactory(new PropertyValueFactory<>("sizeToString"));
        column_files_uploader.setCellValueFactory(new PropertyValueFactory<>("uploaderId"));
        // Loading data into table files
        table_files.setItems(getFileListFromDb());
        // Displaying stats
        lb_fileStats.setText(String.format("(%d files/%s)",
                getFileListFromDb().size(),
                convertLongSizeToString(new PostData().getTotalFilesSize())) // e.g: 8.5MB
        );
    }
    void initUsersTable(){
        /*      TABLE USERS     */
        // Initializing Table View column
        column_users_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        column_users_permissions.setCellValueFactory(new PropertyValueFactory<>("permissionMean"));
        // Loading data into table users
        table_users.setItems(getUserListFromDb());
        // Displaying stats
        lb_userStats.setText(String.format("(%d registered)", getUserListFromDb().size()));
    }
    void setServerIsLaunched(boolean launched){
        if(launched){
            disableLaunchButton();
            lb_serverStatus.setText("The server is running...");
            img_serverStatus.getStyleClass().clear();
            img_serverStatus.getStyleClass().add("icon-ok");
        } else {
            enableLaunchButton();
            lb_serverStatus.setText("The server hasn't been launched yet.");
            img_serverStatus.getStyleClass().clear();
            img_serverStatus.getStyleClass().add("info-logo");
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
