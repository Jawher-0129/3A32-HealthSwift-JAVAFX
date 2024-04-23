package com.example.noubez;
import com.example.noubez.Model.Chambre;
import com.example.noubez.Model.Personnel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;



import javafx.scene.control.Label;


        public class ChambreController {

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
            void handleAjouterChambre(ActionEvent event) {

            }

            @FXML
            void handleModifierChambre(ActionEvent event) {

            }

            @FXML
            void handleSupprimerChambre(ActionEvent event) {

            }

        }








