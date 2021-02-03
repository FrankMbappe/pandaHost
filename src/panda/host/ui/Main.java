package panda.host.ui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import panda.host.config.Configs;
import panda.host.config.database.MySQLConnection;
import panda.host.ui.scenes.HomeScene;
import panda.host.ui.scenes.SetupScene;
import panda.host.utils.Current;
import panda.host.utils.Panda;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        // If the file has a valid connection props, the app loads directly the homepage
        if(Configs.fileIsValid()){
            Current.dbConnection = new MySQLConnection(Objects.requireNonNull(Configs.getMySQLConfig()));
            primaryStage.setScene(new HomeScene().get());

        } else {
            // Otherwise it sets up MySQL
            primaryStage.setScene(new SetupScene().get());
        }
        primaryStage.getIcons().add(new Image(Panda.PATH_TO_APP_ICON));
        primaryStage.setTitle(Panda.APP_NAME);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() {
        Panda.exit();
    }
}
