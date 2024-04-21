package Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import entite.Actualite;
import Service.ActualiteService;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

public class ActualiteFront implements Initializable {

    @FXML
    private TilePane cardsContainer;

    private ActualiteService actualiteService = new ActualiteService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadActualites();
    }

    private void loadActualites() {
        List<Actualite> actualites = actualiteService.getAll();
        for (Actualite actualite : actualites) {
            VBox card = createActualiteCard(actualite);
            cardsContainer.getChildren().add(card);
        }
    }

    private VBox createActualiteCard(Actualite actualite) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #ffffff; -fx-background-radius: 5; -fx-pref-width: 200px; -fx-pref-height: 200px;");

        Label titleLabel = new Label("Titre: " + actualite.getTitre());
        Label descriptionLabel = new Label("Description: " + actualite.getDescription());
        Label typeLabel = new Label("Type Pub Cible: " + actualite.getType_pub_cible());
        Label themeLabel = new Label("Theme: " + actualite.getTheme());

        card.getChildren().addAll(titleLabel, descriptionLabel, typeLabel, themeLabel);

        return card;
    }

}
