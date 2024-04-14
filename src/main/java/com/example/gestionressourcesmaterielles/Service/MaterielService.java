package com.example.gestionressourcesmaterielles.Service;

import com.example.gestionressourcesmaterielles.Model.IService;
import com.example.gestionressourcesmaterielles.Model.Materiel;
import com.example.gestionressourcesmaterielles.util.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterielService implements IService<Materiel> {
    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;


    public MaterielService()
    {
        cnx= DataSource.getInstance().getConnetion();
    }

    public boolean rechercherCategorie(int idCategorie)
    {
        String requete="select * from categorie where id_categorie=?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setInt(1,idCategorie);
            return pst.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Materiel materiel) {
        String requete="insert into materiel(id_categorie,libelle,description,disponibilite,image,prix) values" +
                "(?,?,?,?,?,?)";
        try {
            if(rechercherCategorie(materiel.getId_categorie())) {
                pst = cnx.prepareStatement(requete);
                pst.setInt(1, materiel.getId_categorie());
                pst.setString(2, materiel.getLibelleMateriel());
                pst.setString(3, materiel.getDescription());
                pst.setInt(4, materiel.getDisponibilite());
                pst.setString(5, materiel.getImageMateriel());
                pst.setDouble(6, materiel.getPrix());
                pst.executeUpdate();
                System.out.println("Ajout Materiel effectuee avec succees");
            }
            else
                System.out.println("Categorie n'existe pas");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String requete="delete from materiel where id_materiel=?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setInt(1,id);
            pst.executeUpdate();
            System.out.println("Suppression Materiel effectue");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Materiel materiel, int id) {

        String requete="update materiel set libelle=?,description=?,disponibilite=?,image=?,prix=?,id_categorie=? where id_materiel=?";
        try {
            pst=cnx.prepareStatement(requete);
            pst.setString(1,materiel.getLibelleMateriel());
            pst.setString(2,materiel.getDescription());
            pst.setInt(3,materiel.getDisponibilite());
            pst.setString(4,materiel.getImageMateriel());
            pst.setDouble(5,materiel.getPrix());
            pst.setInt(6,materiel.getId_categorie());
            pst.setInt(7,materiel.getId());

            this.pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Materiel> getAll() {
        String requete="select * from materiel";
        List<Materiel> list=new ArrayList<>();
        try {
            ste=cnx.createStatement();
            ResultSet rs=ste.executeQuery(requete);
            while(rs.next())
            {
                list.add(new Materiel(rs.getInt("id_materiel"), rs.getString("libelle"),
                        rs.getString("description"), rs.getInt("disponibilite"),
                        rs.getString("image"), rs.getDouble("prix"),
                        rs.getInt("id_categorie")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Materiel getById(int id) {
        return null;
    }


    public ObservableList<Materiel> getMaterielByCategorie(int idCategorie) {
        String requete = "SELECT * FROM materiel WHERE id_categorie=?";
        ObservableList<Materiel> obList = FXCollections.observableArrayList();
        try (PreparedStatement ps = cnx.prepareStatement(requete)) {
            ps.setInt(1, idCategorie);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                obList.add(new Materiel(
                        rs.getInt("id_materiel"),
                        rs.getString("libelle"),
                        rs.getString("description"),
                        rs.getInt("disponibilite"),
                        rs.getString("image"),
                        rs.getDouble("prix")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obList;
    }


    public List<Materiel> rechercherParLibelle(String libelle) {
        String requete = "SELECT * FROM materiel WHERE libelle LIKE ?";
        List<Materiel> list = new ArrayList<>();
        try {
            pst = cnx.prepareStatement(requete);
            pst.setString(1, "%" + libelle + "%"); // Utilisation du joker '%' pour trouver des correspondances partielles
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Materiel(
                        rs.getInt("id_materiel"),
                        rs.getString("libelle"),
                        rs.getString("description"),
                        rs.getInt("disponibilite"),
                        rs.getString("image"),
                        rs.getDouble("prix"),
                        rs.getInt("id_categorie")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }




}
