package Controller;

import Service.ActualiteService;
import entite.Actualite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ActualiteController implements Initializable {
    private ActualiteService actualiteService = new ActualiteService();
    @FXML
    private Button GotoEvent_Btn;

    @FXML
    private TextField iddescr;

    @FXML
    private ComboBox<String> idtheme;

    @FXML
    private TextField idtitre;

    @FXML
    private TextField idSearchActualite;

    @FXML
    private ComboBox<String> idtype;

    @FXML
    private TableView<Actualite> table;

    @FXML
    private TableColumn<Actualite, Integer> colId;

    @FXML
    private TableColumn<Actualite, String> colTitre;

    @FXML
    private TableColumn<Actualite, String> colDescription;

    @FXML
    private TableColumn<Actualite, String> colTypepubcible;

    @FXML
    private TableColumn<Actualite, String> colTheme;

    private ObservableList<Actualite> actualiteList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureTableView();
        loadData();
    }

    private void configureTableView() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id_actualite"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colTypepubcible.setCellValueFactory(new PropertyValueFactory<>("type_pub_cible"));
        colTheme.setCellValueFactory(new PropertyValueFactory<>("theme"));
    }

    private void loadData() {
        actualiteList.clear();
        actualiteList.addAll(actualiteService.getAll());
        table.setItems(actualiteList);
    }

    @FXML
    void AddActualite(ActionEvent event) {
        String titre = idtitre.getText();
        String description = iddescr.getText();
        String type_pub_cible = idtype.getValue();
        String theme = idtheme.getValue();

        if (titre != null && !titre.isEmpty() &&
                description != null && !description.isEmpty() &&
                type_pub_cible != null && !type_pub_cible.isEmpty() &&
                theme != null && !theme.isEmpty()) {
            try {
                Actualite newActualite = new Actualite(0, titre, description, type_pub_cible, theme);
                actualiteService.add(newActualite);
                loadData();
                showAlert(AlertType.INFORMATION, "Success", "Success", "Successfully added the data!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.ERROR, "Error Message", "Error", "Please fill in all fields.");
        }
    }

    @FXML
    void ClearActualite(ActionEvent event) {
        idtitre.clear();
        iddescr.clear();
        idtype.getSelectionModel().clearSelection();
        idtheme.getSelectionModel().clearSelection();
        showAlert(AlertType.INFORMATION, "Success", "Success", "Cleared all fields.");
    }

    @FXML
    void DeleteActualite(ActionEvent event) {
        Actualite selectedActualite = table.getSelectionModel().getSelectedItem();
        if (selectedActualite != null) {
            int id_actualite = selectedActualite.getId_actualite();
            try {
                actualiteService.delete(id_actualite);
                loadData();
                showAlert(AlertType.INFORMATION, "Success", "Success", "Successfully deleted the data!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.ERROR, "Error Message", "Error", "Please select an item to delete.");
        }
    }

    @FXML
    void UpdateActualite(ActionEvent event) {
        Actualite selectedActualite = table.getSelectionModel().getSelectedItem();
        if (selectedActualite != null) {
            int id_actualite = selectedActualite.getId_actualite();
            String titre = idtitre.getText();
            String description = iddescr.getText();
            String type_pub_cible = idtype.getValue();
            String theme = idtheme.getValue();

            if (titre.isEmpty() || description.isEmpty() ||
                    type_pub_cible == null || theme == null) {
                showAlert(AlertType.ERROR, "Error Message", "Error", "Please enter all required fields.");
                return;
            }

            try {
                Actualite updatedActualite = new Actualite(id_actualite, titre, description, type_pub_cible, theme);
                actualiteService.update(updatedActualite, id_actualite);
                loadData();
                idtitre.clear();
                iddescr.clear();
                idtype.getSelectionModel().clearSelection();
                idtheme.getSelectionModel().clearSelection();
                showAlert(AlertType.INFORMATION, "Success", "Success", "Successfully updated the data!");
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(AlertType.ERROR, "Error Message", "Error", "Please select an item to update.");
        }
    }

    @FXML
    public void selectData() {
        Actualite actualite = table.getSelectionModel().getSelectedItem();

        if (actualite != null) {
            idtitre.setText(actualite.getTitre());
            iddescr.setText(actualite.getDescription());
            idtype.getSelectionModel().select(actualite.getType_pub_cible());
            idtheme.getSelectionModel().select(actualite.getTheme());
        }
    }

    @FXML
    void SearchActualite(KeyEvent event) {
        String keyword = idSearchActualite.getText().toLowerCase().trim();

        if (keyword.isEmpty()) {
            table.setItems(actualiteList);
            return;
        }

        ObservableList<Actualite> filteredList = FXCollections.observableArrayList();

        for (Actualite actualite : actualiteList) {
            if (actualite.getTitre().toLowerCase().contains(keyword) ||
                    actualite.getDescription().toLowerCase().contains(keyword) ||
                    actualite.getType_pub_cible().toLowerCase().contains(keyword) ||
                    actualite.getTheme().toLowerCase().contains(keyword)) {
                filteredList.add(actualite);
            }
        }

        table.setItems(filteredList);
    }

    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void PageEventBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/EvenementBack.fxml"));
            Parent root = loader.load();
            // Get the scene from any node in the current scene
            Scene scene = GotoEvent_Btn.getScene();
            // Set the loaded FXML file as the root of the scene
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
