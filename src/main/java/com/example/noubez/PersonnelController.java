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
import java.util.List;
import javafx.scene.control.ChoiceBox;
public class PersonnelController  {
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
    private TableView<?> personnelTableView;
    @FXML
    private Button SupprimerPersonnelBtn;
    @FXML
    private TableColumn<?, ?> DisponibilitePersonnelColumn;
    @FXML
    private TableColumn<?, ?> ExperiencePersonnelColumn;
    @FXML
    private TableColumn<?, ?> NomPersonnelColumn;

    @FXML
    private TableColumn<?, ?> PrenomPersonnelColumn;
    @FXML
    private TableColumn<?, ?> RatingPersonnelColumn;

    @FXML
    private TableColumn<?, ?> RolePersonnelColumn;
    @FXML
    private TableColumn<?, ?> idPersonnelColumn;
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



        String imageURL=personnel.getImage();
        if (imageURL != null && !imageURL.isEmpty()) {
            Image image = new Image(imageURL);
            this.imageView.setImage(image);
        }


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


 personnelTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if(newValue!=null)
        {
            this.populateFields(newValue);
        }
        if (newValue != null) {
            // Get the image URL from the selected Personnel object
            String imageUrl = newValue.getimage();
            // Load the image into the ImageView
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Image image = new Image(imageUrl);
                imageView.setImage(image);
            } else {
                // If image URL is null or empty, clear the ImageView
                imageView.setImage(null);
            }
        }

    });
            this.configureTableView();
            this.refreshTableView();
}

    private void configureTableView() {
        this.idPersonnelColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.NomPersonnelColumn.setCellValueFactory(new PropertyValueFactory("Nom"));
        this.DisponibilitePersonnelColumn.setCellValueFactory(new PropertyValueFactory("Disponibilite"));
        this.PrenomPersonnelColumn.setCellValueFactory(new PropertyValueFactory("PrenomPersonnel"));
        this.ExperiencePersonnelColumn.setCellValueFactory(new PropertyValueFactory("Experience"));
        this.RatingPersonnelColumn.setCellValueFactory(new PropertyValueFactory("Rating"));
    }
    private void refreshTableView() {
        List<Personnel> personnelList = PersonnelService.getAll();
        ObservableList<Personnel> personnelObservableList = FXCollections.observableArrayList(Personnel);
        this.personnelTableView.setItems(personnelObservableList);
    }

    @FXML
    void handleAjouterPersonnel(ActionEvent event) {

        String Nom = this.Nom.getText();

        if (Nom.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un Nom");
            alert.show();
            return;
        }


        String Role = RolePersonnelColumn.getText();

        if (Role.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un role.");
            alert.show();
            return;
        }

        Integer experience = Integer.parseInt(experiencePersonnel.getText());

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
