package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import OOModels.User;
import DatabaseControllers.DatabaseManager;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public Button loginBtn;

    @FXML
    public Button signUpBtn;

    private DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = dbManager.authenticateUser(username, password);
        if (user != null) {
            showAlert("Login Successful", "Welcome, " + user.getUsername() + "!");
            loadMainScene(user.isAdmin());
        } else {
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    private void loadMainScene(boolean isAdmin) {
        try {
            String fxmlFile = isAdmin ? "/GUI/adminHome.fxml" : "/GUI/home.fxml";
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource(fxmlFile)));
            stage.setScene(mainScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToSignUp() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/GUI/signUp.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
