package panda.host.ui.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import panda.host.model.data.UserData;
import panda.host.model.models.User;
import panda.host.ui.scenes.HomeScene;
import panda.host.utils.Panda;
import panda.host.utils.UserXLSXHelper;

import java.io.File;
import java.util.ArrayList;

import static panda.host.utils.Panda.*;

public class UserLoadController {
    ArrayList<User> loadedUsers = null;

    public Button btn_validate;
    public Button btn_skip;
    public Button btn_browse;
    public Label lb_fileAnalysis;
    public Label lb_fileUri;

    public void initialize(){
        btn_validate.setDisable(true);
    }

    public void validate(ActionEvent actionEvent) {
        new UserData().addAll(loadedUsers);

        // Reusability of the skip() method, since its content should be here
        skip(actionEvent);
    }

    public void skip(ActionEvent actionEvent) {
        try {
            switchScene(btn_skip.getScene(), new HomeScene());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void browseFiles(ActionEvent actionEvent) {
        // I get the file from the FileDialog
        File file = openFileDialog(btn_browse, Panda.genFileFilter("EXCEL FILES", "xlsx"));

        if(file != null){
            // I display the absolute path of the selected file
            lb_fileUri.setText(file.getAbsolutePath());

            // Then I extract the users from that file
            loadedUsers = UserXLSXHelper.getUserList(file.getAbsolutePath(), 2);

            if(loadedUsers != null){
                lb_fileAnalysis.getStyleClass().clear();
                lb_fileAnalysis.getStyleClass().addAll("label-file-description", "text-ok");
                lb_fileAnalysis.setText("Valid file, " + loadedUsers.size() + " users extracted.");
                btn_validate.setDisable(false);

            } else {
                lb_fileAnalysis.getStyleClass().clear();
                lb_fileAnalysis.getStyleClass().addAll("label-file-description", "text-error");
                lb_fileAnalysis.setText("Error, the file uploaded is not valid.");
                btn_validate.setDisable(true);
            }

        } else {
            lb_fileAnalysis.getStyleClass().clear();
            lb_fileAnalysis.getStyleClass().addAll("label-file-description", "text-error");
            lb_fileAnalysis.setText("Error, the file uploaded doesn't exist.");
            btn_validate.setDisable(true);
        }
    }
}
