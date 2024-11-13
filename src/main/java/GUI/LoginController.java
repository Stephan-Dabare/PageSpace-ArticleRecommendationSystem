package GUI;

import OOModels.Admin;
import OOModels.GeneralUser;
import OOModels.User;
import DatabaseControllers.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;

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
            loadMainScene(user);
        } else {
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    private void loadMainScene(User user) {
        try {
            String fxmlFile = user.isAdmin() ? "/GUI/adminHome.fxml" : "/GUI/home.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // If the user is an admin, set the Admin instance in the controller
            if (user.isAdmin()) {
                AddArticleController addArticleController = loader.getController();
                Admin adminUser = (Admin) user; // Cast to Admin for admin-specific logic
                addArticleController.setAdminUser(adminUser);
            } else {
                // For GeneralUser, set the current user in the HomeController
                HomeController homeController = loader.getController();
                GeneralUser generalUser = (GeneralUser) user; // Cast to GeneralUser
                homeController.setCurrentUser(generalUser);  // Pass current user to HomeController
                homeController.initialize();  // Initialize the home page with the user's articles
            }

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));

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
            Scene signUpScene = new Scene(FXMLLoader.load(getClass().getResource("/GUI/signUp.fxml")));
            stage.setScene(signUpScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
