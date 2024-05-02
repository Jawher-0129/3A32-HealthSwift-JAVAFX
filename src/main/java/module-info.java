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
    requires org.controlsfx.controls;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires java.mail;
    requires twilio;
    //requires java.sdk;//for mailling

}