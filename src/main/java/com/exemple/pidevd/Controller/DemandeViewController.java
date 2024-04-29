package com.exemple.pidevd.Controller;

import com.exemple.pidevd.Model.Demande;
import com.exemple.pidevd.Service.DemandeService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class DemandeViewController {
    @FXML
    private VBox VboxDemande;
    private DemandeService demandeService;

    public DemandeViewController() {
        demandeService = new DemandeService();
    }


    @FXML
    private Label description;

    @FXML
    private Label titre;

    @FXML
    private Label id;

    @FXML
    private Button modifier;

    @FXML
    private Button supprimer;
    private Controller mainController;
    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

  public void setData(Demande demande){
      titre.setText(demande.getTitre());
      description.setText(demande.getDescription());
      id.setText(String.valueOf(demande.getId_demande()));
      // Style pour le titre
      titre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

      // Style pour la description
      description.setStyle("-fx-font-size: 12px;");

      // Style pour l'ID
      id.setStyle("-fx-font-size: 10px;");

      // Style pour le bouton modifier
      modifier.setStyle("-fx-background-color: linear-gradient(to bottom, #007bff, #0056b3);" +
              "-fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8px 16px;");

      // Style pour le bouton supprimer
      supprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 8px 16px;");
  }
    @FXML
    protected void handleDeleteDemande() {
        // Demander confirmation à l'utilisateur avant de supprimer la demande
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la demande");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette demande ?");

        // Attendre la réponse de l'utilisateur
        Optional<ButtonType> result = confirmation.showAndWait();

        // Si l'utilisateur a confirmé la suppression
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Supprimer la demande à partir du service
            int idD = Integer.parseInt(id.getText());
            // Utilisez la méthode delete avec l'ID extrait
            demandeService.delete(idD);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/card.fxml"));
                Parent root = loader.load();
                supprimer.getScene().setRoot(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
          // mainController.refreshDemandeGrid();

        }
    }

    @FXML
    void passerAModifier() {
        int demandeId = Integer.parseInt(id.getText());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/ModifierDemande.fxml"));
            Parent root = loader.load();
            modifier.getScene().setRoot(root);
            DemandeFrontController demandeFrontController = loader.getController();
            demandeFrontController.initData(demandeId); // Passer l'ID de la demande au contrôleur

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void PageQrCode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/ModifierDemande.fxml"));
            Parent root=loader.load();
            modifier.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
