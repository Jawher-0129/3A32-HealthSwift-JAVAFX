package com.exemple.pidevd.Controller;

import com.exemple.pidevd.Model.Demande;
import com.exemple.pidevd.Service.DemandeService;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class sample implements Initializable {
    @FXML
    private ImageView exit;
    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML
    private AnchorPane contenu;
    @FXML
    private Button Calendrier;
    public AnchorPane getContenu() {
        return contenu;
    }
    private boolean isInitialized = false;
    @FXML
    private Button demande;
    private DemandeController demandeController;
    private DemandeService demandeService;

    public sample() {
        demandeService = new DemandeService();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
            Parent root = loader.load();

            DemandeController demandeController = loader.getController();
            demandeController.initSampleController(this); // Passez la référence du SampleController au DemandeController

            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

       /* try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
            Node sidebarContent = loader.load();

            // Récupérer le contrôleur de Demande.fxml si nécessaire
            DemandeController demandeController = loader.getController();

            // Ajouter le contenu initial de Demande.fxml à l'AnchorPane contenu
            contenu.getChildren().setAll(sidebarContent);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        slider.setTranslateX(-176);
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        Menu.setOnMouseClicked(event ->{
            TranslateTransition slide=new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);
            slide.setToX(0);
            slide.play();
            slider.setTranslateX(-176);
            slide.setOnFinished((ActionEvent e)->{
              Menu.setVisible(false);
              MenuBack.setVisible(true);
            });
        } );
        MenuBack.setOnMouseClicked(event ->{
            TranslateTransition slide=new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(slider);
            slide.setToX(-176);
            slide.play();
            slider.setTranslateX(0);
            slide.setOnFinished((ActionEvent e)->{
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        } );
        /*if (!isInitialized) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
                Node sidebarContent = loader.load();

                // Récupérer le contrôleur de Demande.fxml si nécessaire
                DemandeController demandeController = loader.getController();

                // Ajouter le contenu initial de Demande.fxml à l'AnchorPane contenu
                contenu.getChildren().setAll(sidebarContent);
            } catch (IOException e) {
                e.printStackTrace();
            }

            isInitialized = true;
        }*/

    }
    @FXML
    protected void afficherDemande() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Demande.fxml"));
            Parent root = loader.load();

            DemandeController demandeController = loader.getController();
            demandeController.initSampleController(this); // Passez la référence du SampleController au DemandeController

            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void afficherRendezVous(int idDemande) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/RendezVous.fxml"));
            Parent root = loader.load();

            RendezVousController rendezVousController = loader.getController();
            // Configurez le contrôleur de la vue RendezVous si nécessaire
            rendezVousController.initData(idDemande);
            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void afficherCalendrier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/calendrier.fxml"));
            Parent root = loader.load();

            CalenderController CalenController = loader.getController();
            // Configurez le contrôleur de la vue RendezVous si nécessaire
            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void afficherStatistique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/exemple/pidevd/Statistique.fxml"));
            Parent root = loader.load();

            StatController SController = loader.getController();
            // Configurez le contrôleur de la vue RendezVous si nécessaire
            contenu.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ExportExcel() {

        List<Demande> demandes = demandeService.getAll();
        String filePath = "export.xlsx";
        exportToExcel(demandes, filePath);
        System.out.println("Données exportées vers Excel.");

    }

    public void exportToExcel(List<Demande> demandes, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Demandes");

            // Style pour l'état "En cours de traitement" (noir)
            CellStyle enCoursStyle = workbook.createCellStyle();
            enCoursStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
            enCoursStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Style pour l'état "Refusé" (rouge)
            CellStyle refuseStyle = workbook.createCellStyle();
            refuseStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            refuseStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Style pour l'état "Demande traitée" (vert)
            CellStyle traiteStyle = workbook.createCellStyle();
            traiteStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
            traiteStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Créer une ligne d'en-tête avec les attributs
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Titre");
            headerRow.createCell(1).setCellValue("Description");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("ID du directeur de campagne");
            headerRow.createCell(4).setCellValue("État");
            headerRow.createCell(5).setCellValue("ID du rendez-vous");

            // Parcourir les demandes et les écrire dans le fichier Excel
            int rowNum = 1;
            for (Demande demande : demandes) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                row.createCell(colNum++).setCellValue(demande.getTitre());
                row.createCell(colNum++).setCellValue(demande.getDescription());
                row.createCell(colNum++).setCellValue(demande.getDate());
                row.createCell(colNum++).setCellValue(demandeService.getEmailDirecteur(demande.getDirecteurCampagne()));
                Cell cell = row.createCell(colNum);
                cell.setCellValue(demande.getStatut());

                // Appliquer la mise en forme conditionnelle en fonction de l'état de la demande
                String etat = demande.getStatut();
                if (etat.equals("ENCOURS DE TRAITMENT")) {
                    cell.setCellStyle(enCoursStyle);
                } else if (etat.equals("REFUSEE")) {
                    cell.setCellStyle(refuseStyle);
                } else if (etat.equals("DEMANDE TRAITEE")) {
                    cell.setCellStyle(traiteStyle);
                    row.createCell(5).setCellValue(demande.getId_rendezvous());
                }
            }
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
