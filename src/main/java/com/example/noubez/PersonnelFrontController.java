package com.example.noubez;

import com.example.noubez.Model.Personnel;
import com.example.noubez.Service.PersonnelFrontService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.util.List;

public class PersonnelFrontController {
        private int currentIndex = 0;
        private List<Personnel> personnelList;
        private final PersonnelFrontService personnelFrontService = new PersonnelFrontService();

        @FXML
        private Label nomPersonnel;

        @FXML
        private Label prenomPersonnel;

        @FXML
        private ImageView imageP;

        @FXML
        public void initialize() {
                // Récupérer la liste des Personnel depuis le service
                personnelList = personnelFrontService.getAll();
                if (personnelList != null && !personnelList.isEmpty()) {
                        showPersonnel(currentIndex);
                } else {
                        // Gérer le cas où la liste est vide (par exemple, afficher un message)
                        System.out.println("Personnel list is empty!");
                }
        }

        @FXML
        public void nextPost() {
                if (personnelList != null && !personnelList.isEmpty()) {
                        currentIndex = (currentIndex + 1) % personnelList.size();
                        showPersonnel(currentIndex);
                }
        }

        @FXML
        public void PreviousPost() {
                if (personnelList != null && !personnelList.isEmpty()) {
                        currentIndex = (currentIndex - 1 + personnelList.size()) % personnelList.size();
                        showPersonnel(currentIndex);
                }
        }

        private void showPersonnel(int index) {
                if (personnelList != null && !personnelList.isEmpty() && index >= 0 && index < personnelList.size()) {
                        Personnel personnel = personnelList.get(index);
                        nomPersonnel.setText(personnel.getNom());
                        prenomPersonnel.setText(personnel.getPrenom_personnel());
                        afficherImage(personnel.getImage());
                } else {
                        // Gérer le cas d'index invalide (par exemple, afficher un message d'erreur)
                        System.out.println("Invalid index for personnel list!");
                }
        }

        public void afficherImage(String cheminImage) {
                if (cheminImage != null && !cheminImage.isEmpty()) {
                        try {
                                Image image = new Image(cheminImage);
                                imageP.setImage(image);
                        } catch (Exception e) {
                                // Gérer l'exception si le chargement de l'image échoue (par exemple, afficher une image par défaut)
                                System.out.println("Error loading image: " + e.getMessage());
                        }
                }
        }
}