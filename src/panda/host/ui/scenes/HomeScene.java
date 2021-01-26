package panda.host.ui.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HomeScene implements PandaScene {
    Scene homeScene;

    public HomeScene() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../views/home.fxml"));
        this.homeScene = new Scene(root);
    }

    @Override
    public Scene get() {
        return homeScene;
    }
}
