package com.example.gestionressourcesmaterielles.Controller;
import javafx.scene.chart.PieChart;

import com.example.gestionressourcesmaterielles.Model.Categorie;
import com.example.gestionressourcesmaterielles.Model.Materiel;
import com.example.gestionressourcesmaterielles.Service.CategorieService;
import com.example.gestionressourcesmaterielles.Service.MaterielService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import com.itextpdf.layout.element.Cell;

import com.itextpdf.io.image.ImageDataFactory;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MaterielController implements Initializable {
    private final CategorieService categorieService = new CategorieService();
    private final MaterielService materielService=new MaterielService();

    @FXML
    private Button bouttonStatMateriel;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    private RadioButton TriCroissantMateriel;

    @FXML
    private RadioButton TriDecroissantMateriel;

    private MainController mainController;
    @FXML
    private Button choisirImageButton;

    @FXML
    private ChoiceBox<String> categorieChoiceBox;

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
    private Button buttonMaterielAdmin;

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
    private Button buttonCategorieAdmin;

    @FXML
    private TableColumn<Materiel,Integer> prixColumn;


    @FXML
    private Button qrcodeBtn;

    @FXML
    private TextField rechercheTextField;

    @FXML
    private Button rechercheButton;

    @FXML
    private Button buttonPDFMateriel;


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

        String libelleCategorie=categorieService.getLibelleCategorieParId(idCategorie);

        ObservableList<String> categories = categorieChoiceBox.getItems();
        if (libelleCategorie != null) {
            categorieChoiceBox.setValue(libelleCategorie);
        }
            String data = materiel.getLibelleMateriel() + "\n"
                    + materiel.getDescription() + "\n"+ materiel.getPrix()+"\n"+materiel.getId_categorie()+"\n"+materiel.getDisponibilite();
            // Set the size of the QR code image
            int width = 300;
            int height = 300;
            try {
                // Generate the QR code
                BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
                // Convert the BitMatrix to an Image
                Image qrCodeImage = toFXImage(bitMatrix);

                // Display the QR code image in the ImageView
                qrCodeImageView.setImage(qrCodeImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
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
        }
    }

    @FXML
    public void handleAjouterMateriel(ActionEvent event)
    {
        String libelle = this.libelleMaterielTextField.getText();

        if (libelle.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez choisir une image");
            alert.show();
            return;
        }

        String categorieLibelle=categorieChoiceBox.getValue();

        if(categorieLibelle==null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Erreur : Veuillez sélectionner une catégorie.");
            alert.show();
            return;
        }

       Integer idCategorie=categorieService.getIdCategorieParLibelle(categorieLibelle);

        Materiel nouveauMateriel = new Materiel(libelle, description, disponibilite, image, prix, idCategorie);

        materielService.add(nouveauMateriel);
        //System.out.println("Nouveau matériel ajouté avec succès !");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Nouveau matériel ajouté avec succès !");
        String msg="Bonjour Mr(Mme) Jawher Nous vous informe qu'un nouveau produits a été ajoutée à notre centre c'est "+nouveauMateriel.getLibelleMateriel();
        materielService.envoyerSMS("+21629036051",msg);
        alert.showAndWait();

        libelleMaterielTextField.setText("");
        descriptionTextArea.setText("");
        prixTextField.setText("");
        disponibleRadioButton.setSelected(true);
        imageView.setImage(null);
       // categorieChoiceBox.getSelectionModel().clearSelection();
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
        //String image = imageView.getImage() != null ? imageView.getImage().getUrl() : null;

          String imageURL = imageView.getImage() != null ? imageView.getImage().getUrl() : null;
        String imageName = imageURL != null ? new File(imageURL).getName() : null;
        String image="C:\\Users\\jawhe\\OneDrive\\Bureau\\3A32HealthSwiftIntegration (2)\\3A32HealthSwiftIntegration\\3A32HealthSwift\\public\\uploads\\"+imageName;

        //int idCategorie = categorieChoiceBox.getValue();
        String libelleCategorie=categorieChoiceBox.getValue();
        int idCategorie=categorieService.getIdCategorieParLibelle(libelleCategorie);

        String prixtext=prixTextField.getText().trim();
        if (selectedMateriel != null) {

            if (libelleMaterielTextField.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Erreur : Veuillez entrer un libelle");
                alert.show();
                return;
            }

            if (descriptionTextArea.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Erreur : Veuillez entrer une description de materiel.");
                alert.show();
                return;
            }

            if (prix<0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Erreur : Veuillez entrer un prix positif");
                alert.show();
                return;
            }

            selectedMateriel.setLibelleMateriel(libelleMaterielTextField);
            selectedMateriel.setDescription(descriptionTextArea);
            selectedMateriel.setPrix(prix);
            selectedMateriel.setDisponibilite(disponibilite);
            selectedMateriel.setImageMateriel(image);

            selectedMateriel.setId_categorie(idCategorie);
            this.materielService.update(selectedMateriel, selectedMateriel.getId());
            System.out.println("Modification effectuée");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Modification effectuée !");
            alert.showAndWait();
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
    public void initMainController(MainController mainController) {
        this.mainController = mainController;
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
    void PageStatMateriel(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionressourcesmaterielles/StatMateriel.fxml"));
            Parent stat=loader.load();
            // Create the dialog
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(bouttonStatMateriel.getScene().getWindow());
            Scene scene = new Scene(stat);
            dialogStage.setScene(scene);

            // Show the dialog
            dialogStage.showAndWait();

           // bouttonStatMateriel.getScene().setRoot(stat);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void GenererPdfMateriel(ActionEvent event) {
        try (PdfWriter writer = new PdfWriter("materiels.pdf");
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {
            document.add(new Paragraph("Liste des Matériels"));
            Table table = new Table(6);
            table.addCell("ID");
            table.addCell("Libellé");
            table.addCell("Description");
            table.addCell("Disponibilité");
            table.addCell("Prix");

            // Récupération des données des matériels depuis la TableView
            ObservableList<Materiel> materiels = materielTableView.getItems();
            for (Materiel materiel : materiels) {
                table.addCell(String.valueOf(materiel.getId()));
                table.addCell(materiel.getLibelleMateriel());
                table.addCell(materiel.getDescription());
                table.addCell(materiel.getDisponibilite() == 1 ? "Disponible" : "Non disponible");
                table.addCell(String.valueOf(materiel.getPrix()));
            }

            document.add(table);

            System.out.println("PDF généré avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<String> CategoriesLibelle = categorieService.afficherLibellesCategories();
        ObservableList<String> idCategoriesList = FXCollections.observableArrayList(CategoriesLibelle);
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

                } else {
                    // If image URL is null or empty, clear the ImageView
                    imageView.setImage(null);
                }
            }

        });
        this.configureTableView();
        this.refreshTableView();
    }

    private Image toFXImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean bit = bitMatrix.get(x, y);
                Color color = bit ? Color.BLACK : Color.WHITE;
                pixelWriter.setColor(x, y, color);
            }
        }

        return writableImage;
    }


    @FXML
    void TriCroissantMateriel(ActionEvent event) {
        if(TriDecroissantMateriel.isSelected())
        {
            TriDecroissantMateriel.setSelected(false);
        }


        ObservableList<Materiel> materiels = materielTableView.getItems();

        // Créer un Comparator pour trier les objets Materiel selon le prix dans l'ordre croissant
        Comparator<Materiel> comparator = Comparator.comparingDouble(Materiel::getPrix);

        // Trier la liste de matériel dans l'ordre croissant selon le prix
        Collections.sort(materiels, comparator);

        // Mettre à jour la TableView avec la nouvelle liste triée
        materielTableView.setItems(materiels);


    }

    @FXML
    void TriDecroissantMateriel(ActionEvent event) {

        if(TriCroissantMateriel.isSelected())
        {
            TriCroissantMateriel.setSelected(false);
        }

        ObservableList<Materiel> materiels = materielTableView.getItems();

        // Créer un Comparator pour trier les objets Materiel selon le prix dans l'ordre décroissant
        Comparator<Materiel> comparator = Comparator.comparingDouble(Materiel::getPrix).reversed();

        // Trier la liste de matériel dans l'ordre décroissant selon le prix
        Collections.sort(materiels, comparator);

        // Mettre à jour la TableView avec la nouvelle liste triée
        materielTableView.setItems(materiels);

    }


}
