package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import java.io.IOException;
import Entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.mindrot.jbcrypt.BCrypt;
import service.Usercrud;

public class LoginController {

    @FXML
    private WebView recaptchaWebView;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label btnForgot;

    @FXML
    private TextField email;

    @FXML
    private Label lblErrors;

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField password;

    @FXML
    void handleButtonAction(MouseEvent event) {

    }

    @FXML
    void login(ActionEvent event) {


        String userEmail = email.getText();
        String pass = password.getText();
        if (!isValidEmail(userEmail)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Email", "Please enter a valid email address.");
            return;
        }
        Usercrud x = new Usercrud();

        // Fetch user data from the database
        User user = x.getUserByEmail(userEmail);


        // Check if the user exists and the password matches
        if (user != null && BCrypt.checkpw(pass, user.getPassword()) ) {
            SessionManager.setCurrentUser(user);

            // Check the user's role
            String userRole = user.getRoles();
            if (userRole.equals("Admin")) {
                switchScene("/AdminPage.fxml", event);
            } else {
                switchScene("/Home.fxml", event);
            }


                switchScene("/AdminPage.fxml", event);
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome!");
            }
         else {
            // Show error message for invalid email or password
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password!");
        }}


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    void Registre(ActionEvent event) {
        switchScene("/Registre.fxml", event);

    }

    @FXML
    void initialize() {

    }



    @FXML
    void forgotPassword(ActionEvent event) {
        switchScene("/ForgotPassword.fxml", event);
    }


    public static void showLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
