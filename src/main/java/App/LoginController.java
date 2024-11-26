package App;

import Models.AdminUser;
import Models.GeneralUser;
import Models.User;
import DB.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    private DatabaseHandler dbManager = new DatabaseHandler();

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
            String fxmlFile = user.isAdmin() ? "/App/adminHomeView.fxml" : "/App/homeView.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // If the user is an admin, set the Admin instance in the controller
            if (user.isAdmin()) {
                AddArticleController addArticleController = loader.getController();
                AdminUser adminUser = (AdminUser) user; // Cast to Admin for admin-specific logic
                addArticleController.setAdminUser(adminUser);
            } else {
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
        AlertHelper.showAlert(title, message);
    }

    @FXML
    private void switchToSignUp() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene signUpScene = new Scene(FXMLLoader.load(getClass().getResource("/App/signUpView.fxml")));
            stage.setScene(signUpScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
