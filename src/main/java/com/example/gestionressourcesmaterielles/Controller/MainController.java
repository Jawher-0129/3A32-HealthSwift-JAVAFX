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

    public void loadSidebar() {
        try {
            FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("Sidebar.fxml"));
            BorderPane sidebar = sidebarLoader.load();
            SidebarController sidebarController = sidebarLoader.getController();
            sidebarController.setMainController(this); // Pass the MainController to the SidebarController
            mainBorderPane.setLeft(sidebar);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
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
