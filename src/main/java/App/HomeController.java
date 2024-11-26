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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class HomeController {

    @FXML
    public Button preferenceBtn;
    @FXML
    public Button signUpBtn;
    @FXML
    public Button loginBtn;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Label usernameLabel;
    @FXML
    public Button exitBtn;
    @FXML
    public AnchorPane mainPane;
    @FXML
    public Button accountBtn;

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
            usernameLabel.setText(currentUser.getUsername());
            loadArticles();
        }
    }

    private void loadArticles() {
        try {
            List<Article> articles = databaseHandler.getAllArticles();
            Collections.shuffle(articles);

            for (Article article : articles) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/articlePostView.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/userPreferenceView.fxml"));
            Parent preferenceRoot = loader.load();

            UserPreferenceController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Pass the current user

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene preferenceScene = new Scene(preferenceRoot);
            currentStage.setScene(preferenceScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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
    private void switchToSignUp() {
        try {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/App/signUpView.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToAccountDetails(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/accountDetailsView.fxml"));
            Parent accountDetailsRoot = loader.load();

            AccountDetailsController controller = loader.getController();
            controller.setCurrentUser(currentUser); // Pass the current user

            Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene accountDetailsScene = new Scene(accountDetailsRoot);
            currentStage.setScene(accountDetailsScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitApp() {
        System.exit(0);
    }
}

