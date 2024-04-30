module com.exemple.pidevd {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires twilio;
    requires com.sun.jna;
    requires com.sun.jna.platform;
    requires org.controlsfx.controls;
    requires poi;
    requires poi.ooxml;
    requires javafx.swing;
    requires org.jxmapviewer.jxmapviewer2;
    requires com.gluonhq.maps;
    requires json.simple;
    opens com.exemple.pidevd.Model;
    opens com.exemple.pidevd.Controller to javafx.fxml;

    opens com.exemple.pidevd to javafx.fxml;
    exports com.exemple.pidevd;
    exports com.exemple.pidevd.Controller;
}