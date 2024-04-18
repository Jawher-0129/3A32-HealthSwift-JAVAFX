package com.example.gestionressourcesmaterielles.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.example.gestionressourcesmaterielles.Model.Materiel;
import com.example.gestionressourcesmaterielles.Service.MaterielService;
import javafx.geometry.Insets;

import java.net.MalformedURLException;
import java.util.List;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.io.File;

public class MaterielInterfaceFrontController implements Initializable {

    @FXML
    private TilePane materialContainer;

    private MaterielService materielService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        materielService = new MaterielService();
        loadMaterialCards();
    }

    private void loadMaterialCards() {
        List<Materiel> materials = materielService.getAll();
        for (Materiel material : materials) {
            VBox card = createMaterialCard(material);
            if (card != null) {
                materialContainer.getChildren().add(card);
            }
        }
    }

    private VBox createMaterialCard(Materiel material) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #ffffff; -fx-background-radius: 5;");

        Label nameLabel = new Label("Nom: " + material.getLibelleMateriel());
        nameLabel.setStyle("-fx-font-weight: bold;");

        Label descriptionLabel = new Label("Description: " + material.getDescription());

        ImageView imageView = new ImageView();
        String imageUrl = material.getImageMateriel();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            try {
                Image image = new Image(new File(imageUrl).toURI().toString());
                imageView.setImage(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
            } catch (Exception e) {
                System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
            }
        }
        card.getChildren().addAll(imageView, nameLabel, descriptionLabel);
        return card;
    }


}
