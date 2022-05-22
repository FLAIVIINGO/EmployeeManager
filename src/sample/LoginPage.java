package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginPage implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField password;

    @FXML
    private TextField userName;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                Utilities.login(actionEvent, userName.getText(), password.getText());
                //Utilities.changeScene(actionEvent, "sample.fxml", "Employee Table", userName.getText());
            }
        });
    }
}
