package com.example.gestionressourcesmaterielles;

import com.example.gestionressourcesmaterielles.Service.MaterielService;
import com.example.gestionressourcesmaterielles.util.IntelligenceArtificiel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    MaterielService materielService=new MaterielService();



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Chatbot.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        List<String> list=new ArrayList<>();
        list.add("Bonjour comment allez vous");
        IntelligenceArtificiel.analyzeAnswersSentiment(list);
        // System.out.println(MaterielService.chatGPT("Bonjour"));
        launch();
    }
}