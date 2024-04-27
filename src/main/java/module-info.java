module com.exemple.pidevd {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    opens com.exemple.pidevd.Model;
    opens com.exemple.pidevd.Controller to javafx.fxml;

    opens com.exemple.pidevd to javafx.fxml;
    exports com.exemple.pidevd;
}