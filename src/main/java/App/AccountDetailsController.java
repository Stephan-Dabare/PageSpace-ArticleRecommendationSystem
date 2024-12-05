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

public class AccountDetailsController {
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


    // GeneralUser object to store the current user.
    private GeneralUser currentUser;
    // DatabaseHandler object to handle database operations.
    private DatabaseHandler databaseHandler;

    public AccountDetailsController() {
        this.databaseHandler = new DatabaseHandler();
    }

    // Initializer
    @FXML
    public void initialize() {
        // Set the username label and read headlines.
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            readHeadlines();
        } else {
        }
    }

    // Setter method to set the current user.
    public void setCurrentUser(GeneralUser user) {
        // Set the current user
        this.currentUser = user;
        // Set the username label and read headlines.
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            readHeadlines();
        } else {
            // If no user is provided, print a message.
            System.out.println("No user provided");
        }
    }

    // Method to read headlines.
    private void readHeadlines() {
        // This task will load the read articles of the current user.
        Task<List<Article>> loadReadTask = new Task<>() {
            @Override
            protected List<Article> call() {
                return currentUser.loadReadArticles();
            }
        };

        // Set the succeeded and failed handlers.
        loadReadTask.setOnSucceeded(event -> {
            try {
                // Get the read articles.
                List<Article> readArticles = loadReadTask.get();
                if (readArticles.isEmpty()) {
                    // If no read articles are found, show an alert.
                    AlertHelper.showAlert("No reading history has been found.", "You need to mark articles as read to get read history.");
                }
                // Populate the history.
                populateHistory(readArticles);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Set the failed handler.
        loadReadTask.setOnFailed(event -> {
            Throwable throwable = loadReadTask.getException();
            throwable.printStackTrace();
        });

        // Create a new thread for the task.
        Thread readThread = new Thread(loadReadTask);
        // Set the thread as a daemon thread.
        readThread.setDaemon(true);
        // Start the thread.
        readThread.start();
    }

    // Method to populate the history.
    private void populateHistory(List<Article> articles) {
        try {
            Collections.shuffle(articles);

            // Clear the articles container.
            for (Article article : articles) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/articleHeadlineView.fxml"));
                VBox articleBox = loader.load();

                ArticleHeadlineController controller = loader.getController();
                controller.setTitleData(article.getTitle());

                articlesContainer.getChildren().add(articleBox);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToHome() {
        // Switch to home view.
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
    private void switchToPreferences(ActionEvent event) {
        // Switch to user preference view.
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
        // Switch to login view.
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
        // Switch to sign up view.
        try {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            Scene loginScene = new Scene(FXMLLoader.load(getClass().getResource("/App/signUpView.fxml")));
            stage.setScene(loginScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitApp(){
        // Exit the application.
        System.exit(0);
    }
}
