package com.exemple.pidevd.Controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class sample implements Initializable {
    @FXML
    private ImageView exit;
    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML
    private AnchorPane contenu;
    @FXML
    private Button Calendrier;
    public AnchorPane getContenu() {
        return contenu;
    }
    private boolean isInitialized = false;
    @FXML
    private Button demande;
    private DemandeController demandeController;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
            Parent root = loader.load();

            DemandeController demandeController = loader.getController();
            demandeController.initSampleController(this); // Passez la référence du SampleController au DemandeController

            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
            Node sidebarContent = loader.load();

            // Récupérer le contrôleur de Demande.fxml si nécessaire
            DemandeController demandeController = loader.getController();

            // Ajouter le contenu initial de Demande.fxml à l'AnchorPane contenu
            contenu.getChildren().setAll(sidebarContent);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        slider.setTranslateX(-176);
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        Menu.setOnMouseClicked(event ->{
            TranslateTransition slide=new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-176);
            slide.setOnFinished((ActionEvent e)->{
              Menu.setVisible(false);
              MenuBack.setVisible(true);
            });
        } );
        MenuBack.setOnMouseClicked(event ->{
            TranslateTransition slide=new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);
            slide.setToX(-176);
            slide.play();
            slider.setTranslateX(0);
            slide.setOnFinished((ActionEvent e)->{
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        } );
        /*if (!isInitialized) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
                Node sidebarContent = loader.load();

                // Récupérer le contrôleur de Demande.fxml si nécessaire
                DemandeController demandeController = loader.getController();

                // Ajouter le contenu initial de Demande.fxml à l'AnchorPane contenu
                contenu.getChildren().setAll(sidebarContent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            isInitialized = true;
        }*/

    }
    @FXML
    protected void afficherDemande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
            Parent root = loader.load();

            DemandeController demandeController = loader.getController();
            demandeController.initSampleController(this); // Passez la référence du SampleController au DemandeController

            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void afficherRendezVous(int idDemande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/RendezVous.fxml"));
            Parent root = loader.load();

            RendezVousController rendezVousController = loader.getController();
            // Configurez le contrôleur de la vue RendezVous si nécessaire
            rendezVousController.initData(idDemande);
            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void afficherCalendrier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/calendrier.fxml"));
            Parent root = loader.load();

            CalenderController CalenController = loader.getController();
            // Configurez le contrôleur de la vue RendezVous si nécessaire
            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void afficherStatistique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Statistique.fxml"));
            Parent root = loader.load();

            StatController SController = loader.getController();
            // Configurez le contrôleur de la vue RendezVous si nécessaire
            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
