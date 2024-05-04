module com.example.noubez {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    requires java.sql;
    requires java.mail;
    requires org.apache.pdfbox;
    requires twilio;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    opens com.example.noubez.Model;
    opens com.example.noubez to javafx.fxml;



    opens com.example.noubez.util to javafx.fxml;
    exports com.example.noubez;
}