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


    private GeneralUser currentUser;
    private DatabaseHandler databaseHandler;

    public AccountDetailsController() {
        this.databaseHandler = new DatabaseHandler();
    }

    @FXML
    public void initialize() {
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            readHeadlines();
        } else {
        }
    }

    public void setCurrentUser(GeneralUser user) {
        this.currentUser = user;
        if (currentUser != null) {
            usernameLabel.setText(currentUser.getUsername());
            readHeadlines();
        } else {
            System.out.println("No user provided");
        }
    }

    private void readHeadlines() {
        try {
            List<Article> readArticles = currentUser.loadReadArticles();
            if (readArticles.isEmpty()) {
                AlertHelper.showAlert("No reading history has been found.", "You need to mark articles as read to get read history.");
            }
            populateArticles(readArticles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateArticles(List<Article> articles) {
        try {
            Collections.shuffle(articles);

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
    private void exitApp(){
        System.exit(0);
    }
}
