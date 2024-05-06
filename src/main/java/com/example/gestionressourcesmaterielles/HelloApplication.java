package com.example.gestionressourcesmaterielles;

import com.example.gestionressourcesmaterielles.Service.MaterielService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    MaterielService materielService=new MaterielService();


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("CategorieInterfaceFront.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
       // System.out.println(MaterielService.chatGPT("Bonjour"));
        launch();
    }
}