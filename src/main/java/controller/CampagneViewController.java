package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import service.CampagneService;
import java.util.List;
import entite.Campagne;
import javafx.geometry.Insets;
import java.io.File;

public class CampagneViewController {
    @FXML
    private TilePane campagneContainer;

    private CampagneService campagneService;

    @FXML
    public void initialize() {
        campagneService = new CampagneService();
        loadCampagneCards();
    }

    private void loadCampagneCards() {
        List<Campagne> campagnes = campagneService.findAll();
        for (Campagne campagne : campagnes) {
            VBox card = createCampagneCard(campagne);
            campagneContainer.getChildren().add(card);
        }
    }

    private VBox createCampagneCard(Campagne campagne) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #ffffff; -fx-background-radius: 5;");

        Label titleLabel = new Label("Title: " + campagne.getTitre());
        titleLabel.setStyle("-fx-font-weight: bold;");

        Label startDateLabel = new Label("Start: " + campagne.getDate_debut());
        Label endDateLabel = new Label("End: " + campagne.getDate_fin());

        ImageView imageView = new ImageView();
        if (campagne.getImage() != null && !campagne.getImage().isEmpty()) {
            File imgFile = new File(campagne.getImage());
            if (imgFile.exists()) {
                imageView.setImage(new Image(imgFile.toURI().toString()));
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
            }
        }

        card.getChildren().addAll(imageView, titleLabel, startDateLabel, endDateLabel);
        return card;
    }
}
