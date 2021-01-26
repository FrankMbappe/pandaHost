package panda.host.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import panda.host.config.Configs;
import panda.host.config.database.MySQLConnection;
import panda.host.utils.Current;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // To add SVG support to PandaHost
//        SvgImageLoaderFactory.install();

        // I will always have to put the value for current db connection
        Current.dbConnection = new MySQLConnection(Objects.requireNonNull(Configs.getMySQLConfig()));

        Parent root = FXMLLoader.load(getClass().getResource("views/home.fxml"));
        primaryStage.setTitle("PandaHost");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
