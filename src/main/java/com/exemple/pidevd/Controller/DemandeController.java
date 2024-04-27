
package com.exemple.pidevd.Controller;
import com.exemple.pidevd.HelloApplication;
import com.exemple.pidevd.Model.Demande;
import com.exemple.pidevd.Model.Don;
import com.exemple.pidevd.Service.DemandeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class DemandeController {

    @FXML
    private Button clear;
    @FXML
    private Button add;

    @FXML
    private Button delete;

    @FXML
    private TableView<Demande> demandeTable;

    @FXML
    private TextArea description;

    @FXML
    private ChoiceBox<String> don;

    @FXML
    private TextField status;

    @FXML
    private TextField titre;

    @FXML
    private Button update;
    @FXML

    private TableColumn<Demande, Integer> id_demande;
    @FXML
    private TableColumn<Demande, String> descriptionD;

    @FXML
    private TableColumn<Demande, Integer> donD;

    @FXML
    private TableColumn<Demande, String> titreD;


    @FXML
    private TableColumn<Demande, Date> date;

    @FXML
    private TableColumn<Demande, String> statusD;
    private Map<Integer, String> typeDonMap = new HashMap<>();
    @FXML
    private TableColumn<Demande,Void> ActionC;


    private DemandeService demandeService;

    public DemandeController() {
        demandeService = new DemandeService();
    }
    @FXML
    private void initializeDonChoiceBox() {
        // Récupérer les types de don avec leurs IDs depuis la base de données
        typeDonMap = demandeService.getAllTypesWithIds();

        // Ajouter les types de don à la ChoiceBox
        ObservableList<String> donValues = FXCollections.observableArrayList(typeDonMap.values());
        don.setItems(donValues);

        // Ajouter un écouteur d'événements pour récupérer le type de don sélectionné
        don.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int idDonSelectionne = getKeyFromValue(typeDonMap, newValue);
                System.out.println("ID du don sélectionné : " + idDonSelectionne);
                // Utilisez cet ID pour l'attribut de don dans votre demande
                // Par exemple, vous pouvez l'affecter à un attribut dans votre classe de demande
                // demande.setDonId(idDonSelectionne);
            }
        });
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
       initializeDemandeTable();
       initializeDonChoiceBox();
       // Ajouter un écouteur de sélection à la TableView
       demandeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
           if (newSelection != null) {
               // Déplacer les données de la demande sélectionnée vers les champs de texte
               titre.setText(newSelection.getTitre());
               description.setText(newSelection.getDescription());
               status.setText(newSelection.getStatut());
              // don.setValue(newSelection.getDon_id());
           } else {
               // Effacer les champs de texte si aucune demande n'est sélectionnée
               titre.clear();
               description.clear();
               status.clear();
               don.getSelectionModel().clearSelection();
           }
       });
        ActionC.setCellFactory(param -> new TableCell<>() {
            private final Button acceptButton = new Button("Accepter");
            private final Button rejectButton = new Button("Rejeter");

            {
                acceptButton.setOnAction(event -> {
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        accepterDemande(index);
                    }
                });

                rejectButton.setOnAction(event -> {
                    int index = getIndex();
                    if (index >= 0 && index < getTableView().getItems().size()) {
                        rejeterDemande(index);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, acceptButton, rejectButton);
                    setGraphic(buttons);
                }
            }
        });



    }
    private void accepterDemande(int index) {
        Demande demande = demandeTable.getItems().get(index);
        int demandeId = demande.getId_demande();
        passerARendezVous(demandeId);
    }

    private void passerARendezVous(int demandeId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/RendezVous.fxml"));
            Parent root = loader.load();
            clear.getScene().setRoot(root);
            RendezVousController rendezVousController = loader.getController();
            rendezVousController.initData(demandeId); // Passer l'ID de la demande au contrôleur

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rejeterDemande(int index) {
        Demande demande = demandeTable.getItems().get(index);
        int demandeId = demande.getId_demande();
        if (demande.getStatut().equals("REFUSEE")) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Demande déjà refusée", "Cette demande a déjà été refusée.");
        } else if (demande.getStatut().equals("DEMANDE TRAITEE")) {
            showAlert(Alert.AlertType.WARNING, "Avertissement", "Demande déjà acceptée", "Cette demande a déjà été acceptée.");
        } else {
            // Appelle la méthode Rejetee du service pour rejeter la demande dans la base de données
            demandeService.Rejetee(demandeId);
loadDemandes();
            // Désactive le bouton "Accepter" pour cette demande
            for (Node node : demandeTable.lookupAll(".table-cell")) {
                if (node instanceof TableCell && ((TableCell<?, ?>) node).getIndex() == index) {
                    TableCell<Demande, ?> cell = (TableCell<Demande, ?>) node;
                    for (Node buttonNode : cell.getChildrenUnmodifiable()) {
                        if (buttonNode instanceof Button && ((Button) buttonNode).getText().equals("Accepter")) {
                            ((Button) buttonNode).setDisable(true);
                            break;
                        }
                    }
                    break;
                }
            }

            // Affiche un avertissement pour informer l'utilisateur que la demande a été refusée
            showAlert(Alert.AlertType.WARNING, "Demande rejetée", "Demande rejetée", "La demande a été refusée.");
        }
    }


    private void initializeDemandeTable() {
        // Associer les propriétés aux colonnes de la TableView
        id_demande.setCellValueFactory(new PropertyValueFactory<>("id_demande"));
        titreD.setCellValueFactory(new PropertyValueFactory<>("titre"));
        descriptionD.setCellValueFactory(new PropertyValueFactory<>("description"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusD.setCellValueFactory(new PropertyValueFactory<>("statut"));
        donD.setCellValueFactory(new PropertyValueFactory<>("don_id"));

        // Charger les données dans la TableView
        loadDemandes();


        // Ajouter un écouteur d'événements pour mettre à jour le texte de la ChoiceBox lorsque la sélection de la TableView change
        demandeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int idDonSelectionne = newValue.getDon_id(); // Récupérer l'ID du don à partir de la demande sélectionnée
                String typeDon = getTypeDonById(idDonSelectionne); // Récupérer le type de don correspondant à partir de l'ID
                if (typeDon != null) {
                    don.setValue(typeDon); // Définir le texte de la ChoiceBox sur le type de don
                }
            }
        });
    }
    private String getTypeDonById(int idDon) {
        for (Map.Entry<Integer, String> entry : typeDonMap.entrySet()) {
            if (entry.getKey() == idDon) {
                return entry.getValue();
            }
        }
        return null; // Retourne null si aucun type de don correspondant n'est trouvé
    }





    private void loadDemandes() {
        List<Demande> demandes = demandeService.getAll();
        ObservableList<Demande> observableDemandes = FXCollections.observableArrayList(demandes);
        demandeTable.setItems(observableDemandes);
    }
    @FXML
    protected void handleAddDemande() {
        String donType = don.getValue(); // Récupère le type de don depuis le choix de l'utilisateur
        int donId = getKeyFromValue(typeDonMap, donType); // Récupère l'ID du don correspondant au type sélectionné

        if (donId != -1) { // Vérifie si l'ID du don est valide
            String descriptionText = description.getText(); // Récupère la description depuis la zone de texte
            String titreText = titre.getText();
            Demande nouvelleDemande = new Demande(donId, new Date(), descriptionText, titreText, 1);
            // Ajoutez la nouvelle demande via le service
            demandeService.add(nouvelleDemande);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Demande Ajout", "La demande a été ajoutée avec succès.");

            // Actualisez le tableau des demandes
            initializeDemandeTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Ajout de demande", "Veuillez sélectionner un type de don valide.");
        }
    }
    @FXML
    void PageQrCode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/RendezVous.fxml"));
            Parent root=loader.load();
            clear.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void handleDeleteDemande() {
        // Récupérer la demande sélectionnée dans la TableView
        Demande demandeSelectionnee = demandeTable.getSelectionModel().getSelectedItem();

        if (demandeSelectionnee != null) {
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
                demandeService.delete(demandeSelectionnee.getId_demande());

                // Actualiser la TableView
                loadDemandes();
            }
        } else {
            // Aucune demande sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une demande à supprimer.");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleUpdateDemande() {
        Demande demandeSelectionnee = demandeTable.getSelectionModel().getSelectedItem();
        if (demandeSelectionnee != null) {
            // Récupérer les nouvelles valeurs des champs
            String newTitre = titre.getText();
            String newDescription = description.getText();
            String newStatus = status.getText();
            String donType = don.getValue(); // Récupère le type de don depuis le choix de l'utilisateur
            int donId = getKeyFromValue(typeDonMap, donType);
            Demande demande = new Demande(donId, new Date(), newDescription, newTitre, 1);
            demandeService.update(demande,demandeSelectionnee.getId_demande());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Demande mise à jour", "La demande a été mise à jour avec succès.");
            initializeDemandeTable();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucune demande sélectionnée", "Veuillez sélectionner une demande à mettre à jour.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


}
