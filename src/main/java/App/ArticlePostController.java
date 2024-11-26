package App;

import Models.GeneralUser;
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


    public void setArticleData(String title, String category,String author, String date, String content, byte[] imageBytes) {
        titleLabel.setText(title);
        categoryLabel.setText(category);
        authorLabel.setText(author);
        dateLabel.setText(date);
        contentLabel.setText(content);

        if (imageBytes != null) {
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            imageView.setImage(image);
        }

        if (isArticleRead(title)) {
            readBtn.getStyleClass().add("button5-pressed");
            readBtn.setText("✔");
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
        // Assuming you have the article category available in this controller
        String category = categoryLabel.getText();  // Get category from the label or elsewhere
        if (currentUser != null) {
            currentUser.likeCategory(category);
            likeButton.setText("♥");
        }
    }

    private void handleReadButtonClick() {
        String title = titleLabel.getText();
        if (currentUser != null) {
            currentUser.readArticle(title);
            readBtn.getStyleClass().add("button5-pressed");
            readBtn.setText("✔");
        }
    }

    private boolean isArticleRead(String title) {
        return currentUser != null && currentUser.hasReadArticle(title);
    }


}