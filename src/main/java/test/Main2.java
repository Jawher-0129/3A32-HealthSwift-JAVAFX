package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CampagneView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Health Swift");
        stage.setScene(scene);
        stage.show();
    }

}