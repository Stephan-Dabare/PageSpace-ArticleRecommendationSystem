module org.example.ood_cw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens GUI to javafx.fxml;
    exports GUI;
}