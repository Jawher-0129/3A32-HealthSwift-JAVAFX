package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane; // Make sure to import BorderPane
import javafx.scene.layout.VBox; // Import VBox if you are going to use it
import controller.MainController; // Import the MainController if it's in a different package
import controller.SidebarController; // Import the SidebarController if it's in a different package

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/MainLayout.fxml"));
        BorderPane root = mainLoader.load();

        MainController mainController = mainLoader.getController();

        FXMLLoader sidebarLoader = new FXMLLoader(getClass().getResource("/Sidebar.fxml"));
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
