package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
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
import javafx.scene.Scene;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import java.util.List;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;


public class EvenementFront implements Initializable {

    @FXML
    private TilePane cardsContainer;

    @FXML
    private HBox latestEventsBox;

    @FXML
    private ScrollPane latestEventsScrollPane;



    private EvenementService evenementService = new EvenementService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadEvenements();
        loadLatestEvenements();
        animateLatestEvents();
    }

    private void loadEvenements() {
        for (Evenement evenement : evenementService.getAll()) {
            VBox card = createEvenementCard(evenement);
            cardsContainer.getChildren().add(card);

        }
    }

    private VBox createEvenementCard(Evenement evenement) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evenement-card");

        // Display image
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(evenement.getImage())));
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            card.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Display title
        Label titleLabel = new Label(evenement.getTitre());
        titleLabel.getStyleClass().add("evenement-title");
        titleLabel.setWrapText(true); // Set wrapText to true
        titleLabel.setMaxWidth(Double.MAX_VALUE); // Set maximum width to accommodate full text
        card.getChildren().add(titleLabel);

        // Display date
        Label dateLabel = new Label("Date: " + evenement.getDate());
        dateLabel.getStyleClass().add("evenement-date");
        card.getChildren().add(dateLabel);

        // Display lieu
        Label lieuLabel = new Label("Lieu: " + evenement.getLieu());
        lieuLabel.getStyleClass().add("evenement-lieu");
        card.getChildren().add(lieuLabel);

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
        // Create VBox to hold all elements
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        // Image
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(evenement.getImage())));
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            HBox imageBox = new HBox(imageView);
            imageBox.setAlignment(Pos.CENTER);
            vbox.getChildren().add(imageBox);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Details labels
        Label titleLabel = new Label("Title: " + evenement.getTitre());
        Label dateLabel = new Label("Date: " + evenement.getDate());
        Label dureeLabel = new Label("Duration: " + evenement.getDuree());
        Label lieuLabel = new Label("Location: " + evenement.getLieu());
        Label objectifLabel = new Label("Objective: " + evenement.getObjectif());

        // Add details labels to VBox
        vbox.getChildren().addAll(titleLabel, dateLabel, dureeLabel, lieuLabel, objectifLabel);

        // Create and configure the stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Event Details");

        // Create scene and set it to the stage
        Scene scene = new Scene(vbox, 400, 300);
        popupStage.setScene(scene);

        // Show the stage
        popupStage.show();

    }
    private void loadLatestEvenements() {
        if (latestEventsBox != null) {
            List<Evenement> latestEvents = evenementService.getLatestEvents(10); // Get the latest 5 events
            for (Evenement evenement : latestEvents) {
                VBox card = createSimpleEventCard(evenement); // Create a simple event card
                latestEventsBox.getChildren().add(card);
            }
        } else {
            System.out.println("latestEventsBox is null");
        }
    }

    private VBox createSimpleEventCard(Evenement evenement) {
        VBox card = new VBox(10);
        card.getStyleClass().add("evenement-card");

        // Display title
        Label titleLabel = new Label(evenement.getTitre());
        titleLabel.getStyleClass().add("evenement-title");
        titleLabel.setWrapText(true); // Set wrapText to true
        titleLabel.setMaxWidth(Double.MAX_VALUE); // Set maximum width to accommodate full text
        card.getChildren().add(titleLabel);

        // Display date
        Label dateLabel = new Label("Date: " + evenement.getDate());
        dateLabel.getStyleClass().add("evenement-date");
        card.getChildren().add(dateLabel);

        // Display lieu
        Label lieuLabel = new Label("Lieu: " + evenement.getLieu());
        lieuLabel.getStyleClass().add("evenement-lieu");
        card.getChildren().add(lieuLabel);

        // Add double-click event handler
        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Show full details in popup
                showFullDetailsPopup(evenement);
            }
        });

        return card;
    }
    private void animateLatestEvents() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(10), latestEventsBox);
        transition.setToX(-200);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();
    }




}
