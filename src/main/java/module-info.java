module com.example.noubez {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.noubez to javafx.fxml;

    exports com.example.noubez.util;
    opens com.example.noubez.util to javafx.fxml;

    exports com.example.noubez;
}