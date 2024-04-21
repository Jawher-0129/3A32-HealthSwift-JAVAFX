package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import service.DonService;
import service.CampagneService;
import entite.Don;
import entite.Campagne;
import java.time.LocalDate;
import java.util.List;
import javafx.util.StringConverter;

public class DonFormController {

    @FXML private Label labelTitre;
    @FXML private TextField champType;
    @FXML private TextField champMontant;
    @FXML private DatePicker selecteurDateRemise;
    @FXML private ComboBox<Campagne> comboCampagne;
    @FXML private Button boutonEnregistrer;

    private DonService serviceDon = new DonService();
    private CampagneService serviceCampagne = new CampagneService();
    private Don donActuel;

    public void initialize() {
        updateFormTitle(); // Ensure title is updated based on initial state
        chargerCampagnes();
        boutonEnregistrer.setOnAction(event -> gererEnregistrementDon());
    }

    public void setDon(Don don) {
        donActuel = don;
        updateFormTitle();
        if (don != null) {
            champType.setText(don.getType());
            champMontant.setText(don.getMontant() != null ? don.getMontant().toString() : "");
            selecteurDateRemise.setValue(LocalDate.parse(don.getDate_remise()));
            comboCampagne.setValue(findCampagneById(don.getCampagne_id()));
        } else {
            clearForm();
        }
    }

    private void updateFormTitle() {
        if (donActuel == null) {
            labelTitre.setText("Ajouter un Nouveau Don");
        } else {
            labelTitre.setText("Modifier le Don");
        }
    }

    private Campagne findCampagneById(Integer campagneId) {
        return campagneId == null ? null : serviceCampagne.findById(campagneId).orElse(null);
    }

    private void chargerCampagnes() {
        List<Campagne> campagnes = serviceCampagne.findAll();
        comboCampagne.setItems(FXCollections.observableArrayList(campagnes));
        comboCampagne.setConverter(new StringConverter<Campagne>() {
            @Override
            public String toString(Campagne campagne) {
                return campagne == null ? null : campagne.getTitre();
            }
            @Override
            public Campagne fromString(String string) {
                return null;
            }
        });
    }

    private void gererEnregistrementDon() {
        if (!validerSaisie()) {
            afficherAlerte(Alert.AlertType.WARNING, "Validation échouée", "Veuillez corriger les champs invalides.");
            return;
        }

        String type = champType.getText();
        Integer montant = essayerParserInt(champMontant.getText());
        LocalDate dateRemise = selecteurDateRemise.getValue();
        Campagne selectedCampagne = comboCampagne.getSelectionModel().getSelectedItem();

        if (donActuel == null) {
            donActuel = new Don(type, montant, dateRemise.toString(), selectedCampagne != null ? selectedCampagne.getId() : null);
            Don donEnregistre = serviceDon.save(donActuel);
            if (donEnregistre != null) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Don ajouté avec succès.");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ajout du don a échoué.");
            }
        } else {
            donActuel.setType(type);
            donActuel.setMontant(montant);
            donActuel.setDate_remise(dateRemise.toString());
            donActuel.setCampagne_id(selectedCampagne != null ? selectedCampagne.getId() : null);

            Don donMisAJour = serviceDon.update(donActuel);
            if (donMisAJour != null) {
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Don mis à jour avec succès.");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "La mise à jour du don a échoué.");
            }
        }
    }

    private boolean validerSaisie() {
        String errorMessage = "";
        if (champType.getText().isEmpty()) {
            errorMessage += "Le type ne peut pas être vide.\n";
        }
        if (selecteurDateRemise.getValue() == null) {
            errorMessage += "La date de remise est requise.\n";
        }
        if (!errorMessage.isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Saisie invalide", errorMessage);
            return false;
        }
        return true;
    }

    private Integer essayerParserInt(String texte) {
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void afficherAlerte(Alert.AlertType typeAlerte, String titre, String contenu) {
        Alert alerte = new Alert(typeAlerte);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(contenu);
        alerte.showAndWait();
    }

    private void clearForm() {
        champType.clear();
        champMontant.clear();
        selecteurDateRemise.setValue(null);
        comboCampagne.getSelectionModel().clearSelection();
    }
}
