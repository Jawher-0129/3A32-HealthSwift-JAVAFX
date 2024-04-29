package com.example.noubez;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.example.noubez.Model.Personnel;
import com.example.noubez.Service.PersonnelService;


import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.geometry.Pos;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class PersonnelFrontController implements Initializable {

    private final PersonnelService personnelService = new PersonnelService();

    @FXML
    private ScrollPane NosPersonnelsScrollPane;

    @FXML
    private ScrollPane TopPersonnelScrollPane;

    @FXML
    private HBox TopPersonnelsBox;

    @FXML
    private TilePane cardsContainer;


    private PersonnelService PersonnelService = new PersonnelService();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadPersonnels();
        loadTopPersonnels();
        animateTopPersonnels();

    }

    private void loadPersonnels() {
        for (Personnel personnel : personnelService.getAll()) {
            if(personnel.getRating()<5) {
                VBox card = createPersonnelCard(personnel);
                cardsContainer.getChildren().add(card);
            }

        }
    }



    private VBox createPersonnelCard(Personnel personnel) {
        VBox card = new VBox(10);
        card.getStyleClass().add("personnel-card");

        // Display image


       // String image="C:\\Users\\Admin\\Desktop\\3A32HealthSwift\\public\\uploads\\gold.jpeg";
        //String image1="C:\\Users\\Admin\\Desktop\\3A32HealthSwift\\public\\uploads\\f4d06b562184677a2a4a5c3e79f4e333.jpg";


        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(personnel.getImage())));
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            card.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



        Label nomLabel = new Label("Nom: " + personnel.getNom());
        nomLabel.getStyleClass().add("personnel-nom");
        card.getChildren().add(nomLabel);

        Label prenomLabel = new Label("Prenom: " + personnel.getPrenom_personnel());
        prenomLabel.getStyleClass().add("personnel-prenom");
        card.getChildren().add(prenomLabel);


        Label ratingLabel = new Label("Rating: " + personnel.getRating());
        ratingLabel.getStyleClass().add("personnel-rating");
        card.getChildren().add(ratingLabel);

        HBox ratingStars = createRatingStars(personnel.getRating());
        card.getChildren().add(ratingStars);

        card.setOnMouseClicked(Personnel -> {
            if (Personnel.getClickCount() == 2) {
                // Show full details in popup
                showDetailsPersonnel(personnel);
            }
        });

        return card;
    }







    private void showDetailsPersonnel(Personnel personnel) {
        // Create VBox to hold all elements
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));



        // Image
        try {
            ImageView imageView = new ImageView(new Image(new FileInputStream(personnel.getImage())));
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            HBox image = new HBox(imageView);
            image.setAlignment(Pos.CENTER);
            vbox.getChildren().add(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Details labels
        Label nomLabel = new Label("Nom: " + personnel.getNom());
        Label prenomLabel = new Label("Prenom: " + personnel.getPrenom_personnel());
        Label ratingLabel = new Label("Rating: " + personnel.getRating());
        Label roleLabel = new Label("Role: " + personnel.getRole());


        // Add details labels to VBox
        vbox.getChildren().addAll(nomLabel, prenomLabel, ratingLabel, roleLabel);

        // Create and configure the stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Personnel Details");

        // Create scene and set it to the stage
        Scene scene = new Scene(vbox, 400, 300);
        popupStage.setScene(scene);

        // Show the stage
        popupStage.show();

    }
    private void loadTopPersonnels() {
        if (TopPersonnelsBox != null) {
            List<Personnel> topPersonnels = personnelService.getTopPersonnels(5);
            for (Personnel personnel : topPersonnels) {
                VBox card = createNosPersonnelCard(personnel); // Create a simple personnel card
                TopPersonnelsBox.getChildren().add(card);
            }
        } else {
            System.out.println("topBox is null");
        }
    }
    private VBox createNosPersonnelCard(Personnel personnel) {
        VBox card = new VBox(10);
        card.getStyleClass().add("personnel-card");


        Label nomLabel = new Label("Nom: " + personnel.getNom());
        nomLabel.getStyleClass().add("personnel-nom");
        card.getChildren().add(nomLabel);


        Label prenomLabel = new Label("Prenom: " + personnel.getPrenom_personnel());
        prenomLabel.getStyleClass().add("personnel-prenom");
        card.getChildren().add(prenomLabel);


        Label ratingLabel = new Label("Rating: " + personnel.getRating());
        ratingLabel.getStyleClass().add("personnel-rating");
        card.getChildren().add(ratingLabel);


        // Appel à la méthode createRatingStars pour générer les étoiles de rating
        HBox ratingStars = createRatingStars(personnel.getRating());
        card.getChildren().add(ratingStars);

        ImageView imageView = new ImageView();
        try {
            Image image = new Image(personnel.getImage());
            imageView.setImage(image);
            imageView.setFitWidth(150);
            imageView.setFitHeight(150);
            card.getChildren().add(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add double-click personnel handler
        card.setOnMouseClicked(Personnel -> {
            if (Personnel.getClickCount() == 2) {
                // Show full details in popup
                showDetailsPersonnel(personnel);
            }
        });

        return card;

    }
    // Méthode pour générer une image d'étoile dorée en fonction du rating
    // Dans votre méthode createRatingStars
    private HBox createRatingStars(int rating) {
        HBox starsBox = new HBox(5);
        int maxRating = 5; // Nombre maximum d'étoiles
        for (int i = 0; i < maxRating; i++) {
            ImageView starImageView = new ImageView();
            if (i < rating) {
                // Si l'index est inférieur au rating, affichez une étoile dorée
                starImageView.setImage(new Image("C:\\Users\\Admin\\Desktop\\3A32HealthSwift\\public\\uploads/etoile.png")); // Remplacez par le chemin de votre image d'étoile dorée
            } else {
                // Sinon, affichez une étoile vide ou grise
                starImageView.setImage(new Image("C:\\Users\\Admin\\Desktop\\3A32HealthSwift\\public\\uploads/etoilevide.png")); // Remplacez par le chemin de votre image d'étoile vide ou grise
            }
            starImageView.setFitWidth(20); // Ajustez la largeur de l'étoile selon vos préférences
            starImageView.setFitHeight(20); // Ajustez la hauteur de l'étoile selon vos préférences
            starsBox.getChildren().add(starImageView);
        }
        return starsBox;
    }

    private void animateTopPersonnels() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(10), TopPersonnelsBox);
        transition.setToX(-200);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.play();
    }


}
