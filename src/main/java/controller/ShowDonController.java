package controller;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;
import javafx.scene.control.ButtonType;
import javafx.geometry.Insets;
import java.util.Optional;

import entite.Don;
import service.DonService;

public class ShowDonController {

    @FXML
    private VBox vbox;  // Ensure this matches fx:id in FXML
    @FXML
    private Label typeLabel, amountLabel, dateLabel;
    @FXML
    private Button updateButton, deleteButton;
    private Don currentDon;
    private DonService donService = new DonService();

    @FXML
    public void initialize() {
        // Set padding programmatically to avoid FXML load issues
        vbox.setPadding(new Insets(20, 20, 20, 20));

        // Initialization logic here
    }

    public void setDon(Don don) {
        this.currentDon = don;
        typeLabel.setText("Type: " + don.getType());
        amountLabel.setText("Montant: " + (don.getMontant() == null ? "N/A" : don.getMontant() + " €"));
        dateLabel.setText("Date de remise: " + don.getDate_remise());
        updateButton.setOnAction(e -> showDonForm(don));
        deleteButton.setOnAction(e -> deleteDon(don.getId()));
    }

    private void showDonForm(Don don) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DonForm.fxml"));
            VBox form = loader.load();
            DonFormController controller = loader.getController();
            controller.setDon(don);

            Stage stage = new Stage();
            stage.setScene(new Scene(form));
            stage.setTitle("Modifier le Don");
            stage.showAndWait();

            // Refresh data display after the form is closed
            refreshDonDisplay();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void refreshDonDisplay() {
        if (currentDon != null) {
            // Assuming DonService or similar method to fetch the latest data
            Optional<Don> optionalDon = donService.findById(currentDon.getId());
            if (optionalDon.isPresent()) {
                Don updatedDon = optionalDon.get();
                setDon(updatedDon); // Reuse the setDon method to update the UI
            }
        }
    }


    @FXML
    private void handleBack() {
        initialize(); // Ensure vbox is initialized
        if (vbox != null && vbox.getScene() != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DonView.fxml"));
                Parent previousPage = loader.load();
                vbox.getScene().setRoot(previousPage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot navigate back because vbox is not attached to any scene.");
        }
    }




    private void deleteDon(int id) {
        Alert confirmation = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this donation?");
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    donService.deleteById(id);
                    // Navigate back to DonView after successful deletion
                    handleBack();
                } catch (Exception ex) {
                    Alert errorAlert = new Alert(AlertType.ERROR, "Error deleting donation: " + ex.getMessage());
                    errorAlert.show();
                }
            }
        });
    }
}
