package App;

import Models.GeneralUser;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;

public class ArticlePostController {
    @FXML
    public Label authorLabel;
    @FXML
    public Button readBtn;
    @FXML
    private ImageView imageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private Button likeButton;

    // Pass the current user into this controller
    private GeneralUser currentUser;
    public void setGeneralUser(GeneralUser generalUser) {
        this.currentUser = generalUser;
    }

    // Initialize the controller
    @FXML
    public void initialize() {
        likeButton.setOnAction(event -> handleLikeButtonClick());
        readBtn.setOnAction(event -> handleReadButtonClick());

        // Check if the article is read and update the button state
        if (isArticleRead(titleLabel.getText())) {
            readBtn.getStyleClass().add("button5-pressed");
            readBtn.setText("✔");
        }
    }

    // Set the article data to the view
    public void setArticleData(String title, String category, String author, String date, String content, byte[] imageBytes) {
        titleLabel.setText(title);
        categoryLabel.setText(category);
        authorLabel.setText(author);
        dateLabel.setText(date);
        contentLabel.setText(content);

        // Load the image
        if (imageBytes != null) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    return new Image(new ByteArrayInputStream(imageBytes));
                }
            };

            loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));
            // Start the task in a new thread
            new Thread(loadImageTask).start();
        }
    }

    // Handle the like button click
    private void handleLikeButtonClick() {
        String category = categoryLabel.getText();
        if (currentUser != null) {
            Models.Category categoryObj = new Models.Category(category);
            Runnable likeTask = () -> currentUser.likeCategory(categoryObj);
            Thread likeThread = new Thread(likeTask);
            likeButton.setText("♥");
            likeThread.setDaemon(true);
            likeThread.start();
        }
    }

    // Handle the read button click
    private void handleReadButtonClick() {
        String title = titleLabel.getText();
        if (currentUser != null) {
            Runnable readTask = () -> currentUser.readArticle(title);
            Thread readThread = new Thread(readTask);
            readBtn.setText("✔");
            readThread.setDaemon(true);
            readThread.start();
        }
    }

    // Check if the article is read
    private boolean isArticleRead(String title) {
        return currentUser != null && currentUser.hasReadArticle(title);
    }
}