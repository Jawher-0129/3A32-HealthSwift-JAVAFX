package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import entite.Campagne;
import service.CampagneService;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CampagneFormController {

    @FXML private TextField titreField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker debutDatePicker;
    @FXML private DatePicker finDatePicker;
    @FXML private TextField imageField;
    @FXML private ImageView imageView;
    @FXML private Button boutonEnregistrer;
    @FXML private Label labelTitre;

    private CampagneService service;
    private Campagne currentCampagne;

    @FXML
    public void initialize() {
        service = new CampagneService();
        if (currentCampagne != null) {
            loadCampagneDetails();
        } else {
            labelTitre.setText("Créer une Nouvelle Campagne");
        }
    }

    public void setCampagne(Campagne campagne) {
        this.currentCampagne = campagne;
        if (campagne != null) {
            loadCampagneDetails();
            labelTitre.setText("Modifier la Campagne");
        } else {
            labelTitre.setText("Créer une Nouvelle Campagne");
        }
    }

    private void loadCampagneDetails() {
        titreField.setText(currentCampagne.getTitre());
        descriptionArea.setText(currentCampagne.getDescription());
        debutDatePicker.setValue(LocalDate.parse(currentCampagne.getDate_debut(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        finDatePicker.setValue(LocalDate.parse(currentCampagne.getDate_fin(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        imageField.setText(currentCampagne.getImage());
    }

    @FXML
    private void handleSave() {
        if (currentCampagne == null) {
            currentCampagne = new Campagne(
                    titreField.getText(),
                    descriptionArea.getText(),
                    debutDatePicker.getValue().toString(),
                    finDatePicker.getValue().toString(),
                    imageField.getText()
            );
        } else {
            currentCampagne.setTitre(titreField.getText());
            currentCampagne.setDescription(descriptionArea.getText());
            currentCampagne.setDate_debut(debutDatePicker.getValue().toString());
            currentCampagne.setDate_fin(finDatePicker.getValue().toString());
            currentCampagne.setImage(imageField.getText());
        }

        if (currentCampagne.getId() == 0) {
            service.save(currentCampagne);
        } else {
            service.update(currentCampagne);
        }
        closeStage();
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image pour la campagne");
        File file = fileChooser.showOpenDialog(imageField.getScene().getWindow());
        if (file != null) {
            imageField.setText(file.getAbsolutePath());
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }

    private void closeStage() {
        Stage stage = (Stage) titreField.getScene().getWindow();
        stage.close();
    }
}
