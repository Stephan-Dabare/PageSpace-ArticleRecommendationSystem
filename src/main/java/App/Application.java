package App;

import DB.DatabaseHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Initialize the database handler to create tables
        new DatabaseHandler();

        // Load the FXML file for the login scene
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("loginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 700);

        // Set the stage properties
        stage.setTitle("PageSpace");
        stage.setScene(scene);

        // Disable resizing but keep the default minimize and close buttons
        stage.setResizable(false); // Disables window resizing, but keeps the title bar

        // Show the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
