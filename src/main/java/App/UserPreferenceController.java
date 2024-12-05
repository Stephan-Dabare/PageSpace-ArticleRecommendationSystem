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

    // Current user
    private GeneralUser currentUser;
    // Database handler
    private DatabaseHandler databaseHandler;

    // Constructor
    public UserPreferenceController() {
        this.databaseHandler = new DatabaseHandler();
    }

    // Initialize method
    @FXML
    public void initialize() {
    }

    //  Set current user
    public void setCurrentUser(GeneralUser user) {
        this.currentUser = user;
        if (currentUser != null) {
            // Set the username label
            usernameLabel.setText(currentUser.getUsername());
            loadUserPreferredArticles();
        } else {
            System.out.println("No user provided");
        }
    }

    // Load user preferred articles
    private void loadUserPreferredArticles() {
        // Load preferred articles
        Task<List<Article>> loadPreferredTask = new Task<>() {
            @Override
            protected List<Article> call() {
                return currentUser.loadPreferredArticles();
            }
        };

        // Set on succeeded and on failed event handlers
        loadPreferredTask.setOnSucceeded(event -> {
            try {
                // Get preferred articles
                List<Article> preferredArticles = loadPreferredTask.get();
                if (preferredArticles.isEmpty()) {
                    // Show alert if no articles found.
                    AlertHelper.showAlert("No Articles Found", "No articles found based on your preferences. You need to like articles to get recommendations.");
                }
                populateArticles(preferredArticles);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // On failed event handler
        loadPreferredTask.setOnFailed(event -> {
            Throwable throwable = loadPreferredTask.getException();
            throwable.printStackTrace();
        });

        // Create a new thread
        Thread preferenceThread = new Thread(loadPreferredTask);
        // Set the thread as a daemon thread
        preferenceThread.setDaemon(true);
        // Start the thread
        preferenceThread.start();
    }

    // Populate articles
    private void populateArticles(List<Article> articles) {
        try {
            // Shuffle the articles
            Collections.shuffle(articles);

            // Iterate through the articles and populate the articles container
            for (Article article : articles) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/articlePostView.fxml"));
                VBox articleBox = loader.load();

                ArticlePostController controller = loader.getController();
                controller.setGeneralUser(currentUser);

                // Set article data
                controller.setArticleData(
                        article.getTitle(),
                        article.getCategory().toString(),
                        article.getCreatedBy().getUsername(),
                        article.getDatePublished().toString(),
                        article.getContent(),
                        databaseHandler.bufferedImageToBytes(article.getImage())
                );

                // Add the article box to the articles container
                articlesContainer.getChildren().add(articleBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Event handlers
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

    // Switch to login view
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

    // Switch to sign up view
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

    // Switch to account details view
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

    // exit app
    @FXML
    private void exitApp(){
        System.exit(0);
    }
}
