package com.example.gestionressourcesmaterielles.Service;

import com.example.gestionressourcesmaterielles.Model.Categorie;
import com.example.gestionressourcesmaterielles.Model.IService;
import com.example.gestionressourcesmaterielles.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IService<Categorie> {
    private Connection cnx = DataSource.getInstance().getConnetion();
    private Statement ste;
    private PreparedStatement pst;

    public CategorieService() {
    }

    public void add(Categorie c) {
        String requete = "insert into categorie(libelle_categorie) values('" + c.getLibelle() + "')";

        try {
            this.ste = this.cnx.createStatement();
            this.ste.executeUpdate(requete);
        } catch (SQLException var4) {
            throw new RuntimeException(var4);
        }
    }

    public void delete(int id) throws SQLException {
        String requete = "delete from categorie where id_categorie=?";

        try {
            this.pst = this.cnx.prepareStatement(requete);
            this.pst.setInt(1, id);
            this.pst.executeUpdate();
            System.out.println("Suppression effectue");
        } catch (SQLException var4) {
            throw new RuntimeException(var4);
        }
    }

    public void update(Categorie categorie, int id) {
        if (categorie.getLibelle() != "") {
            String requete = "update categorie set libelle_categorie=? where id_categorie=?";

            try {
                this.pst = this.cnx.prepareStatement(requete);
                this.pst.setString(1, categorie.getLibelle());
                this.pst.setInt(2, categorie.getId());
                this.pst.executeUpdate();
                System.out.println("Modification effectuee");
            } catch (SQLException var5) {
                throw new RuntimeException(var5);
            }
        }

    }

    public List<Categorie> getAll() {
        String requete = "select * from categorie";
        List<Categorie> list = new ArrayList();

        try {
            this.ste = this.cnx.createStatement();
            ResultSet rs = this.ste.executeQuery(requete);

            while(rs.next()) {
                list.add(new Categorie(rs.getInt("id_categorie"), rs.getString(2)));
            }
            return list;
        } catch (SQLException var4) {
            throw new RuntimeException(var4);
        }
    }

    public Boolean rechercherCategorie(String lib) {

        String requete = "select * from categorie where libelle_categorie=?";
        try {
            this.pst = this.cnx.prepareStatement(requete);
            this.pst.setString(1, lib);
            return this.pst.executeQuery().next();
        } catch (SQLException var4) {
            throw new RuntimeException(var4);
        }
    }

    public List<Integer> afficherIdCategories() {
        List<Integer> idCategories = new ArrayList<>();
        String requete = "select id_categorie from categorie";
        try {
            // Vérifier si la connexion est ouverte
            if (cnx != null && !cnx.isClosed()) {
                PreparedStatement pst = cnx.prepareStatement(requete);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    idCategories.add(rs.getInt("id_categorie"));
                }
            } else {
                // Gestion de l'erreur de connexion
                System.out.println("La connexion à la base de données n'est pas ouverte.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return idCategories;
    }






    public Categorie getById(int id) {
        return null;
    }
}

