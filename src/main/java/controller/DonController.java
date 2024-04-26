package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDate;
import entite.Don;
import entite.Campagne;
import service.DonService;
import service.CampagneService;
import java.sql.SQLException;
import java.util.List;
import javafx.util.StringConverter;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.stream.Collectors;


public class DonController {

    @FXML private TextField idField;
    @FXML private TextField typeField;
    @FXML private TextField montantField;
    @FXML private DatePicker dateRemiseField;
    @FXML private TableView<Don> tableView;
    @FXML private TableColumn<Don, Integer> colId;
    @FXML private TableColumn<Don, String> colType;
    @FXML private TableColumn<Don, Integer> colMontant;
    @FXML private TableColumn<Don, String> colDateRemise;
    @FXML private TableColumn<Don, Integer> colCampagneId;
    @FXML private ComboBox<Campagne> campagneComboBox;
    @FXML
    private TextField searchField;

    private DonService donService = new DonService();
    private CampagneService campagneService = new CampagneService();

    @FXML
    public void initialize() {
        loadCampagnes();
        // Initialize table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colDateRemise.setCellValueFactory(new PropertyValueFactory<>("date_remise"));
        colCampagneId.setCellValueFactory(new PropertyValueFactory<>("campagne_id"));

        // Load initial data into the table
        loadTableData();

        // Setup selection listener to populate the form when a row is clicked
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateForm(newSelection);
            }
        });

        searchTimeline.setCycleCount(1);
        searchTimeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), evt -> filterTable()));
        searchTimeline.setAutoReverse(false);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchTimeline.stop(); // Stop any running delay
            searchTimeline.playFromStart(); // Restart the delay
        });
    }



    // Use a Timeline for delaying search activation (throttling)
    private Timeline searchTimeline = new Timeline();
    @FXML
    private void filterTable() {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            tableView.setItems(FXCollections.observableArrayList(donService.findAll()));
            return;
        }

        ObservableList<Don> filteredList = tableView.getItems().stream()
                .filter(don -> don.getType().toLowerCase().contains(searchText) ||
                        String.valueOf(don.getMontant()).contains(searchText) ||
                        don.getDate_remise().toString().contains(searchText) ||
                        (don.getCampagne_id() != null && String.valueOf(don.getCampagne_id()).contains(searchText)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        tableView.setItems(filteredList);
    }

    private void loadCampagnes() {
        List<Campagne> campagnes = campagneService.findAll();
        campagneComboBox.setItems(FXCollections.observableArrayList(campagnes));
        campagneComboBox.setConverter(new StringConverter<Campagne>() {
            @Override
            public String toString(Campagne campagne) {
                return campagne == null ? null : campagne.getTitre();
            }

            @Override
            public Campagne fromString(String string) {
                return campagneComboBox.getItems().stream().filter(item ->
                        item.getTitre().equals(string)).findFirst().orElse(null);
            }
        });
    }

    private void populateForm(Don don) {
        if (don != null) {
            // Ensure each field is not null before setting its value
            if (idField != null) {
                idField.setText(String.valueOf(don.getId()));
            }

            if (typeField != null) {
                typeField.setText(don.getType());
            }

            if (montantField != null) {
                montantField.setText(don.getMontant() != null ? String.valueOf(don.getMontant()) : "");
            }

            if (dateRemiseField != null) {
                dateRemiseField.setValue(LocalDate.parse(don.getDate_remise()));
            }
        }
    }

    private void loadTableData() {
        ObservableList<Don> observableList = FXCollections.observableArrayList(donService.findAll());
        tableView.setItems(observableList);
    }

    @FXML
    private void insert() {
        if (!validateInput()) {
            showAlert(Alert.AlertType.WARNING,"Validation failed", "Please correct invalid fields.");
            return;
        }

        Campagne selectedCampagne = campagneComboBox.getSelectionModel().getSelectedItem();
        Integer selectedCampagneId = selectedCampagne != null ? selectedCampagne.getId() : null;

        Don don = new Don(
                typeField.getText(),
                tryParseInt(montantField.getText()),
                dateRemiseField.getValue() != null ? dateRemiseField.getValue().toString() : null,
                selectedCampagneId
        );

        Don savedDon = donService.save(don);
        if (savedDon != null) {
            tableView.getItems().add(savedDon);
            clearFields();
            showAlert(Alert.AlertType.INFORMATION,"Success", "Donation added successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR,"Error", "Failed to add donation.");
        }
    }

    @FXML
    private void update() {
        if (validateInput() && tableView.getSelectionModel().getSelectedItem() != null) {
            Don don = tableView.getSelectionModel().getSelectedItem();
            don.setType(typeField.getText());
            don.setMontant(tryParse(montantField.getText()));
            don.setDate_remise(dateRemiseField.getValue() != null ? dateRemiseField.getValue().toString() : null);

            Campagne selectedCampagne = campagneComboBox.getSelectionModel().getSelectedItem();
            don.setCampagne_id(selectedCampagne != null ? selectedCampagne.getId() : null);

            Don updatedDon = donService.update(don);
            if (updatedDon != null) {
                tableView.refresh();
                showAlert(Alert.AlertType.INFORMATION,"Success", "Donation updated successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR,"Error", "Failed to update donation.");
            }
        }
    }



    private Integer tryParse(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @FXML
    private void delete() {
        int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Don don = tableView.getItems().get(selectedIndex);
            try {
                donService.deleteById(don.getId());
                tableView.getItems().remove(selectedIndex);
                showAlert(Alert.AlertType.INFORMATION,"Success", "Donation deleted successfully.");
            } catch (RuntimeException e) {
                showAlert(Alert.AlertType.ERROR,"Error", "Failed to delete donation: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.ERROR,"Error", "No selection made.");
        }
    }

    @FXML
    private void clear() {
        clearFields();
        tableView.getSelectionModel().clearSelection();
    }


    // Méthode pour tenter de convertir un String en Integer
    private Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Méthode pour valider les entrées utilisateur
    private boolean validateInput() {
        String errorMessage = "";
        if (typeField.getText().isEmpty()) {
            errorMessage += "Type cannot be empty.\n";
        }
        if (dateRemiseField.getValue() == null) {
            errorMessage += "Date Remise is required.\n";
        } else if (dateRemiseField.getValue().isBefore(LocalDate.now())) {
            errorMessage += "Date Remise must be in the future.\n";
        }

        if (!montantField.getText().isEmpty()) {
            Integer montant = tryParse(montantField.getText());
            if (montant != null && montant <= 0) {
                errorMessage += "Montant must be positive if provided.\n";
            }
        }

        if (!errorMessage.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,"Invalid Input", errorMessage);
            return false;
        }
        return true;
    }


    ////showwwww alertsssss

    // Helper method to show alerts with dynamic alert types
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    // Méthode pour nettoyer les champs
    private void clearFields() {
        // Check each field for null before attempting to clear to avoid NullPointerExceptions
        if (idField != null) {
            idField.clear();
        } else {
            System.out.println("idField is null, cannot clear.");
        }

        if (typeField != null) {
            typeField.clear();
        } else {
            System.out.println("typeField is null, cannot clear.");
        }

        if (montantField != null) {
            montantField.clear();
        } else {
            System.out.println("montantField is null, cannot clear.");
        }

        if (dateRemiseField != null) {
            dateRemiseField.setValue(null);
        } else {
            System.out.println("dateRemiseField is null, cannot clear.");
        }

        // Check if campagneComboBox is not null before clearing its selection
        if (campagneComboBox != null) {
            campagneComboBox.getSelectionModel().clearSelection();
        } else {
            System.out.println("campagneComboBox is null, cannot clear selection.");
        }
    }


}
