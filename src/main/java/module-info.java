module com.example.gestionressourcesmaterielles {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens com.example.gestionressourcesmaterielles.Model;
    opens com.example.gestionressourcesmaterielles.Controller to javafx.fxml;

    opens com.example.gestionressourcesmaterielles to javafx.fxml;
    exports com.example.gestionressourcesmaterielles;
}