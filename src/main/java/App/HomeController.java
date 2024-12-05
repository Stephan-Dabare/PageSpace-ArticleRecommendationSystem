package App;

import Models.Article;
import Models.GeneralUser;
import DB.DatabaseHandler;

import javafx.concurrent.Task;
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

    // Get an instance of the database handler
    private final DatabaseHandler databaseHandler;

    // The current user
    private GeneralUser currentUser;

    // Constructor
    public HomeController() {
        this.databaseHandler = new DatabaseHandler();
    }

    // Set the current user
    public void setCurrentUser(GeneralUser user) {
        this.currentUser = user;
    }

    // Initialize the controller
    @FXML
    public void initialize() {
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            loadArticles();
        }
    }

    // Load articles from the database
    private void loadArticles() {
        // Load articles from the database
        Task<List<Article>> loadArticlesTask = new Task<>() {
            @Override
            protected List<Article> call() {
                return databaseHandler.getAllArticles();
            }
        };

        // Set the articles in the articles container
        loadArticlesTask.setOnSucceeded(event -> {
            try {
                List<Article> articles = loadArticlesTask.get();
                Collections.shuffle(articles);
                articlesContainer.getChildren().clear();
                for (Article article : articles) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/App/articlePostView.fxml"));
                    VBox articleBox = loader.load();

                    ArticlePostController controller = loader.getController();
                    controller.setGeneralUser(currentUser);

                    // Set the article data
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
        });

        // Handle the failed task
        loadArticlesTask.setOnFailed(event -> {
            Throwable throwable = loadArticlesTask.getException();
            throwable.printStackTrace();
        });

        // Create a new thread
        Thread articleThread = new Thread(loadArticlesTask);
        // Set the thread as a daemon thread
        articleThread.setDaemon(true);
        // Start the thread
        articleThread.start();
    }

    // Switch to the preferences view
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

    // Switch to the login view
    @FXML
    private void switchToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/loginView.fxml"));
            Parent loginRoot = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(loginRoot));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Switch to the sign up view
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

    // Switch to the account details view
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

    // Exit the application
    @FXML
    private void exitApp() {
        System.exit(0);
    }
}

