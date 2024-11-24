package GUI;

import DatabaseControllers.DatabaseManager;
import OOModels.Article;
import OOModels.GeneralUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class UserPreferenceController {
    @FXML
    public Button backToHomeBtn;
    @FXML
    private VBox articlesContainer;

    private GeneralUser currentUser;
    private final DatabaseManager databaseManager;

    public UserPreferenceController() {
        this.databaseManager = new DatabaseManager();
    }

    @FXML
    public void initialize() {
    }

    public void setGeneralUser(GeneralUser user) {
        this.currentUser = user;
        if (currentUser != null) {
            loadUserPreferredArticles();
        } else {
            System.out.println("No user provided");
        }
    }

    private void loadUserPreferredArticles() {
        try {
            List<Article> preferredArticles = currentUser.loadPreferredArticles();
            if (preferredArticles.isEmpty()) {
                System.out.println("No preferred articles found");
            }
            populateArticles(preferredArticles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateArticles(List<Article> articles) {
        articlesContainer.getChildren().clear();
        for (Article article : articles) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/articlePost.fxml"));
                VBox articleBox = loader.load();

                ArticlePostController controller = loader.getController();
                controller.setGeneralUser(currentUser);
                controller.setArticleData(
                        article.getTitle(),
                        article.getCategory(),
                        article.getDatePublished().toString(),
                        article.getSource(),
                        article.getContent(),
                        databaseManager.bufferedImageToBytes(article.getImage())
                );

                articlesContainer.getChildren().add(articleBox);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void backToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/home.fxml"));
            Parent homeRoot = loader.load();

            HomeController homeController = loader.getController();
            homeController.setCurrentUser(currentUser);  // Pass current user to HomeController
            homeController.initialize();

            Stage stage = (Stage) articlesContainer.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
