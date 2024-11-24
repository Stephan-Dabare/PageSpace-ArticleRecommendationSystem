module org.example.ood_cw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens App to javafx.fxml;
    exports App;
}