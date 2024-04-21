package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import entite.Evenement;
import Service.EvenementService;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class EvenementFront implements Initializable {

    @FXML
    private FlowPane cardsContainer;

    private EvenementService evenementService = new EvenementService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEvenements();
    }

    private void loadEvenements() {
        for (Evenement evenement : evenementService.getAll()) {
            VBox card = createEvenementCard(evenement);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createEvenementCard(Evenement evenement) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #ffffff; -fx-background-radius: 5; -fx-pref-width: 200px; -fx-pref-height: 200px;");

        // Display title
        Label titleLabel = new Label("Title: " + evenement.getTitre());
        card.getChildren().add(titleLabel);

        // Display date
        Label dateLabel = new Label("Date: " + evenement.getDate());
        card.getChildren().add(dateLabel);

        // Display lieu
        Label lieuLabel = new Label("Lieu: " + evenement.getLieu());
        card.getChildren().add(lieuLabel);

        // Display image
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(evenement.getImage())));
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            card.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Add double-click event handler
        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Show full details in popup
                showFullDetailsPopup(evenement);
            }
        });

        return card;
    }


    private void showFullDetailsPopup(Evenement evenement) {
        // Create a VBox to hold the details
        VBox vbox = new VBox();
        vbox.setSpacing(10); // Set spacing between elements

        // Add labels for each detail
        Label titleLabel = new Label("Title: " + evenement.getTitre());
        Label dateLabel = new Label("Date: " + evenement.getDate());
        Label dureeLabel = new Label("Duration: " + evenement.getDuree());
        Label lieuLabel = new Label("Location: " + evenement.getLieu());
        Label objectifLabel = new Label("Objective: " + evenement.getObjectif());

        // Add labels to the VBox
        vbox.getChildren().addAll(titleLabel, dateLabel, dureeLabel, lieuLabel, objectifLabel);

        // Create an alert dialog to display the details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event Details");
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(vbox);

        // Set the dialog to be modal
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.initModality(Modality.APPLICATION_MODAL);

        // Show the dialog
        alert.showAndWait();
    }
}
