package GUI;

import Algorithmns.ContentCategorizer;
import DatabaseControllers.DatabaseManager;
import OOModels.Article;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class AddArticleController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentField;
    @FXML
    private DatePicker datePublishedField;
    @FXML
    private TextField sourceField;
    @FXML
    private TextField imagePathField;
    @FXML
    private Button submitBtn;
    @FXML
    private Button backBtn;

    @FXML
    private void handleSubmit() {
        String title = titleField.getText();
        String content = contentField.getText();
        LocalDate datePublishedLocal = datePublishedField.getValue();
        Date datePublished = datePublishedLocal != null ? java.sql.Date.valueOf(datePublishedLocal) : null;
        String source = sourceField.getText();
        BufferedImage image = loadImage(imagePathField.getText());

        String category = ContentCategorizer.categorizeContent(content);

        Article article = new Article(0, title, content, category, datePublished, source, image);
        DatabaseManager.addArticle(article);

        showAlert("Article Added", "The article has been successfully added.");
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
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

    @FXML
    private void switchToLogin() {
        try {
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/GUI/login.fxml")));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
