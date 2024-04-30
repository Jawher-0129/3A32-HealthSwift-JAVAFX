package com.exemple.pidevd.Controller;

import com.exemple.pidevd.Model.Demande;
import com.exemple.pidevd.Model.RendezVous;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;

public class cardRendezvous {

    @FXML
    private Label date;

    @FXML
    private Label lieu;

    @FXML
    private Label objective;
        public void setData(RendezVous demande){
        lieu.setText(demande.getLieu());
        objective.setText(demande.getObjective());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(demande.getDate());
        date.setText(formattedDate);
            lieu.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            objective.setStyle("-fx-font-size: 12px;");
       }
}
