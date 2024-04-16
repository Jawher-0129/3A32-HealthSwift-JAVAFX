package Controller;

import entite.Actualite;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import util.DataSource;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.scene.control.Alert.AlertType;

public class ActualiteController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

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
        con = DataSource.getInstance().getConnection();
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
        try {
            String query = "SELECT * FROM actualite";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                int id_actualite = rs.getInt("id_actualite");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                String type_pub_cible = rs.getString("type_pub_cible");
                String theme = rs.getString("theme");

                Actualite actualite = new Actualite(id_actualite, titre, description, type_pub_cible, theme);
                actualiteList.add(actualite);
            }

            table.setItems(actualiteList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AddActualite(ActionEvent event) {
        String titre = idtitre.getText();
        String description = iddescr.getText();
        String type_pub_cible = idtype.getValue(); // Ensure the ComboBox has a selected value
        String theme = idtheme.getValue();

        if (titre != null && !titre.isEmpty() &&
                description != null && !description.isEmpty() &&
                type_pub_cible != null && !type_pub_cible.isEmpty() &&
                theme != null && !theme.isEmpty()) {
            try {
                String query = "INSERT INTO actualite (titre, description, type_pub_cible, theme) VALUES (?, ?, ?, ?)";
                st = con.prepareStatement(query);
                st.setString(1, titre);
                st.setString(2, description);
                st.setString(3, type_pub_cible);
                st.setString(4, theme);

                st.executeUpdate();

                loadData(); // Refresh table data after insertion
                showAlert(AlertType.INFORMATION, "Success", "Success", "Successfully added the data!");
            } catch (SQLException e) {
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
        idtype.getSelectionModel().clearSelection(); // Clear the selection
        idtheme.getSelectionModel().clearSelection();
        showAlert(AlertType.INFORMATION, "Success", "Success", "Cleared all fields.");
    }

    @FXML
    void DeleteActualite(ActionEvent event) {
        Actualite selectedActualite = table.getSelectionModel().getSelectedItem();
        if (selectedActualite != null) {
            int id_actualite = selectedActualite.getId_actualite();
            try {
                String query = "DELETE FROM actualite WHERE id_actualite=?";
                st = con.prepareStatement(query);
                st.setInt(1, id_actualite);
                st.executeUpdate();

                loadData(); // Refresh table data after deletion
                showAlert(AlertType.INFORMATION, "Success", "Success", "Successfully deleted the data!");
            } catch (SQLException e) {
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
            String type_pub_cible = idtype.getValue(); // Ensure the ComboBox has a selected value
            String theme = idtheme.getValue();

            if (idtitre.getText().isEmpty() || iddescr.getText().isEmpty()
                    || type_pub_cible == null || theme == null) {

                showAlert(AlertType.ERROR, "Error Message", "Error", "Please enter all required fields .");
                return;
            }

            try {
                String query = "UPDATE actualite SET titre=?, description=?, type_pub_cible=?, theme=? WHERE id_actualite=?";
                st = con.prepareStatement(query);
                st.setString(1, titre);
                st.setString(2, description);
                st.setString(3, type_pub_cible);
                st.setString(4, theme);
                st.setInt(5, id_actualite);

                st.executeUpdate();

                loadData(); // Refresh table data after update

                idtitre.clear();
                iddescr.clear();
                idtype.getSelectionModel().clearSelection();
                idtheme.getSelectionModel().clearSelection();
                showAlert(AlertType.INFORMATION, "Success", "Success", "Successfully updated the data!");
            } catch (SQLException e) {
                e.printStackTrace();

            }
        } else {

            showAlert(AlertType.ERROR, "Error Message", "Error", "Please select an item to update.");
        }
    }
    @FXML
    public void selectData() {
        Actualite actualite = table.getSelectionModel().getSelectedItem();

        int selectedIndex = table.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0)
            return;

        idtitre.setText(actualite.getTitre());
        iddescr.setText(actualite.getDescription());
        idtype.getSelectionModel().clearSelection();
        idtype.getSelectionModel().select(actualite.getType_pub_cible());
        idtheme.getSelectionModel().clearSelection();
        idtheme.getSelectionModel().select(actualite.getTheme());
    }


    private void showAlert(AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    void SearchActualite(KeyEvent event) {
        String keyword = idSearchActualite.getText().toLowerCase().trim();

        if (keyword.isEmpty()) {
            table.setItems(actualiteList); //all items kn mzelt mabditch
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

        table.setItems(filteredList); //bch yjini kn l result

    }
}
