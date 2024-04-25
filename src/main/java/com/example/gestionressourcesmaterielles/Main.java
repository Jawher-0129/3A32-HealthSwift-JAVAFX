package com.example.gestionressourcesmaterielles;

import com.example.gestionressourcesmaterielles.Controller.MainController;
import com.example.gestionressourcesmaterielles.Controller.SidebarController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainLayout.fxml"));
        BorderPane root = mainLoader.load();

        MainController mainController = mainLoader.getController();

        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("Sidebar.fxml"));
        BorderPane sidebar = sidebarLoader.load(); // Corrected casting to BorderPane
        SidebarController sidebarController = sidebarLoader.getController();
        sidebarController.setMainController(mainController);

        // Set the sidebar to the left of the main layout
        root.setLeft(sidebar);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
