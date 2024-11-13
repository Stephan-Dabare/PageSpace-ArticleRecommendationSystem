// ArticlePostController.java
package GUI;

import OOModels.GeneralUser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import java.io.ByteArrayInputStream;

public class ArticlePostController {

    @FXML
    private ImageView imageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label sourceLabel;

    @FXML
    private Label contentLabel;

    @FXML
    private HBox likeDislikeBox;

    @FXML
    private Button likeButton;


    public void setArticleData(String title, String category, String date, String source, String content, byte[] imageBytes) {
        titleLabel.setText(title);
        categoryLabel.setText(category);
        dateLabel.setText(date);
        sourceLabel.setText(source);
        contentLabel.setText(content);

        if (imageBytes != null) {
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            imageView.setImage(image);
        }
    }


    private GeneralUser currentUser;  // Pass the current user into this controller

    @FXML
    public void initialize() {
        likeButton.setOnAction(event -> handleLikeButtonClick());
    }

    private void handleLikeButtonClick() {
        // Assuming you have the article category available in this controller
        String category = categoryLabel.getText();  // Get category from the label or elsewhere
        if (currentUser != null) {
            currentUser.likeCategory(category);
            likeButton.setText("♥");
        }
    }

    public void setGeneralUser(GeneralUser generalUser) {
        this.currentUser = generalUser;
    }
}