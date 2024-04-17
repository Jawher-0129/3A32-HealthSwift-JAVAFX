package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import service.Usercrud;
import Entity.User;

import java.util.List;

public class StatController {

    @FXML
    PieChart pieChart;

    @FXML
    void displaystats(ActionEvent event) {
        // Récupérer tous les utilisateurs
        Usercrud userService = new Usercrud();
        List<User> users = userService.getAllData();

        // Initialiser les compteurs pour chaque rôle
        int donateurCount = 0;
        int directeurCount = 0;

        // Compter le nombre d'utilisateurs pour chaque rôle
        for (User user : users) {
            String role = user.getRoles();
            if ("Donateur".equals(role)) {
                donateurCount++;
            } else if ("Directeur de campagne".equals(role)) {
                directeurCount++;
            }
            // Vous pouvez ajouter d'autres conditions pour d'autres rôles si nécessaire
        }

        // Créer les données pour le PieChart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Donateur", donateurCount),
                        new PieChart.Data("Directeur de campagne", directeurCount));
        // Vous pouvez ajouter d'autres données ici pour d'autres rôles

        // Définir les données sur le PieChart
        pieChart.setData(pieChartData);
    }
    private void switchScene(String fxmlFile, ActionEvent event) {
        try {
            System.out.println("fxml:"+ fxmlFile);

            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    void back(ActionEvent event) {
        switchScene("/AdminPage.fxml", event);

    }
}
