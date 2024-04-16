package Controller;

import entite.Actualite;
import entite.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import util.DataSource;

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
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Date;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import java.sql.Types;





public class EvenementController implements Initializable {
    Connection con = null;
    PreparedStatement st = null;
    ResultSet rs = null;

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
    private ObservableList<Evenement> evementList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        con = DataSource.getInstance().getConnection();
        configureTableView();
        loadData();
        loadActualiteIDs();

    }

    private void loadData() {
        evementList.clear();
        try {
            String query = "SELECT e.*, a.id_actualite AS actualite_id " +
                    "FROM evenement e " +
                    "LEFT JOIN actualite a ON e.actualite_id = a.id_actualite";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                int id_evenement = rs.getInt("id_evenement");
                String titre = rs.getString("Titre");
                Date date = rs.getDate("Date");
                int duree = rs.getInt("Duree");
                String lieu = rs.getString("lieu");
                String objectif = rs.getString("Objectif");
                String image = rs.getString("image");
                int actualiteId = rs.getInt("actualite_id");

                Evenement evenement = new Evenement(id_evenement, titre, date, duree, lieu, objectif, image, actualiteId);
                evementList.add(evenement);
            }

            tableEvent.setItems(evementList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        // Get the selected Actualite from the ComboBox
        String selectedActualite = ActualiteRelated.getSelectionModel().getSelectedItem();

        // Retrieve the Actualite ID from the database based on its title
        Integer actualiteId = null;
        if (selectedActualite != null) {
            try {
                String query = "SELECT id_actualite FROM actualite WHERE Titre = ?";
                PreparedStatement st = con.prepareStatement(query);
                st.setString(1, selectedActualite);
                ResultSet rs = st.executeQuery();

                // If the ResultSet has data, retrieve and assign the Actualite ID
                if (rs.next()) {
                    actualiteId = rs.getInt("id_actualite");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while retrieving Actualite ID.");
                e.printStackTrace();
                return;
            }
        }

        // Check if all fields are filled
        if (enent_title.getText().isEmpty() || event_date.getValue() == null || event_duree.getText().isEmpty()
                || event_lieu.getText().isEmpty() || event_obj.getText().isEmpty() || selectedImagePath == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please fill all fields and select an image.");
            return;
        }

        // Validate l duree
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

        // Validate the date
        if (event_date.getValue().isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Event date must be in the future.");
            return;
        }

        // Check if an image is selected
        if (selectedImagePath == null) {
            showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please select an image.");
            return;
        }

        // Insert the Evenement record with the retrieved Actualite ID
        String sql = "INSERT INTO evenement (Titre, Date, Duree, lieu, Objectif, image, actualite_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            // Prepare and execute the SQL statement
            PreparedStatement prepare = con.prepareStatement(sql);
            prepare.setString(1, enent_title.getText());
            prepare.setDate(2, java.sql.Date.valueOf(event_date.getValue()));
            prepare.setInt(3, Integer.parseInt(event_duree.getText()));
            prepare.setString(4, event_lieu.getText());
            prepare.setString(5, event_obj.getText());
            prepare.setString(6, selectedImagePath);
            if (actualiteId != null) {
                prepare.setInt(7, actualiteId);
            } else {
                prepare.setNull(7, Types.INTEGER); // Set the actualite_id to null
            }

            prepare.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Successfully Added!");

            ClearEvent(null);
            loadData();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while adding event to the database.");
            e.printStackTrace();
        }
    }

    @FXML
    void ClearEvent(ActionEvent event) {
        enent_title.setText("");
        event_duree.setText("");
        event_lieu.setText("");
        event_obj.setText("");
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

        String sql = "DELETE FROM evenement WHERE id_evenement = ?";
        try {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, selectedEvent.getId_evenement());

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete Event ID: " + selectedEvent.getId_evenement() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                statement.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Event successfully deleted!");

                ClearEvent(null);
                loadData();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while deleting the event.");
            e.printStackTrace();
        }
    }

    @FXML
    void UpdateEvent(ActionEvent event) {
        Evenement selectedEvent = tableEvent.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please select an event to update.");
            return;
        }

        String sql = "UPDATE evenement SET Titre = ?, Date = ?, Duree = ?, lieu = ?, Objectif = ?, image = ? WHERE id_evenement = ?";
        try {
            // Check if all fields are filled
            if (enent_title.getText().isEmpty() || event_date.getValue() == null || event_duree.getText().isEmpty()
                    || event_lieu.getText().isEmpty() || event_obj.getText().isEmpty() || selectedImagePath == null) {
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Please fill all fields and select an image.");
                return;
            }

            // Validate the date
            LocalDate selectedDate = event_date.getValue();
            if (selectedDate.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.ERROR, "Error Message", null, "Event date must be in the future.");
                return;
            }

            // Validate the duration (duree)
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

            // Prepare the SQL statement
            PreparedStatement prepare = con.prepareStatement(sql);
            prepare.setString(1, enent_title.getText());
            prepare.setDate(2, java.sql.Date.valueOf(event_date.getValue()));
            prepare.setInt(3, Integer.parseInt(event_duree.getText()));
            prepare.setString(4, event_lieu.getText());
            prepare.setString(5, event_obj.getText());
            prepare.setString(6, selectedImagePath); // Use selectedImagePath instead of file_path.getText()
            prepare.setInt(7, selectedEvent.getId_evenement());

            // Execute the update
            prepare.executeUpdate();

            showAlert(Alert.AlertType.INFORMATION, "Information Message", null, "Successfully Updated!");

            loadData();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Please enter a valid integer value for the duration.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "An error occurred while updating event in the database.");
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

        int selectedIndex = tableEvent.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0 || evenement == null)
            return;

        enent_title.setText(evenement.getTitre());
        event_duree.setText(String.valueOf(evenement.getDuree()));
        event_lieu.setText(evenement.getLieu());
        event_obj.setText(evenement.getObjectif());
        event_date.setValue(LocalDate.parse(String.valueOf(evenement.getDate())));
        //bch tothir l image fl imageview
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

        // Convert the id_actualite to a string before setting it
        ActualiteRelated.setValue(String.valueOf(evenement.getId_actualite()));
    }



    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    @FXML
    void chercherEvenement(KeyEvent event) {
        String searchValue = chercherEvent.getText();
        FilteredList<Evenement> filteredList = new FilteredList<>(evementList, e -> true);

        if (!searchValue.isEmpty()) {
            filteredList.setPredicate(evenement -> {
                String titre = evenement.getTitre().toLowerCase();
                String date = evenement.getDate().toString().toLowerCase();
                String duree = String.valueOf(evenement.getDuree());
                String lieu = evenement.getLieu().toLowerCase();
                String objectif = evenement.getObjectif().toLowerCase();

                return titre.contains(searchValue.toLowerCase()) ||
                        date.contains(searchValue.toLowerCase()) ||
                        duree.contains(searchValue.toLowerCase()) ||
                        lieu.contains(searchValue.toLowerCase()) ||
                        objectif.contains(searchValue.toLowerCase());
            });
        }

        SortedList<Evenement> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableEvent.comparatorProperty());
        tableEvent.setItems(sortedList);
    }




    private void loadActualiteIDs() {
        ActualiteRelated.getItems().clear();
        try {
            // Assuming you have a database connection stored in the 'con' variable
            String query = "SELECT id_actualite FROM actualite";
            st = con.prepareStatement(query);
            rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id_actualite");
                ActualiteRelated.getItems().add(String.valueOf(id)); // Convert integer to string
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




}
