module com.example.healthswift1 {
    requires javafx.controls;
    requires javafx.fxml;
    exports test;
    requires java.sql; // Inclure le module java.sql
    opens controller to javafx.fxml;
    opens com.example.healthswift1 to javafx.fxml;
    exports com.example.healthswift1;
    opens entite to javafx.base, javafx.fxml;
    exports controller to javafx.fxml;

}