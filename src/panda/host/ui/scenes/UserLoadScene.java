package panda.host.ui.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class UserLoadScene implements PandaScene {
    Scene userLoad;
    
    public UserLoadScene() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../views/userLoad.fxml"));
        this.userLoad = new Scene(root);
    }

    @Override
    public Scene get() {
        return userLoad;
    }
}
