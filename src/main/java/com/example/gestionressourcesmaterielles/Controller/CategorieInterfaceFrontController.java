package com.example.gestionressourcesmaterielles.Controller;

import com.example.gestionressourcesmaterielles.Model.Categorie;
import com.example.gestionressourcesmaterielles.Service.CategorieService;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CategorieInterfaceFrontController implements Initializable {

    @FXML
    private TilePane categoryContainer;
    @FXML
    private Button voirMaterielButton;

    private CategorieService categorieService;

    private final String DEFAULT_CATEGORY_IMAGE_URL = "C:\\Users\\jawhe\\OneDrive\\Bureau\\3A32HealthSwiftIntegration (2)\\3A32HealthSwiftIntegration\\3A32HealthSwift\\public\\uploads\\f4d06b562184677a2a4a5c3e79f4e333.jpg";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categorieService = new CategorieService();
        loadCategoryImages();
    }

    private void loadCategoryImages() {
        List<Categorie> categories = categorieService.getAll();
        for (Categorie category : categories) {
            VBox card = createCategoryImageCard(category);
            if (card != null) {
                categoryContainer.getChildren().add(card);
            }
        }
    }

    private VBox createCategoryImageCard(Categorie category) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-background-color: #ffffff; -fx-background-radius: 5;");

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(new File(DEFAULT_CATEGORY_IMAGE_URL).toURI().toString());
            imageView.setImage(image);
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'image par défaut : " + e.getMessage());
        }
        Label label = new Label(category.getLibelle()); // Création du label avec le libellé de la catégorie

        Button voirMaterielButton = new Button("Voir Matériel");

        voirMaterielButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionressourcesmaterielles/MaterielInterfaceFront.fxml"));
                    Parent root = loader.load();
                    MaterielInterfaceFrontController materielInterfaceC = loader.getController();
                    materielInterfaceC.setCategoryId(category.getId()); // Appeler la méthode pour transmettre categoryId

                    // Récupérez la scène actuelle à partir du bouton
                    Scene currentScene = voirMaterielButton.getScene();

                    // Obtenez le stage de la scène actuelle
                    Stage stage = (Stage) currentScene.getWindow();

                    // Créez une nouvelle scène avec la racine chargée à partir du FXML
                    Scene newScene = new Scene(root);

                    // Définissez la nouvelle scène sur le stage
                    stage.setScene(newScene);
                    stage.show();

                    System.out.println(category.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        card.getChildren().addAll(imageView, label, voirMaterielButton); // Ajout de l'image, du libellé et du bouton à la VBox
        return card;
    }


}
