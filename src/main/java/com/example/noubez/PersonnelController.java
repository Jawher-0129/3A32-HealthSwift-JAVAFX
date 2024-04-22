package com.example.noubez;

import com.example.noubez.Model.Personnel;
import com.example.noubez.Service.PersonnelService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.scene.control.ChoiceBox;
public class PersonnelController{
    private final PersonnelService personnelService=new PersonnelService();

    @FXML
    private TextField RatingPersonnel;

    @FXML
    private TextField experiencePersonnel;
    @FXML
    private Label Disponibilite;

    @FXML
    private CheckBox DisponibleRadioButton;

    @FXML
    private TextField Nom;

    @FXML
    private TextField Prenom_personnel;

    @FXML
    private Button choisir;

    @FXML
    private Button ModifierPersonnelBtn;

    @FXML
    private CheckBox nonDisponibleRadioButton;

    @FXML
    private ChoiceBox<String> roleChoiceBox;
    @FXML
    private ImageView imageView;
    @FXML
    private TableColumn<Personnel, Integer> experience;

    @FXML
    private TableColumn<Personnel, Integer> idpersonnel;
    @FXML
    private TableColumn<Personnel,String> nompersonnel;
    @FXML
    private TableView<Personnel> tablePersonnel;
    @FXML
    private TableColumn<Personnel,String> prenompersonnel;


    @FXML
    private Button SupprimerPersonnelBtn;

    @FXML
    private Button AjouterPersonnelBtn;

    private void populateFields(Personnel personnel) {

        this.Nom.setText(personnel.getNom());
        this.Prenom_personnel.setText(personnel.getPrenom_personnel());
        this.experiencePersonnel.setText(Integer.toString(personnel.getExperience()));
        this.RatingPersonnel.setText(Integer.toString(personnel.getRating()));
        this.roleChoiceBox.setValue(personnel.getRole());

        //this.DisponibleRadioButton.setSelected(personnel.getDisponibilite() == 1);
        if(personnel.getDisponibilite()==1)
        {
            this.DisponibleRadioButton.setSelected(personnel.getDisponibilite()==1);
        }
        else
            this.nonDisponibleRadioButton.setSelected(personnel.getDisponibilite()==0);



       /* String imageURL=personnel.getImage();
        if (imageURL != null && !imageURL.isEmpty()) {
            Image image = new Image(imageURL);
            this.imageView.setImage(image);
        }*/


        // Populate the roleChoiceBox with options
        ObservableList<String> personnels = FXCollections.observableArrayList("Radiologie", "Chirurgie", "Infirmier", "Neurologie");
        roleChoiceBox.setItems(personnels);

        // Set up a listener for the roleChoiceBox selection
        roleChoiceBox.setOnAction(event -> {
            String selectedRole = roleChoiceBox.getValue(); // Get the selected role
            if (selectedRole != null && selectedRole.equals(personnel.getRole())) {
                roleChoiceBox.setValue(personnel.getRole());
            }
        });


    }
@FXML
void initialize(){
    configureTableView();
    loadPersonnels();
}
    private void configureTableView() {
        System.out.println("Configuration de la TableView...");
        idpersonnel.setCellValueFactory(new PropertyValueFactory<>("id_personnel"));
        nompersonnel.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenompersonnel.setCellValueFactory(new PropertyValueFactory<>("prenom_personnel"));
        experience.setCellValueFactory(new PropertyValueFactory<>("experience"));
       loadPersonnels();
    }
    private void loadPersonnels() {
        System.out.println("Chargement des personnels depuis la base de données...");
        List<Personnel> Personnels = personnelService.getAll();
        System.out.println("Nombre de personnels récupérés : " + Personnels.size());
        ObservableList<Personnel> observablePersonnels = FXCollections.observableArrayList(Personnels);
        System.out.println("Personnels chargés avec succès.");
        tablePersonnel.setItems(observablePersonnels);
    }



    @FXML
    void handleAjouterPersonnel(ActionEvent event) {

        String Nom = this.Nom.getText();
        String prenom = this.Prenom_personnel.getText();

        if (Nom.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un Nom");
            alert.show();
            return;
        }


       /* String Role = roleChoiceBox.getText();

        if (Role.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un role.");
            alert.show();
            return;
        }*/

        Integer experience = Integer.parseInt(experiencePersonnel.getText());
        Integer rating = Integer.parseInt(RatingPersonnel.getText());
        if (experience<0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer une experience positif");
            alert.show();
            return;
        }


        int disponibilite = DisponibleRadioButton.isSelected() ? 1 : 0;

        String image = imageView.getImage() != null ? imageView.getImage().getUrl() : null;

        if (image==null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez choisir une image");
            alert.show();
            return;
        }

       /* Personnel newPer = new Personnel(Nom,prenom,disponibilite,Role,experience,image, rating, 8);
        this.personnelService.add(newPer);
        this.refreshTableView();*/

    }

    @FXML
    void handleChoisirImage(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File selectedFile=fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }

    }

    @FXML
    void handleModifierPersonnel(ActionEvent event) {

    }

    @FXML
    void handleSupprimerPersonnel(ActionEvent event) {

    }



}
