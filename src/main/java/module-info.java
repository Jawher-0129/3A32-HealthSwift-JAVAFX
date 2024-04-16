module com.example.gestionevenement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    exports Test to javafx.graphics;
    exports Controller;
    opens Controller;
    opens entite to javafx.base;
    requires java.desktop;
    requires javafx.graphics;

    opens com.example.gestionevenement to javafx.fxml;
    exports com.example.gestionevenement;
}