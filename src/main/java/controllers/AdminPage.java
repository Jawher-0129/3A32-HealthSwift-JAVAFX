package controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import service.Usercrud;
import javafx.event.ActionEvent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

import Entity.User;

import javafx.scene.control.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import java.util.Comparator;
import javafx.beans.property.SimpleIntegerProperty;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
public class AdminPage {

    @FXML
    private PieChart pieChart;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    @FXML
    private Label NameUser;
    @FXML
    private Label RoleUser;
    @FXML
    private Button statisticsButton;

    @FXML
    private TableView<User> UserTable;
    @FXML
    private TableColumn<User, Integer> id;

    @FXML
    private TableColumn<User, String> Nom;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<User, String> Prenom;

    @FXML
    private TableColumn<User, String> Email;

    @FXML
    private TableColumn<User, String> Adresse;

    @FXML
    private TableColumn<User, String> Telephone;

    @FXML
    private TableColumn<User, String> Roles;

    @FXML
    void initialize() {

        // Retrieve the current user from the session
        User currentUser = SessionManager.getCurrentUser();

        // Display the name and role of the connected user
        NameUser.setText("Welcome Back : " + currentUser.getNom());

        // Set up the TableView columns
        id.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());

        Nom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        Prenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        Email.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        Adresse.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        Telephone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        Roles.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRoles()));

        // Populate the TableView with users
        loadUsers();


    }
    @FXML
    void search() {
        // Obtenez les données de la table
        ObservableList<User> userList = UserTable.getItems();

        // Créez un FilteredList initialisé avec toutes les données
        FilteredList<User> filteredList = new FilteredList<>(userList, p -> true);

        // Ajoutez un listener pour le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    // Si la recherche est vide, affichez tous les éléments
                    return true;
                }

                // Comparez chaque attribut de l'utilisateur avec la valeur de recherche
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getNom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance trouvée dans le nom
                } else if (user.getPrenom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance trouvée dans le prénom
                } else if (user.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance trouvée dans l'email
                } else if (user.getAdresse().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance trouvée dans l'adresse
                } else if (user.getTelephone().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance trouvée dans le téléphone
                } else if (user.getRoles().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Correspondance trouvée dans les rôles
                }

                return false; // Aucune correspondance trouvée
            });
        });

        // Affichez les données filtrées dans la table
        UserTable.setItems(filteredList);
    }
    // Method to load users into the TableView
    private void loadUsers() {
        Usercrud userCrud = new Usercrud();
        ObservableList<User> userList = FXCollections.observableArrayList(userCrud.getAllData());
        userList.removeIf(user -> user.getRoles().equalsIgnoreCase("Admin"));

        UserTable.setItems(userList);
    }
    private void switchScene(String fxmlFile, ActionEvent event) {
        try {
            System.out.println("fxml:"+ fxmlFile);

            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void EditProfile(ActionEvent event) {
        switchScene("/EditProfile.fxml", event);

    }


    @FXML
    void delete(ActionEvent event) {
        // Get the selected user from the table
        User selectedUser = UserTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Confirm deletion with a dialog box
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete User");
            alert.setContentText("Are you sure you want to delete the selected user?");

            // Handle user's choice
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User confirmed deletion
                    Usercrud usercrud = new Usercrud();
                    usercrud.deleteUser(selectedUser);
                    // Remove the user from the table
                    UserTable.getItems().remove(selectedUser);

                }
            });
        } else {
            // No user selected, show an error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Selection");
            alert.setHeaderText("No User Selected");
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }
    @FXML
    void logout(ActionEvent event) {
        // Confirm logout with a dialog box
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Logout");
        alert.setHeaderText("Logout");
        alert.setContentText("Are you sure you want to log out?");

        // Handle user's choice
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User confirmed logout
                SessionManager.clearSession(); // Clear the session
                // Close the current window (assuming this is the only stage)
                Stage stage = (Stage) NameUser.getScene().getWindow();
                stage.close();
                // You might want to open a login window here
                // Example:
                 LoginController.showLoginWindow();
            }
        });
    }






    @FXML
    void sortAsc() {
        ObservableList<User> userList = UserTable.getItems();
        userList.sort(Comparator.comparing(User::getNom));
    }

    @FXML
    void sortDesc() {
        ObservableList<User> userList = UserTable.getItems();
        userList.sort(Comparator.comparing(User::getNom).reversed());
    }


    @FXML
    void Materiels(ActionEvent event) {
        switchScene("/Materiels.fxml", event);

    }
    // Method to calculate role count

}


