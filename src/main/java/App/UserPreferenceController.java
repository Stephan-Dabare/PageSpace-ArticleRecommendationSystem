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

public class UserPreferenceController {
    @FXML
    public Button homeBtn;
    @FXML
    public Button loginBtn;
    @FXML
    public Button signUpBtn;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    public Label usernameLabel;
    @FXML
    public Button preferenceBtn;
    @FXML
    public Button exitBtn;
    @FXML
    public AnchorPane mainPane;
    @FXML
    public Button accountBtn;
    @FXML
    private VBox articlesContainer;


    private GeneralUser currentUser;
    private DatabaseHandler databaseHandler;

    public UserPreferenceController() {
        this.databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
    }

    public void setCurrentUser(GeneralUser user) {
        this.currentUser = user;
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            loadUserPreferredArticles();
        } else {
            System.out.println("No user provided");
        }
    }

    private void loadUserPreferredArticles() {
        try {
            List<Article> preferredArticles = currentUser.loadPreferredArticles();
            if (preferredArticles.isEmpty()) {
                AlertHelper.showAlert("No Articles Found", "No articles found based on your preferences. You need to like articles to get recommendations.");
            }
            populateArticles(preferredArticles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void populateArticles(List<Article> articles) {
        try {
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
    private void backToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/homeView.fxml"));
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
    private void exitApp(){
        System.exit(0);
    }
}
