package com.example.gestionressourcesmaterielles.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SidebarController {

    private MainController mainController;
    private StatMaterielController statMaterielController;


    public void setStatController(StatMaterielController statMaterielController) {
        System.out.println("Setting stat controller");
        this.statMaterielController = statMaterielController;
    }
    public void setMainController(MainController mainController) {
        System.out.println("Setting main controller");
        this.mainController = mainController;
    }

    @FXML
    private void handleHome() {
        loadUI("/com/example/gestionressourcesmaterielles/CategorieInterfaceAdmin.fxml");
    }

    @FXML
    private void handleSettings() {
        loadUI("/com/example/gestionressourcesmaterielles/MaterielInterfaceAdmin.fxml");
    }

    private void loadUI(String ui) {
        if (mainController != null) { // Check if mainController is not null
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(ui));
                Node node = loader.load();
                mainController.setCenter(node);
            } catch (IOException e) {
                System.out.println("Error loading FXML: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("MainController is null. Cannot set center.");
        }
    }

}
