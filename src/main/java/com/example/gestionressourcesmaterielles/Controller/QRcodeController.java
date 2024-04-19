package com.example.gestionressourcesmaterielles.Controller;

import com.example.gestionressourcesmaterielles.Model.Materiel;
import com.example.gestionressourcesmaterielles.Service.MaterielService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QRcodeController implements Initializable {

    private final MaterielService se =new MaterielService();

    @FXML
    private Button generateButton;

    @FXML
    private ImageView qrCodeImageView;

    @FXML
    Button buttonMaterielAdmin;


    @FXML
    private ComboBox<Materiel> tf_combobox; // Assuming you have a ComboBox of Concours objects

    @FXML
    void generateQRCode(ActionEvent event) {
        Materiel selectedEvenement = tf_combobox.getValue();
        if (selectedEvenement != null) {
            String data = selectedEvenement.getLibelleMateriel() + "\n"
                    + selectedEvenement.getDescription() + "\n"+ selectedEvenement.getPrix()+"\n"+selectedEvenement.getId_categorie()+"\n"+selectedEvenement.getDisponibilite();
            // Set the size of the QR code image
            int width = 300;
            int height = 300;
            try {
                // Generate the QR code
                BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height);
                // Convert the BitMatrix to an Image
                Image qrCodeImage = toFXImage(bitMatrix);

                // Display the QR code image in the ImageView
                qrCodeImageView.setImage(qrCodeImage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Image toFXImage(BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean bit = bitMatrix.get(x, y);
                Color color = bit ? Color.BLACK : Color.WHITE;
                pixelWriter.setColor(x, y, color);
            }
        }

        return writableImage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<Materiel> list = null;

        list = (ArrayList<Materiel>) se.getAll();

        ArrayList<String> listN = null;
        for (Materiel i : list)
        {
            System.out.println(i.getLibelleMateriel());
            //listN.add(i.getNom());
        }
        ObservableList<Materiel> observableList = FXCollections.observableList(list);
        tf_combobox.setItems(observableList);
    }


    @FXML
    void PageAdminMateriel(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionressourcesmaterielles/MaterielInterfaceAdmin.fxml"));
            Parent root=loader.load();
            buttonMaterielAdmin.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void PageAdminCategorie(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionressourcesmaterielles/CategorieInterfaceAdmin.fxml"));
            Parent root=loader.load();
            buttonMaterielAdmin.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }




}
