package App;

import Models.AdminUser;
import Models.GeneralUser;
import Models.User;
import DB.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
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
            showAlert("Sign Up Failed", "Username already exists. Please choose a different username.", AlertType.ERROR);
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
        showAlert("Success", "User has been created successfully!", AlertType.INFORMATION);
        switchToLogin();
    }

    // Utility method to show alerts
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToLogin() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/App/login.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}