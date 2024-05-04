package Test;

import Controller.MainController;
import Controller.SidebarController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main2 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/Fxml/MainLayout.fxml"));
        BorderPane root = mainLoader.load();

        MainController mainController = mainLoader.getController();

        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("/Fxml/Sidebar.fxml"));
        BorderPane sidebar = sidebarLoader.load(); // Corrected casting to BorderPane
        SidebarController sidebarController = sidebarLoader.getController();
        sidebarController.setMainController(mainController);


        root.setLeft(sidebar);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Health Swift");
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
        alert.setContentText("Are you sure yo want to exit ?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("you successfully logged Out !");
            stage.close();
        }
    }

}