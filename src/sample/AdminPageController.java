package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminPageController implements Initializable {
    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Employee, String> emailCol;

    @FXML
    private TableColumn<Employee, String> passwordCol;

    @FXML
    private TableColumn<Employee, String> startDateCol;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_password;

    @FXML
    private TextField txt_start_date;

    @FXML
    private TextField txt_username;

    @FXML
    private Button updateBtn;

    @FXML
    private TableColumn<Employee, Integer> userIDCol;

    @FXML
    private TableView<Employee> userTable;

    @FXML
    private TableColumn<Employee, String> usernameCol;

    private ObservableList<Employee> list;
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @FXML
    void addUsers(ActionEvent event) {
        if(txt_id.getText().isBlank() || txt_username.getText().isBlank() || txt_password.getText().isBlank()
                || txt_email.getText().isBlank() || txt_start_date.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Please fill in all text fields.");
        }
        else {
            try{
                connection = Utilities.returnConnection();
                preparedStatement = connection.prepareStatement("INSERT INTO employee VALUES(?,?,?,?,?)");
                preparedStatement.setString(1, txt_id.getText());
                preparedStatement.setString(2, txt_username.getText());
                preparedStatement.setString(3, txt_password.getText());
                preparedStatement.setString(4, txt_email.getText());
                preparedStatement.setString(5, txt_start_date.getText());
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Users added");
                updateTable();
            } catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    @FXML
    void delete(ActionEvent event) {
        if(txt_id.getText().isBlank()) {
            JOptionPane.showMessageDialog(null, "Please enter a user ID to delete an employee");
        }
        else {
            try{
                Connection connection = Utilities.returnConnection();
                String value1 = txt_id.getText();
                String sql = "DELETE FROM employee WHERE emp_id = '" + value1 + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Delete Successful");
                updateTable();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void edit(ActionEvent event) {
        try {
            Connection connection = Utilities.returnConnection();
            String value1 = txt_id.getText();
            String value2 = txt_username.getText();
            String value3 = txt_password.getText();
            String value4 = txt_email.getText();
            String value5 = txt_start_date.getText();

            if(value1.isBlank() || value2.isBlank() || value3.isBlank() || value4.isBlank() || value5.isBlank()) {
                JOptionPane.showMessageDialog(null, "Please fill in all text fields");
            }

            else {
                String sql = "UPDATE employee SET emp_id = '" + value1 + "', user_name = '" + value2 + "', password = '" + value3 + "'," +
                        "email = '" + value4 + "', start_date = '" + value5 + "' WHERE emp_id = '" + value1 + "'";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.execute();
                JOptionPane.showMessageDialog(null, "Update successful");
                updateTable();
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
        }
    }

    @FXML
    void selectUser(MouseEvent event) {
        int index = -1;
        index = userTable.getSelectionModel().getSelectedIndex();
        if(index <= -1) return;

        txt_id.setText(userIDCol.getCellData(index).toString());
        txt_username.setText(usernameCol.getCellData(index));
        txt_password.setText(passwordCol.getCellData(index));
        txt_email.setText(emailCol.getCellData(index));
        txt_start_date.setText(startDateCol.getCellData(index));
    }

    public void updateTable() {
        userIDCol.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("emp_id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("user_name"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("password"));
        emailCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
        startDateCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("start_date"));

        list = Utilities.getEmployeeInformation();
        userTable.setItems(list);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTable();
    }
}
