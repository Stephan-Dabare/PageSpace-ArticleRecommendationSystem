package App;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class AlertHelper {
    // Show an alert with the given title and message
    public static void showAlert(String title, String message) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Set the alert as a modal window
            stage.initStyle(StageStyle.UNDECORATED); // Hide the default title bar
            FXMLLoader loader = new FXMLLoader(AlertHelper.class.getResource("/App/alertView.fxml"));
            Parent root = loader.load();
            AlertController alertController = loader.getController();
            root.setStyle("-fx-background-color: transparent;");
            alertController.setTitle(title);
            alertController.setMessage(message);

            // Enable dragging of the window
            final Delta dragDelta = new Delta();
            root.setOnMousePressed(event -> {
                dragDelta.x = stage.getX() - event.getScreenX();
                dragDelta.y = stage.getY() - event.getScreenY();
            });
            root.setOnMouseDragged(event -> {
                stage.setX(event.getScreenX() + dragDelta.x);
                stage.setY(event.getScreenY() + dragDelta.y);
            });

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper class for drag coordinates
    private static class Delta {
        double x, y;
    }
}
