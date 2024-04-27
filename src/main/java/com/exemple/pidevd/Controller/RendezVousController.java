package com.exemple.pidevd.Controller;
import com.exemple.pidevd.Model.Demande;
import com.exemple.pidevd.Model.RendezVous;
import com.exemple.pidevd.Service.DemandeService;
import com.exemple.pidevd.Service.RendezVousService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class RendezVousController {
    @FXML
    private TableView<RendezVous> rvtable;
    @FXML
    private Label demmm;


    @FXML
    private Button add;

    @FXML
    private DatePicker date;

    @FXML
    private TextField lieu;

    @FXML
    private TextArea objective;
    private RendezVousService rendezVousService;
    private DemandeService demandeService;
    @FXML
    private TableColumn<RendezVous,Timestamp> dater;

    @FXML
    private TableColumn<RendezVous,Integer> demander;
    @FXML
    private TableColumn<RendezVous,Integer> id_rendezvousr;

    @FXML
    private TableColumn<RendezVous,String> lieur;

    @FXML
    private TableColumn<RendezVous,String> objectiver;

    public RendezVousController() {
        rendezVousService = new RendezVousService();
        demandeService = new DemandeService();
    }
    private Map<Integer, String> typeDemandeMap = new HashMap<>();
    @FXML
    private ChoiceBox<String> heure;



    @FXML
    private void initializeDemandeChoiceBox() {

        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String formattedHour = String.format("%02d", hour); // Ajoute un zéro en tête pour les heures < 10
                String formattedMinute = String.format("%02d", minute); // Ajoute un zéro en tête pour les minutes < 10
                heure.getItems().add(formattedHour + ":" + formattedMinute);
            }
        }

        // Définis une valeur par défaut, si nécessaire
        heure.setValue("00:00");


    }
    @FXML
    private int getKeyFromValue(Map<Integer, String> map, String value) {
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return -1; // Si la valeur n'est pas trouvée, retourne -1
    }
    @FXML
    private void initialize() {
        initializeTable();
        initializeDemandeChoiceBox();


    }
    private void initializeTable() {
        if (id_rendezvousr != null && demander != null && dater != null && lieur != null && objectiver != null ) {
            // Associer les propriétés aux colonnes de la TableView
            id_rendezvousr.setCellValueFactory(new PropertyValueFactory<>("id_rendezvous"));
            demander.setCellValueFactory(new PropertyValueFactory<>("demande"));
            dater.setCellValueFactory(new PropertyValueFactory<>("date"));
            lieur.setCellValueFactory(new PropertyValueFactory<>("lieu"));
            objectiver.setCellValueFactory(new PropertyValueFactory<>("objective"));

            // Charger les données dans la TableView
            loadRV();
        } else {
            System.err.println("Erreur : Les éléments de la TableView ou la TableView elle-même sont null.");
        }
    }

    private void loadRV() {
        try {
            List<RendezVous> rv = rendezVousService.getAll();
            ObservableList<RendezVous> observableRV = FXCollections.observableArrayList(rv);
            if (rvtable != null) {
                rvtable.setItems(observableRV);
            } else {
                System.err.println("Erreur : La TableView est null.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer l'exception d'une manière appropriée
        }
    }
    @FXML
    protected void handleAddRendezVous() {
        /*String demandeTitre = demande.getValue();
        int demandeId = getKeyFromValue(typeDemandeMap, demandeTitre); */
        String idDemandeText = demmm.getText();
// Vérifiez si le texte contient uniquement des chiffres
        if (!idDemandeText.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "ID de demande invalide", "L'ID de demande ne contient pas uniquement des chiffres.");
            return;
        }

// Si le texte ne contient que des chiffres, vous pouvez maintenant le convertir en entier en toute sécurité
        int demandeId = Integer.parseInt(idDemandeText);
        if (demandeId != -1) { // Vérifie si l'ID du don est valide
            String lieuText = lieu.getText(); // Récupère la description depuis la zone de texte
            String objectiveText = objective.getText();
            LocalDate localDate = date.getValue(); // Récupère la date depuis le DatePicker
            String selectedHour = heure.getValue();
            java.sql.Date dateSql = java.sql.Date.valueOf(localDate);
            if (localDate != null && selectedHour != null) {
                LocalTime localTime = LocalTime.parse(selectedHour); // Convertis l'heure sélectionnée en LocalTime
                LocalDateTime dateTime = LocalDateTime.of(localDate, localTime); // Combine la date et l'heure
                Timestamp timestamp = Timestamp.valueOf(dateTime); // Convertis en Timestamp

                RendezVous nouvelleR = new RendezVous(timestamp, lieuText, objectiveText,demandeId);
                // Ajoutez la nouvelle demande via le service
                rendezVousService.add(nouvelleR);

                demandeService.Acceptee(demandeId);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Demande Ajout", "La demande a été ajoutée avec succès.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout de demande", "Veuillez sélectionner date");
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout de demande", "Veuillez sélectionner un type de don valide.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    public void initData(int idDemande) {
        // Utilise l'ID de la demande pour effectuer des opérations dans l'interface RendezVous.fxml
        demmm.setText(String.valueOf(idDemande));
    }
    @FXML
    protected void handleDeleteDemande() {
        // Récupérer la demande sélectionnée dans la TableView
        RendezVous rvSelectionnee = rvtable.getSelectionModel().getSelectedItem();

        if (rvSelectionnee != null) {
            // Demander confirmation à l'utilisateur avant de supprimer la demande
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer le rendez-vous");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette rendez-vous ?");

            // Attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmation.showAndWait();

            // Si l'utilisateur a confirmé la suppression
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la demande à partir du service
                rendezVousService.delete(rvSelectionnee.getId_rendezvous());

                // Actualiser la TableView
                loadRV();
            }
        } else {
            // Aucune demande sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un redez-vous à supprimer.");
            alert.showAndWait();
        }
    }
    /*
    @FXML

    private void handleUpdateDemande() {
        RendezVous rvSelectionnee = rvtable.getSelectionModel().getSelectedItem();
        if (rvSelectionnee != null) {
            // Récupérer les nouvelles valeurs des champs
            String newlieu = lieu.getText();
            String newDescription = objective.getText();
            LocalDate localDate = date.getValue(); // Récupère la date depuis le DatePicker
            String selectedHour = heure.getValue();
            java.sql.Date dateSql = java.sql.Date.valueOf(localDate);

            int demande = rvSelectionnee.getDemande();
            Demande demande = new Demande(donId, new Date(), newDescription, newTitre, 1);
            demandeService.update(demande,demandeSelectionnee.getId_demande());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Demande mise à jour", "La demande a été mise à jour avec succès.");
            initializeDemandeTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune demande sélectionnée", "Veuillez sélectionner une demande à mettre à jour.");
        }
    }*/


}