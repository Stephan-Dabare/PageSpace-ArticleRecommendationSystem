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


    public void setArticleData(String title, String category, String author, String date, String content, byte[] imageBytes) {
        titleLabel.setText(title);
        categoryLabel.setText(category);
        authorLabel.setText(author);
        dateLabel.setText(date);
        contentLabel.setText(content);

        if (imageBytes != null) {
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    return new Image(new ByteArrayInputStream(imageBytes));
                }
            };

            loadImageTask.setOnSucceeded(event -> imageView.setImage(loadImageTask.getValue()));
            new Thread(loadImageTask).start();
        }
    }

    private GeneralUser currentUser;  // Pass the current user into this controller
    public void setGeneralUser(GeneralUser generalUser) {
        this.currentUser = generalUser;
    }

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

    private void handleLikeButtonClick() {
        String category = categoryLabel.getText();
        if (currentUser != null) {
            Runnable likeTask = () -> currentUser.likeCategory(category);
            Thread likeThread = new Thread(likeTask);
            likeButton.setText("♥");
            likeThread.setDaemon(true);
            likeThread.start();
        }
    }

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

    private boolean isArticleRead(String title) {
        return currentUser != null && currentUser.hasReadArticle(title);
    }


}