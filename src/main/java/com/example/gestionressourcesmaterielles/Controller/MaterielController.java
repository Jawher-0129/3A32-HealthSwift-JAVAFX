package com.example.gestionressourcesmaterielles.Controller;

import com.example.gestionressourcesmaterielles.Model.Categorie;
import com.example.gestionressourcesmaterielles.Model.Materiel;
import com.example.gestionressourcesmaterielles.Service.CategorieService;
import com.example.gestionressourcesmaterielles.Service.MaterielService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MaterielController {

    private final CategorieService categorieService = new CategorieService();
    private final MaterielService materielService=new MaterielService();

    @FXML
    private Button bouttonStatMateriel;
    @FXML
    private Button choisirImageButton;

    @FXML
    private ChoiceBox<Integer> categorieChoiceBox;

    @FXML
    private ImageView imageView;

    @FXML
    private Button AjouterMaterielBtn;

    @FXML
    private TextField libelleMaterielTextField;

     @FXML
     private TextArea descriptionTextArea;

    @FXML
    private TextField prixTextField;

    @FXML
    private RadioButton disponibleRadioButton;
    @FXML
    private RadioButton nonDisponibleRadioButton;

    @FXML
    private TableView<Materiel> materielTableView;

    @FXML
    private TableColumn<Materiel, Integer> LibelleMaterielColumn;

    @FXML
    private TableColumn<Materiel, Integer> idCategorieMColumn;

    @FXML
    private TableColumn<Materiel,Integer> idMaterielColumn;

    @FXML
    private TableColumn<Materiel,Integer> disponibiliteColumn;

    @FXML
    private TableColumn<Materiel,Integer> descriptionColumn;

    @FXML
    private TableColumn<Materiel,Integer> prixColumn;


    @FXML
    private Button qrcodeBtn;

    @FXML
    private TextField rechercheTextField;

    @FXML
    private TextField imagefield;

    @FXML
    private Button rechercheButton;

    @FXML
    private Button PDFMaterielBtn;





    private void populateFields(Materiel materiel) {
        this.libelleMaterielTextField.setText(materiel.getLibelleMateriel());
        this.descriptionTextArea.setText(materiel.getDescription());
        this.prixTextField.setText(Double.toString(materiel.getPrix()));
        //this.disponibleRadioButton.setSelected(materiel.getDisponibilite() == 1);
       if(materiel.getDisponibilite()==1)
       {
           this.disponibleRadioButton.setSelected(materiel.getDisponibilite()==1);
       }
       else
           this.nonDisponibleRadioButton.setSelected(materiel.getDisponibilite()==0);



        String imageURL=materiel.getImageMateriel();
        if (imageURL != null && !imageURL.isEmpty()) {
            Image image = new Image(imageURL);
            this.imageView.setImage(image);
        }
        int idCategorie = materiel.getId_categorie();
        ObservableList<Integer> categories = categorieChoiceBox.getItems();
        if (categories.contains(idCategorie)) {
            categorieChoiceBox.setValue(idCategorie);
        }
    }


    @FXML
    void initialize() {
        List<Integer> idCategories = categorieService.afficherIdCategories();
        ObservableList<Integer> idCategoriesList = FXCollections.observableArrayList(idCategories);
        categorieChoiceBox.setItems(idCategoriesList);

        materielTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue!=null)
            {
                this.populateFields(newValue);
            }
            if (newValue != null) {
                // Get the image URL from the selected Materiel object
                String imageUrl = newValue.getImageMateriel();
                // Load the image into the ImageView
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Image image = new Image(imageUrl);
                    imageView.setImage(image);
                    imagefield.setText(imageUrl);

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
        this.idMaterielColumn.setCellValueFactory(new PropertyValueFactory("id"));
        this.idCategorieMColumn.setCellValueFactory(new PropertyValueFactory("Id_categorie"));
        this.LibelleMaterielColumn.setCellValueFactory(new PropertyValueFactory("LibelleMateriel"));
        this.disponibiliteColumn.setCellValueFactory(new PropertyValueFactory("Disponibilite"));
        this.descriptionColumn.setCellValueFactory(new PropertyValueFactory("Description"));
        this.prixColumn.setCellValueFactory(new PropertyValueFactory("Prix"));


    }

    private void refreshTableView() {
        List<Materiel> materiels = this.materielService.getAll();
        ObservableList<Materiel> materielObservableList = FXCollections.observableArrayList(materiels);
        this.materielTableView.setItems(materielObservableList);
    }

    @FXML
   public void handleChoisirImage()
    {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File selectedFile=fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
            imagefield.setText(selectedFile.toURI().toString());
        }
    }

    @FXML
    public void handleAjouterMateriel(ActionEvent event)
    {
        String libelle = this.libelleMaterielTextField.getText();

        if (libelle.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un libelle");
            alert.show();
            return;
        }

        String description = descriptionTextArea.getText();

        if (description.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer une description de materiel.");
            alert.show();
            return;
        }
        double prix = Double.parseDouble(prixTextField.getText());

        if (prix<0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez entrer un prix positif");
            alert.show();
            return;
        }


        int disponibilite = disponibleRadioButton.isSelected() ? 1 : 0;

        String imageURL = imageView.getImage() != null ? imageView.getImage().getUrl() : null;
        String imageName = imageURL != null ? new File(imageURL).getName() : null;
        String image="C:\\Users\\jawhe\\OneDrive\\Bureau\\3A32HealthSwiftIntegration (2)\\3A32HealthSwiftIntegration\\3A32HealthSwift\\public\\uploads\\"+imageName;


        System.out.println(imageName);


        if (imageURL==null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez choisir une image");
            alert.show();
            return;
        }

        Integer idCategorie = categorieChoiceBox.getValue();

        if (idCategorie == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez sélectionner une catégorie.");
            alert.show();
            return;
        }

        Materiel nouveauMateriel = new Materiel(libelle, description, disponibilite, image, prix, idCategorie);

        materielService.add(nouveauMateriel);
        System.out.println("Nouveau matériel ajouté avec succès !");

        libelleMaterielTextField.setText("");
        descriptionTextArea.setText("");
        prixTextField.setText("");
        disponibleRadioButton.setSelected(true); // Réinitialiser à Disponible
        imageView.setImage(null);
        categorieChoiceBox.getSelectionModel().clearSelection();
        refreshTableView();
    }

    @FXML
    public void handleSupprimerMateriel(ActionEvent event)
    {
        Materiel selectedMateriel = (Materiel) this.materielTableView.getSelectionModel().getSelectedItem();
        if (selectedMateriel != null) {
            try {
                this.materielService.delete(selectedMateriel.getId());
                System.out.println("Suppression effectuée");
                this.refreshTableView();
            } catch (SQLException var3) {
                System.out.println("Erreur lors de la suppression :");
            }
        }
    }

    @FXML
    public void handleModifierrMateriel(ActionEvent event)
    {
        Materiel selectedMateriel = (Materiel)this.materielTableView.getSelectionModel().getSelectedItem();
        String libelleMaterielTextField = this.libelleMaterielTextField.getText().trim();
        String descriptionTextArea = this.descriptionTextArea.getText().trim();
        double prix = Double.parseDouble(prixTextField.getText());
        int disponibilite = disponibleRadioButton.isSelected() ? 1 : 0;
        String image = imageView.getImage() != null ? imageView.getImage().getUrl() : null;

        int idCategorie = categorieChoiceBox.getValue();

        String prixtext=prixTextField.getText().trim();
        if (selectedMateriel != null && !libelleMaterielTextField.isEmpty() && !descriptionTextArea.isEmpty() && !prixtext.isEmpty()) {
            selectedMateriel.setLibelleMateriel(libelleMaterielTextField);
            selectedMateriel.setDescription(descriptionTextArea);
            selectedMateriel.setPrix(prix);
            selectedMateriel.setDisponibilite(disponibilite);
            selectedMateriel.setImageMateriel(image);
            selectedMateriel.setId_categorie(idCategorie);
            this.materielService.update(selectedMateriel, selectedMateriel.getId());
            //System.out.println("Modification effectuée");
            this.refreshTableView();
            this.libelleMaterielTextField.clear();

            this.descriptionTextArea.clear();
            this.prixTextField.clear();
            this.disponibleRadioButton.setSelected(true);
            imageView.setImage(null);
            categorieChoiceBox.getSelectionModel().clearSelection();
        } else {
            System.out.println("Veuillez sélectionner une catégorie et spécifier un nouveau libellé pour effectuer la modification.");
        }
    }

    @FXML
    void PageQrCode(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionressourcesmaterielles/QRcode.fxml"));
            Parent root=loader.load();
            qrcodeBtn.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void handleRecherche(ActionEvent event) {
        String libelleRecherche = rechercheTextField.getText().trim();
        if (!libelleRecherche.isEmpty()) {
            List<Materiel> materiels = materielService.rechercherParLibelle(libelleRecherche);
            ObservableList<Materiel> materielObservableList = FXCollections.observableArrayList(materiels);
            materielTableView.setItems(materielObservableList);
        } else {
            refreshTableView();
        }
    }

    @FXML
    void PageStatMateriel(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionressourcesmaterielles/StatMateriel.fxml"));
            Parent root=loader.load();
            qrcodeBtn.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

















}
