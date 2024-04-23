package com.example.noubez;
import com.example.noubez.Model.Chambre;
import com.example.noubez.Model.Personnel;
import com.example.noubez.Service.ChambreService;
import com.example.noubez.Service.PersonnelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;


import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;


public class ChambreController {
    private final ChambreService chambreService = new ChambreService();

    @FXML
    private Button AjouterChambreBtn;

    @FXML
    private Label Disponibilite;

    @FXML
    private RadioButton DisponibleRadioButton;

    @FXML
    private TextField LitsDisponible;

    @FXML
    private TextField LitsTotal;

    @FXML
    private Button ModifierChambreBtn;

    @FXML
    private TextField Numero;

    @FXML
    private TableColumn<Chambre, Integer> NumeroChambre;

    @FXML
    private Button SupprimerChambreBtn;

    @FXML
    private TableColumn<Chambre, Integer> disponibilite;

    @FXML
    private TableColumn<Chambre, Integer> litsDispo;

    @FXML
    private TableColumn<Chambre, Integer> litsTotal;

    @FXML
    private RadioButton nonDisponibleRadioButton;

    @FXML
    private TableColumn<Chambre, Integer> PersonnelChambre;

    @FXML
    private TableView<Chambre> tableChambre;
    /*
    @FXML
    void initialize() {
        configureTableView();
        initializeDemandeChoiceBox();
        // Ajouter un écouteur de sélection à la TableView
        tableChambre.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Déplacer les données de la demande sélectionnée vers les champs de texte
                Numero.setText(newSelection.getNumero());
                personnelChambre.setText(newSelection.getPrenom_personnel());
                Disponibilite.setText(newSelection.getdisponibilite());
                LitsTotal.setText(String.valueOf(newSelection.getExperience()));
                LitsDisponible.setText(String.valueOf(newSelection.getRating()));

                // don.setValue(newSelection.getDon_id());
            } else {
                // Effacer les champs de texte si aucun case n'est sélectionnée
                Numero.clear();
                Prenom_personnel.clear();
                experiencePersonnel.clear();
                roleChoiceBox.getSelectionModel().clearSelection();
            }
        });


    }

    private void configureTableView() {
        System.out.println("Configuration de la TableView...");
        NumeroChambre.setCellValueFactory(new PropertyValueFactory<>("numero"));
        PersonnelChambre.setCellValueFactory(new PropertyValueFactory<>("personnel"));
        disponibilite.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        litsTotal.setCellValueFactory(new PropertyValueFactory<>("nombre_lits_total"));
        litsDispo.setCellValueFactory(new PropertyValueFactory<>("nmbr_lits_disponible"));


        loadChambres();
    }

    private void loadChambres() {
        System.out.println("Chargement des chambres depuis la base de données...");
        List<Chambre> Chambres = ChambreService.getAll();
        System.out.println("Nombre de personnels récupérés : " + Chambres.size());
        ObservableList<Personnel> observablePersonnels = FXCollections.observableArrayList(Chambres);
        System.out.println("Personnels chargés avec succès.");
        tableChambre.setItems(observableChambres);
    }

//

    @FXML
            void handleAjouterChambre(ActionEvent event) {

            }

            @FXML
            void handleModifierChambre(ActionEvent event) {

            }

            @FXML
            void handleSupprimerChambre(ActionEvent event) {

            }

        }

     */
}








