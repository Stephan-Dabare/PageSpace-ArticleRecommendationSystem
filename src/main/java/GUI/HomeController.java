package GUI;

import DatabaseControllers.DatabaseManager;
import OOModels.Article;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.util.List;

public class HomeController {

    @FXML
    private VBox articlesContainer;

    private DatabaseManager databaseManager;

    public HomeController() {
        this.databaseManager = new DatabaseManager();
    }

    // Constructor with DatabaseManager parameter
    public HomeController(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void initialize() {
        loadArticles();
    }

    private void loadArticles() {
        try {
            List<Article> articles = databaseManager.getAllArticles();

            for (Article article : articles) {
                // Create a new VBox from the articlePost.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/articlePost.fxml"));
                VBox articleBox = loader.load();

                // Get the controller for this post and set data
                ArticlePostController controller = loader.getController();
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