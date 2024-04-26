package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import java.io.IOException;

public class SidebarController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        System.out.println("Setting main controller");
        this.mainController = mainController;
    }

    @FXML
    private void handleHome() {
        loadUI("/Campagne.fxml");
    }

    @FXML
    private void handleSettings() {
        loadUI("/Don.fxml");
    }


    @FXML
    private void handleStat() {
        loadUI("/Statistics.fxml");
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
