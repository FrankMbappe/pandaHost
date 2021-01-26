package panda.host.ui.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class SetupScene implements PandaScene {
    Scene setupScene;

    public SetupScene() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../views/setup.fxml"));
        this.setupScene = new Scene(root);
    }

    @Override
    public Scene get() {
        return setupScene;
    }
}
