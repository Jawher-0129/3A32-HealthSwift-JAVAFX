module com.example.gestionressourcesmaterielles {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.zxing;
    requires java.desktop;
    requires kernel;
    requires layout;
    requires io;
    requires twilio;
    requires org.controlsfx.controls;
    opens com.example.gestionressourcesmaterielles.Model;
    opens com.example.gestionressourcesmaterielles.Controller to javafx.fxml;


    opens com.example.gestionressourcesmaterielles to javafx.fxml;
    exports com.example.gestionressourcesmaterielles;
}