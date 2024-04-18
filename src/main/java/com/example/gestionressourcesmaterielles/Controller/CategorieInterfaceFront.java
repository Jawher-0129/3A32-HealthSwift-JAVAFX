package com.example.gestionressourcesmaterielles.Controller;

import com.example.gestionressourcesmaterielles.Model.Categorie;
import com.example.gestionressourcesmaterielles.Service.CategorieService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CategorieInterfaceFront implements Initializable {

    @FXML
    private ListView<Categorie> categorieListView;

    private CategorieService categorieService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categorieService = new CategorieService();
        loadCategories();
    }

    private void loadCategories() {
        List<Categorie> categories = categorieService.getAll();
        ObservableList<Categorie> categoryObservableList = FXCollections.observableArrayList(categories);
        categorieListView.setItems(categoryObservableList);

        // Définir un CellFactory pour personnaliser l'affichage des éléments de la ListView
        categorieListView.setCellFactory(new Callback<ListView<Categorie>, ListCell<Categorie>>() {
            @Override
            public ListCell<Categorie> call(ListView<Categorie> param) {
                return new ListCell<Categorie>() {
                    @Override
                    protected void updateItem(Categorie categorie, boolean empty) {
                        super.updateItem(categorie, empty);
                        if (empty || categorie == null) {
                            setText(null);
                        } else {
                            setText(categorie.getLibelle());
                        }
                    }
                };
            }
        });
    }

}
