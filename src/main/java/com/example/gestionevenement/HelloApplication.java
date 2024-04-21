package com.example.gestionevenement;

import Controller.EvenementController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file for the EvenementController
        FXMLLoader evenementLoader = new FXMLLoader(getClass().getResource("/Fxml/ActualiteBack.fxml"));
        AnchorPane evenementPane = evenementLoader.load();

        // Get the controller associated with the FXML file
        EvenementController evenementController = evenementLoader.getController();

        // Set up the scene
        Scene scene = new Scene(evenementPane, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Your Application Title");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
