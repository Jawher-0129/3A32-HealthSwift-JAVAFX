package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import Entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import service.Usercrud;

public class Registre {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Adresse;

    @FXML
    private TextField email;

    @FXML
    private Label lblErrors;

    @FXML
    private Button loginButton;

    @FXML
    private TextField nom;

    @FXML
    private PasswordField password;

    @FXML
    private TextField prenom;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField telephone;


    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    @FXML
    void Registre(ActionEvent event) {
        String nom = this.nom.getText();
        String prenom = this.prenom.getText();
        String email = this.email.getText();
        String adresse = this.Adresse.getText();

        String telephone = this.telephone.getText();

        String password = this.password.getText();


        // Check if any field is empty
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || password.isEmpty()) {
            // Display an alert if any of the fields are empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("All fields are required");
            alert.show();
            return;
        }

        // Check if the email is valid
        if (!isValidEmail(email)) {
            // Display an alert if the email is invalid
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please enter a valid email address");
            alert.show();
            return;
        }
        if (new Usercrud().isEmailExistsInDatabase(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Email already exists");
            alert.show();
            return;
        }
        // Check if a role is selected
        String selectedRole = roleComboBox.getValue();
        if (selectedRole == null || selectedRole.isEmpty()) {
            // Display an alert if no role is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a role");
            alert.show();
            return;
        }

        // Hash the password
        String hashedPassword = hashPassword(password);
        User newUser = new User(nom,prenom,email,hashedPassword,adresse,telephone,selectedRole);

        Usercrud add = new Usercrud();
        add.addUser(newUser);

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setContentText("Registration successful");
        successAlert.show();
        switchScene("/Login.fxml", event);



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
    void handleButtonAction(MouseEvent event) {

    }
    @FXML
    void Login(ActionEvent event) {
        switchScene("/Login.fxml", event);

    }
    @FXML
    void initialize() {

    }

}
