package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class FirstTimeLoginController implements Initializable {

    @FXML
    private Button loginBtn;

    @FXML
    private TextField password;

    @FXML
    private TextField userID;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                ResultSet resultSet = null;
                Utilities.createTable();
                Utilities.createAdminUser(userID.getText(), password.getText());
                Utilities.changeScene(actionEvent, "sample.fxml", "Employee Table", userID.getText());
                /*if(resultSet != null) {
                    try {
                        System.out.println(resultSet.getString("user_name") + " " + resultSet.getString("password") +
                                " " + resultSet.getString("emp_id"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });
    }
}
