package App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AlertController {
    @FXML
    public Label titleLabel;
    @FXML
    public Label messageLabel;
    @FXML
    public Button closeButton;

    @FXML
    private void handleClose() {
        closeButton.getScene().getWindow().hide();
    }

    // Setters
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
