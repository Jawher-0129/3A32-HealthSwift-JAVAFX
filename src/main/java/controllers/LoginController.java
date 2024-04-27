package controllers;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
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
import java.util.prefs.Preferences;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.mindrot.jbcrypt.BCrypt;
import service.Usercrud;
import javafx.scene.control.Alert;


public class LoginController {




    @FXML
    private CheckBox rememberMeCheckbox;
    @FXML
    private WebView recaptchaWebView;

    @FXML
    private ResourceBundle resources;
    @FXML
    private Label lblEmailError;

    @FXML
    private Label lblPasswordError;
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
    private void rememberCredentials(String email, String password) {
        // Store the email and password securely (e.g., using encryption)
        // For simplicity, let's just use Preferences API
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        prefs.put("rememberedEmail", email);
        prefs.put("rememberedPassword", password);
    }

    private void showErrorMessage(Label label, String message) {
        label.setTextFill(Color.RED);
        label.setText(message);
    }
    @FXML
    void login(ActionEvent event) {
        lblEmailError.setText(""); // Efface les messages d'erreur précédents
        lblPasswordError.setText("");

        String userEmail = email.getText();
        String pass = password.getText();
        if (!isValidEmail(userEmail)) {
            showErrorMessage(lblEmailError, "Please enter a valid email address.");
            return;
        }
        Usercrud x = new Usercrud();

        // Fetch user data from the database
        User user = x.getUserByEmail(userEmail);

        // Check if the user exists and the password matches
        if (user != null && BCrypt.checkpw(pass, user.getPassword())) {
            SessionManager.setCurrentUser(user);

            // Check the user's role
            String userRole = user.getRoles();
            if (userRole.equals("Admin")) {
                switchScene("/AdminPage.fxml", event);
            } else {
                switchScene("/Home.fxml", event);
            }
            if (rememberMeCheckbox.isSelected() && user != null) {
                rememberCredentials(user.getEmail(), pass);
            }
        } else {
            // Show error message for invalid email or password
            showErrorMessage(lblPasswordError, "Incorrect password !");
        }
    }

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
            if (event == null || event.getSource() == null) {
                System.out.println("Event or event source is null.");
                return;
            }

            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            if (stage != null) {
                stage.setScene(scene);
                stage.show();
            } else {
                System.out.println("Stage is null.");
            }
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
        loadRememberedCredentials();

    }

    private void loadRememberedCredentials() {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        String rememberedEmail = prefs.get("rememberedEmail", null);
        String rememberedPassword = prefs.get("rememberedPassword", null);

        if (rememberedEmail != null && rememberedPassword != null) {
            email.setText(rememberedEmail);
            password.setText(rememberedPassword);
            rememberMeCheckbox.setSelected(true);
        }
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
