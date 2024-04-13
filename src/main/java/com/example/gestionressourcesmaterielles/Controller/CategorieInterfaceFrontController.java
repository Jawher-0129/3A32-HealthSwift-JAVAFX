package com.example.gestionressourcesmaterielles.Controller;

import com.example.gestionressourcesmaterielles.Model.Categorie;
import com.example.gestionressourcesmaterielles.Model.Materiel;
import com.example.gestionressourcesmaterielles.Service.CategorieService;
import com.example.gestionressourcesmaterielles.Service.MaterielService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import java.util.List;

public class CategorieInterfaceFrontController {
    private final CategorieService categorieService = new CategorieService();
    private final MaterielService materielService = new MaterielService();

    @FXML
    private VBox categorieButtonsVBox;

    @FXML
    private TilePane categorieTilePane;

    @FXML
    void initialize() {
        afficherCategories();
    }

    private void afficherCategories() {
        List<Categorie> categories = categorieService.getAll();
        for (Categorie categorie : categories) {
            Button categorieButton = new Button(categorie.getLibelle());
            categorieButton.setOnAction(event -> afficherMaterielsCategorie(categorie.getId()));
            categorieButtonsVBox.getChildren().add(categorieButton);
        }
    }

    private void afficherMaterielsCategorie(int idCategorie) {
        // Effacer le contenu actuel de la TilePane
        categorieTilePane.getChildren().clear();

        List<Materiel> materiels = materielService.getMaterielByCategorie(idCategorie);

        for (Materiel materiel : materiels) {
            VBox materielVBox = createMaterielVBox(materiel);
            categorieTilePane.getChildren().add(materielVBox);
        }
    }

    private VBox createMaterielVBox(Materiel materiel) {
        VBox materielVBox = new VBox();
        materielVBox.setSpacing(10);

        // Image du matériel
        ImageView imageView = new ImageView(new Image(materiel.getImageMateriel()));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        materielVBox.getChildren().add(imageView);

        // Libellé du matériel
        Label libelleLabel = new Label(materiel.getLibelleMateriel());
        libelleLabel.setStyle("-fx-font-weight: bold;");
        materielVBox.getChildren().add(libelleLabel);

        // Description du matériel
        Label descriptionLabel = new Label(materiel.getDescription());
        materielVBox.getChildren().add(descriptionLabel);

        return materielVBox;
    }




}
