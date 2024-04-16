package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import entite.Don;
import service.DonService;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class DonViewController {

    @FXML
    private TilePane cardsContainer;

    private DonService donService = new DonService();

    @FXML
    public void initialize() {
        loadDons();
    }

    private void loadDons() {
        for (Don don : donService.findAll()) {
            VBox card = createDonCard(don);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createDonCard(Don don) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15, 12, 15, 12));
        card.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #ffffff; -fx-background-radius: 5;");

        Label typeLabel = new Label("Type: " + don.getType());
        typeLabel.setStyle("-fx-font-weight: bold;");

        Label amountLabel = new Label("Montant: " + (don.getMontant() == null ? "N/A" : don.getMontant() + " €"));
        Label dateLabel = new Label("Date de remise: " + don.getDate_remise());

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/don.PNG"))); // Assume placeholder.png is an image in your project
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        card.getChildren().addAll(imageView, typeLabel, amountLabel, dateLabel);

        return card;
    }
}
