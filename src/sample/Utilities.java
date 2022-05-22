package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;

public class Utilities {
    private static Connection connection = null;
    //private static Statement statement = null;
    private static ResultSet resultSet = null;
    private static PreparedStatement preparedStatement = null;
    private static final String SETUP = "SetupSoftware.fxml";
    private static final String NORMAL = "LoginPage.fxml";

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;

        if(username != null) {
            try {
                FXMLLoader loader = new FXMLLoader(Utilities.class.getResource(fxmlFile));
                root = loader.load();
                AdminPageController adminPageController = loader.getController();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try{
                root = FXMLLoader.load(Utilities.class.getResource(fxmlFile));
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 700, 550));
        stage.setResizable(false);
        stage.show();
    }

    /*public static ResultSet displayUsers() {
        if(connection == null) {
            getConnection();
        }
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT user_name, password, emp_id FROM employee");

        } catch(SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }*/

    public String getLoginPage() {
        String returnString = "";
        String catalog = "";
        if (connection == null) {
            getConnection();
        }

        if(!exists()) {
            returnString = SETUP;
        }
        else {
            returnString = NORMAL;
        }

        return returnString;
    }

    private boolean exists() {
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='employee'");
            if(!resultSet.next()) {
                return false;
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static void getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:ProductSQLite.db");
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection returnConnection() {
        return connection;
    }

    public static void createTable() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS employee(emp_id INT NOT NULL, " +
                    "user_name VARCHAR(40), password VARCHAR(40), email VARCHAR(40), start_date DATE," +
                    " PRIMARY KEY(emp_id));");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(statement != null) {
                    statement.close();
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void login(ActionEvent event, String username, String password) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String retrievedPassword = null;
        if(username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all text fields.");
        }
        try{
            preparedStatement = connection.prepareStatement("SELECT password FROM employee WHERE user_name = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username and or password is incorrect");
                alert.show();
            }
            else {
                retrievedPassword = resultSet.getString("password");
                if(retrievedPassword.equals(password)) {
                    changeScene(event, "sample.fxml", "Employee Page", username);
                }
                else {
                    Alert alert =  new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Username and or password is incorrect");
                    alert.show();
                }
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createAdminUser(String username, String password) {

        PreparedStatement preparedStatement = null;

        if(username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all text fields.");
        }

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO employee(emp_id, user_name, password) VALUES(?, ?, ?);");
            preparedStatement.setString(1, "1");
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            preparedStatement.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /*private void initialize() {
        if(!hasData) {
            hasData = true;
        }
        try {
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("SELECT name FROM sqlite_master WHERE type = 'table' " +
                    "AND name = 'user'");
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()) {
                System.out.println("Building user table with prepopulated values");
                Statement initialStatement = connection.createStatement();
                initialStatement.executeUpdate("CREATE TABLE IF NOT EXISTS user(id INT, " +
                        "f_name VARCHAR(40), l_name VARCHAR(40), PRIMARY KEY(id));");

                PreparedStatement preparedValues1 = connection.prepareStatement("INSERT INTO user values(?, ?, ?);");
                preparedValues1.setString(2, "John");
                preparedValues1.setString(3, "McNeil");
                preparedValues1.execute();

                PreparedStatement preparedValues2 = connection.prepareStatement("INSERT INTO user values(?, ?, ?);");
                preparedValues2.setString(2, "Paul");
                preparedValues2.setString(3, "Smith");
                preparedValues2.execute();
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }*/

    private void addUser(String firstName, String lastName) {
        if(connection == null) {
            getConnection();
        }
        try {
            PreparedStatement prep = connection.prepareStatement("INSERT INTO user VALUES(?, ?, ?);");
            prep.setString(2, firstName);
            prep.setString(3, lastName);
            prep.execute();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<Employee> getEmployeeInformation() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ObservableList<Employee> list = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM employee");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                list.add(new Employee(resultSet.getInt("emp_id"), resultSet.getString("user_name"),
                        resultSet.getString("password"), resultSet.getString("email"), resultSet.getString("start_date")));
            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}
