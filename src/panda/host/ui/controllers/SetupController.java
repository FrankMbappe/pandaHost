package panda.host.ui.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import panda.host.config.Configs;
import panda.host.model.models.MySQLConfig;
import panda.host.ui.scenes.UserLoadScene;
import panda.host.utils.Panda;

import static panda.host.utils.Panda.*;

public class SetupController {
    public TextField txt_db_username;
    public TextField txt_db_password;
    public TextField txt_db_servername;
    public Button btn_validate;
    public Button btn_cancel;

    public void validate(ActionEvent actionEvent) {
        if(txtsAreNotEmpty(txt_db_username, txt_db_servername)){
            // I save the configurations in Configs.json
            MySQLConfig config = new MySQLConfig(
                    txt_db_username.getText(),
                    txt_db_password.getText(),
                    txt_db_servername.getText().replace(" ", "")
            );
            Configs.setMySQLConfig(config);

            // Then I try to init PandaHost with these configs
            try{
                Panda.init(config);
                // If the init is successful, I go to the userLoad scene
                switchScene(btn_validate.getScene(), new UserLoadScene());

            } catch (Exception e){
                System.out.println("[GUI | SetupCtrl, validate()] | Something went wrong while initializing Panda.");
                e.printStackTrace();
            }
        } else {
            System.out.println("[GUI | SetupCtrl, validate()] | Cannot validate because some required fields are empty.");
        }
    }

    public void cancel(ActionEvent actionEvent) {
        closePanda(btn_cancel);
    }
}
