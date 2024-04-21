package Controller;

import entite.Actualite;
import entite.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import Service.EvenementService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class EvenementController implements Initializable {
    private EvenementService evenementService;

    @FXML
    private Button GotoActualite_Btn;

    @FXML
    private Button UpdateEvent_btn;

    @FXML
    private Button addEvent_btn;

    @FXML
    private Button clearEvent_Btn;

    @FXML
    private TableColumn<Evenement, String> colDateEvent;

    @FXML
    private TableColumn<Evenement, Integer> colDureeEvent;

    @FXML
    private TableColumn<Evenement, Integer> colIdEvent;

    @FXML
    private TableColumn<Evenement, String> colImageEvent;

    @FXML
    private TableColumn<Evenement, String> colLieuEvent;

    @FXML
    private TableColumn<Evenement, String> colObjEvent;

    @FXML
    private TableColumn<Evenement, String> colTitreEvent;

    @FXML
    private TableColumn<Evenement, Integer> colActualiteId;

    @FXML
    private Button deleteEvent_btn;

    @FXML
    private TextField enent_title;

    @FXML
    private DatePicker event_date;

    @FXML
    private TextField event_duree;

    @FXML
    private Button event_importBtn;

    @FXML
    private TextField event_lieu;

    @FXML
    private TextField event_obj;

    @FXML
    private TextField chercherEvent;

    @FXML
    private ImageView imageView;

    @FXML
    private ComboBox<String> ActualiteRelated;

    @FXML
    private TableView<Evenement> tableEvent;
    private ObservableList<Evenement> evementList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        evenementService = new EvenementService();
        configureTableView();
        loadData();
        loadActualiteIDs();
    }

    private void loadData() {
        evementList = FXCollections.observableArrayList(evenementService.getAll());
        tableEvent.setItems(evementList);
    }


    private void configureTableView() {
        colIdEvent.setCellValueFactory(new PropertyValueFactory<>("id_evenement"));
        colTitreEvent.setCellValueFactory(new PropertyValueFactory<>("Titre"));
        colDateEvent.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colDureeEvent.setCellValueFactory(new PropertyValueFactory<>("Duree"));
        colLieuEvent.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colObjEvent.setCellValueFactory(new PropertyValueFactory<>("Objectif"));
        colImageEvent.setCellValueFactory(new PropertyValueFactory<>("image"));
        colActualiteId.setCellValueFactory(new PropertyValueFactory<>("id_actualite"));
    }

    @FXML
    void AddEvent(ActionEvent event) {
        // Retrieve the selected Actualite from the ComboBox
        String selectedActualite = ActualiteRelated.getSelectionModel().getSelectedItem();

        // Retrieve the Actualite ID from the database based on its title
        int actualiteId = -1; // Initialize with -1 as default value
        if (selectedActualite != null) {
            // Retrieve actualiteId from the service
            actualiteId = evenementService.getActualiteIdByTitle(selectedActualite);
        }

        // Check if all fields are filled
        if (enent_title.getText().isEmpty() || event_date.getValue() == null || event_duree.getText().isEmpty()
                || event_lieu.getText().isEmpty() || event_obj.getText().isEmpty() || imageView.getImage() == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please fill all fields and select an image.");
            return;
        }

        // Validate duree
        try {
            int dureeValue = Integer.parseInt(event_duree.getText());
            if (dureeValue < 0) {
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Duration must be a non-negative integer.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Duration must be a valid integer.");
            return;
        }

        if (event_date.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Event date must be in the future.");
            return;
        }

        // cbn fama image
        if (selectedImagePath == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please select an image.");
            return;
        }

        Evenement newEvenement = new Evenement(0, enent_title.getText(), java.sql.Date.valueOf(event_date.getValue()),
                Integer.parseInt(event_duree.getText()), event_lieu.getText(), event_obj.getText(),
                selectedImagePath, actualiteId);

        try {
            evenementService.add(newEvenement);
            showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Successfully Added!");
            ClearEvent(null);
            loadData();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "An error occurred while adding the event.");
            e.printStackTrace();
        }
    }

    @FXML
    void ClearEvent(ActionEvent event) {
        enent_title.clear();
        event_duree.clear();
        event_lieu.clear();
        event_obj.clear();
        event_date.setValue(null);
        imageView.setImage(null);
        ActualiteRelated.getSelectionModel().clearSelection();
        showAlert(Alert.AlertType.INFORMATION, "Information", "Data Cleared", "Event data has been cleared.");
    }

    @FXML
    void DeleteEvent(ActionEvent event) {
        Evenement selectedEvent = tableEvent.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please select an event to delete.");
            return;
        }

        evenementService.delete(selectedEvent.getId_evenement());

        showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Event successfully deleted!");

        ClearEvent(null);
        loadData();
    }

    @FXML
    void UpdateEvent(ActionEvent event) {
        Evenement selectedEvent = tableEvent.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please select an event to update.");
            return;
        }

        // Check if all fields are filled
        if (enent_title.getText().isEmpty() || event_date.getValue() == null || event_duree.getText().isEmpty()
                || event_lieu.getText().isEmpty() || event_obj.getText().isEmpty() || imageView.getImage() == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please fill all fields and select an image.");
            return;
        }

        // Validate duree
        try {
            int dureeValue = Integer.parseInt(event_duree.getText());
            if (dureeValue < 0) {
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Duration must be a non-negative integer.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Duration must be a valid integer.");
            return;
        }

        if (event_date.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Event date must be in the future.");
            return;
        }

        // cbn fama image
        if (selectedImagePath == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please select an image.");
            return;
        }

        // Retrieve the selected Actualite from the ComboBox
        String selectedActualite = ActualiteRelated.getSelectionModel().getSelectedItem();

        // Retrieve the Actualite ID from the database based on its title
        int actualiteId = -1; // Initialize with -1 as default value
        if (selectedActualite != null) {
            // Retrieve actualiteId from the service
            actualiteId = evenementService.getActualiteIdByTitle(selectedActualite);
        }

        Evenement updatedEvenement = new Evenement(selectedEvent.getId_evenement(), enent_title.getText(),
                java.sql.Date.valueOf(event_date.getValue()), Integer.parseInt(event_duree.getText()),
                event_lieu.getText(), event_obj.getText(), selectedImagePath, actualiteId);

        try {
            evenementService.update(updatedEvenement, selectedEvent.getId_evenement());
            showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Successfully Updated!");
            loadData();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "An error occurred while updating the event.");
            e.printStackTrace();
        }
    }



    private String selectedImagePath;

    @FXML
    void importImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Window window = ((Node) event.getTarget()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }

    @FXML
    public void selectData() {
        Evenement evenement = tableEvent.getSelectionModel().getSelectedItem();

        if (evenement == null)
            return;

        enent_title.setText(evenement.getTitre());
        event_duree.setText(String.valueOf(evenement.getDuree()));
        event_lieu.setText(evenement.getLieu());
        event_obj.setText(evenement.getObjectif());
        event_date.setValue(LocalDate.parse(String.valueOf(evenement.getDate())));

        String imagePath = evenement.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Image Not Found", "The selected image file does not exist.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Image Path Not Provided", "The image path for the selected event is not provided.");
        }

        ActualiteRelated.setValue(String.valueOf(evenement.getId_actualite()));
    }

    @FXML
    void chercherEvenement(KeyEvent event) {
        String searchValue = chercherEvent.getText();
        ObservableList<Evenement> filteredList = FXCollections.observableArrayList();

        for (Evenement evenement : evementList) {
            String titre = evenement.getTitre().toLowerCase();
            String date = evenement.getDate().toString().toLowerCase();
            String duree = String.valueOf(evenement.getDuree());
            String lieu = evenement.getLieu().toLowerCase();
            String objectif = evenement.getObjectif().toLowerCase();

            if (titre.contains(searchValue.toLowerCase()) ||
                    date.contains(searchValue.toLowerCase()) ||
                    duree.contains(searchValue.toLowerCase()) ||
                    lieu.contains(searchValue.toLowerCase()) ||
                    objectif.contains(searchValue.toLowerCase())) {
                filteredList.add(evenement);
            }
        }

        tableEvent.setItems(filteredList);
    }

    private void loadActualiteIDs() {
        ActualiteRelated.getItems().clear();
        ActualiteRelated.getItems().addAll(evenementService.getAllActualiteTitles());
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void PageActualiteBack(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/ActualiteBack.fxml"));
            Parent root = loader.load();
            // Get the scene from any node in the current scene
            Scene scene = GotoActualite_Btn.getScene();
            // Set the loaded FXML file as the root of the scene
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
