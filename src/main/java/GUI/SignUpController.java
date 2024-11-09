package GUI;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

import DatabaseControllers.DatabaseManager;
import OOModels.User;
import OOModels.Admin;
import OOModels.GeneralUser;


public class SignUpController {

    @FXML
    public TextField usernameField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public CheckBox isAdminCheck;

    @FXML
    public Button createAccountBtn;

    private DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isAdmin = isAdminCheck.isSelected();

        if (dbManager.userExists(username)) {
            showAlert("Sign Up Failed", "Username already exists. Please choose a different username.");
            return;
        }

        User user;
        if (isAdmin) {
            user = new Admin(username, password);
        } else {
            user = new GeneralUser(username, password);
        }

        dbManager.addUser(user);
        switchToLogin();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToLogin() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/GUI/login.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
