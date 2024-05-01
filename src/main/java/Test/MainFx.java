package Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class MainFx extends Application {
    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ActualiteFront.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Health Swift");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event ->{
            event.consume();
            logout(stage);
        } );
    }

    public void logout(Stage stage){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("logout");
        alert.setHeaderText("you're about to logout !");
        alert.setContentText("Are you sure you want to logout ?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("you successfully logged Out !");
            stage.close();
        }
    }
}
