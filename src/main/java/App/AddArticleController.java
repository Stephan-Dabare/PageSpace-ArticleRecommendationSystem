package App;

import Models.AdminUser;
import Models.Article;
import Models.Category;
import Service.ContentCategorizer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class AddArticleController {
    @FXML
    public TextField titleField;
    @FXML
    public TextArea contentField;
    @FXML
    public Button submitBtn;
    @FXML
    public Button backBtn;
    @FXML
    public TextField imagePathField;
    @FXML
    public DatePicker datePublishedField;
    @FXML
    public AnchorPane mainPane;

    private AdminUser adminUser;

    // Setter for the AdminUser
    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    @FXML
    private void handleSubmit() {
        // Get input data from fields
        String title = titleField.getText();
        String content = contentField.getText();
        LocalDate datePublishedLocal = datePublishedField.getValue();
        Date datePublished = datePublishedLocal != null ? java.sql.Date.valueOf(datePublishedLocal) : null;
        BufferedImage image = loadImage(imagePathField.getText());

        // Validate inputs
        if (title.isEmpty() || content.isEmpty() || datePublished == null) {
            showAlert("Error", "Please fill in all fields!");
            return;
        }

        // Categorize content and create the article
        String categoryName = ContentCategorizer.categorizeContent(content);
        Category category = new Category(categoryName);
        Article article = new Article(title, content, category, adminUser, datePublished, image);

        // Use the AdminUser's addArticle method
        adminUser.addArticle(article);

        // Notify the user of success
        showAlert("Success", "Article successfully added!");

        //clear the fields after submission
        clearFields();
    }

    // Clear the fields
    private void clearFields() {
        titleField.clear();
        contentField.clear();
        imagePathField.clear();
        datePublishedField.setValue(null);
    }

    // Load image from file path
    private BufferedImage loadImage(String path) {
        File file = new File(path);
        if (!file.exists()) {
            System.err.println("File not found: " + path);
            return null;
        }
        try {
            return ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    private void switchToLogin() {
        // Switch to the login view
        try {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/App/loginView.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Show an alert dialog
    private void showAlert(String title, String message) {
        AlertHelper.showAlert(title, message);
    }
}
