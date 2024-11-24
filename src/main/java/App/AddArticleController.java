package App;

import Models.AdminUser;
import Models.Article;
import Models.Category;
import Service.ContentCategorizer;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    private AdminUser adminUser;

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

        // Optionally, clear the fields after submission
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        contentField.clear();
        imagePathField.clear();
        datePublishedField.setValue(null);
    }



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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
