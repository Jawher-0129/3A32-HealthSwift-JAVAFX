package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import entite.Campagne;
import service.CampagneService;
import javafx.geometry.Insets;
import java.util.Optional;
import javafx.scene.Parent;

public class ShowCampagneController {

    @FXML private VBox detailsBox;
    @FXML private Label titleLabel, titleValue, descriptionLabel, descriptionValue, startDateLabel, startDateValue, endDateLabel, endDateValue;
    @FXML private ImageView imageView;
    @FXML private Button modifyButton, deleteButton;


    private CampagneService service = new CampagneService();
    private Campagne currentCampagne;

    @FXML
    public void initialize() {    detailsBox.setPadding(new Insets(20));
    }
    public void setCampagne(Campagne campagne) {
        this.currentCampagne = campagne;
        titleValue.setText(campagne.getTitre());
        descriptionValue.setText(campagne.getDescription());
        startDateValue.setText(campagne.getDate_debut());
        endDateValue.setText(campagne.getDate_fin());
        if (campagne.getImage() != null && !campagne.getImage().isEmpty()) {
            imageView.setImage(new Image("file:" + campagne.getImage()));
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);
        }
    }
    @FXML
    private void handleBack() {
        // Logic to navigate back goes here
        // For example, if you want to set the scene's root to the previous page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CampagneView.fxml")); // replace with your actual previous page FXML
            Parent previousPage = loader.load();
            detailsBox.getScene().setRoot(previousPage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleModify() {
        if (currentCampagne != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CampagneForm.fxml"));
                ScrollPane root = (ScrollPane) loader.load();  // Cast to ScrollPane instead of VBox
                CampagneFormController controller = loader.getController();
                controller.setCampagne(currentCampagne);

                Stage stage = new Stage();
                stage.setScene(new Scene(root)); // Set the scene with ScrollPane
                stage.setTitle("Modifier la Campagne");
                stage.showAndWait(); // Wait for the form to close

                setCampagne(currentCampagne); // Optionally refresh the displayed campagne details
            } catch (IOException e) {
                e.printStackTrace();
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Error opening the edit form: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (currentCampagne != null && currentCampagne.getId() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this campaign?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                service.deleteById(currentCampagne.getId());
                closeStage(); // Close the stage after deletion
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No valid campaign is loaded for deletion.");
            alert.showAndWait();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) detailsBox.getScene().getWindow();
        stage.close();
    }
}
