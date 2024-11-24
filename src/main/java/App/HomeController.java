package App;

import Models.Article;
import Models.GeneralUser;
import DB.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HomeController {

    @FXML
    public Button preferenceBtn;

    @FXML
    private VBox articlesContainer;

    private final DatabaseHandler databaseHandler;

    private GeneralUser currentUser;

    public HomeController() {
        this.databaseHandler = new DatabaseHandler();
    }

    public void setCurrentUser(GeneralUser user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            loadArticles();
        }
    }

    private void loadArticles() {
        try {
            List<Article> articles = databaseHandler.getAllArticles();
            Collections.shuffle(articles);

            for (Article article : articles) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/articlePost.fxml"));
                VBox articleBox = loader.load();

                ArticlePostController controller = loader.getController();
                controller.setGeneralUser(currentUser);

                controller.setArticleData(
                        article.getTitle(),
                        article.getCategory().toString(),
                        article.getCreatedBy().getUsername(),
                        article.getDatePublished().toString(),
                        article.getContent(),
                        databaseHandler.bufferedImageToBytes(article.getImage())
                );

                articlesContainer.getChildren().add(articleBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToPreferences(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/userPreference.fxml"));
            Parent preferenceRoot = loader.load();

            UserPreferenceController controller = loader.getController();
            controller.setGeneralUser(currentUser); // Pass the current user

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene preferenceScene = new Scene(preferenceRoot);
            currentStage.setScene(preferenceScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

