package App;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ArticleHeadlineController {
    @FXML
    private Label titleLabel;

    public void setTitleData(String title) {
        titleLabel.setText(title);
    }
}