package App;

import Models.AdminUser;
import Models.GeneralUser;
import Models.User;
import DB.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button signUpBtn;
    @FXML
    public CheckBox isAdminCheckBox;
    @FXML
    public Button cancelBtn;
    @FXML
    public AnchorPane mainPane;

    // Initialize the database handler
    private DatabaseHandler dbHandler = new DatabaseHandler();

    // Method to handle sign-up action
    @FXML
    private void signUp() {
        // Retrieve user input
        String username = usernameField.getText();
        String password = passwordField.getText();
        boolean isAdmin = isAdminCheckBox.isSelected();  // Check if admin is selected

        // Validate input
        if (username.isEmpty() || password.isEmpty() || User.isUsernameTaken(username)) {
            showAlert("Sign Up Failed", "Username already exists. Please choose a different username.");
            return;
        }

        // Create a new user (either admin or general user)
        User newUser;
        if (isAdmin) {
            newUser = new AdminUser(username, password); // Create Admin user with placeholder ID
        } else {
            newUser = new GeneralUser( username, password); // Create General user with placeholder ID
        }

        // Insert the new user into the database
        dbHandler.insertUser(newUser);

        // Clear input fields after successful sign-up
        usernameField.clear();
        passwordField.clear();
        isAdminCheckBox.setSelected(false);

        // Show success message
        showAlert("Success", "User has been created successfully!");
        switchToLogin();
    }

    private void showAlert(String title, String message) {
        AlertHelper.showAlert(title, message);
    }

    private void switchToLogin() {
        try {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/App/loginView.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelAction() {
        switchToLogin();
    }
}