package com.example.gestionressourcesmaterielles.Controller;

import com.itextpdf.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class MainController {
    @FXML
    private BorderPane mainBorderPane;

    public void setCenter(Node node) {
        mainBorderPane.setCenter(node);
    }
    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Your Application");
        alert.setContentText("This is a simple application created using JavaFX.");
        alert.showAndWait();
    }
}
