package GUI;

import OOModels.Article;
import OOModels.GeneralUser;
import DatabaseControllers.DatabaseManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.util.List;

public class HomeController {

    @FXML
    private VBox articlesContainer;

    private DatabaseManager databaseManager;
    private GeneralUser currentUser; // Add this field

    public HomeController() {
        this.databaseManager = new DatabaseManager();
    }

    // Constructor with DatabaseManager parameter
    public HomeController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    // Setter for currentUser
    public void setCurrentUser(GeneralUser user) {
        this.currentUser = user;
    }

    public void initialize() {
        if (currentUser != null) {
            loadArticles();  // Load articles only after currentUser is set
        }
    }

    private void loadArticles() {
        try {
            List<Article> articles = databaseManager.getAllArticles();

            for (Article article : articles) {
                // Load the VBox from articlePost.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/articlePost.fxml"));
                VBox articleBox = loader.load();

                // Get the controller for this post and set data
                ArticlePostController controller = loader.getController();
                controller.setGeneralUser(currentUser); // Pass the currentUser to ArticlePostController

                // Pass the article data to the controller
                controller.setArticleData(
                        article.getTitle(),
                        article.getCategory(),
                        article.getDatePublished().toString(),
                        article.getSource(),
                        article.getContent(),
                        databaseManager.bufferedImageToBytes(article.getImage())
                );

                // Add the populated VBox to the articles container
                articlesContainer.getChildren().add(articleBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
