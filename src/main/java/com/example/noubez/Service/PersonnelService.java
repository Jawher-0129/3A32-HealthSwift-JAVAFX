package com.example.noubez.Service;

import com.example.noubez.Model.Personnel;
import com.example.noubez.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnelService implements com.example.noubez.Service.IService<Personnel> {
    private Connection cnx;
    private Statement ste;
    private PreparedStatement pst;
    public PersonnelService(){
        cnx= DataSource.getInstance().getConnection();
    }

    public void add(Personnel p){
        String requete="insert into personnel(nom,prenom_personnel,disponibilite,role,experience,image,rating) " +
                "values('"+p.getNom()+"','"+p.getPrenom_personnel()+"','"+p.getDisponibilite()+"','"+p.getRole()+"','"+p.getExperience()+"','"+p.getImage()+"','"+p.getRating()+"')";
        try {
            ste=cnx.createStatement();
            ste.executeUpdate(requete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void delete(int id_personnel) {
        try {
            // Delete associated records in chambre table
            String chambreDeleteQuery = "DELETE FROM chambre WHERE personnel = ?";
            try (PreparedStatement chambreDeleteStmt = cnx.prepareStatement(chambreDeleteQuery)) {
                chambreDeleteStmt.setInt(1, id_personnel);
                chambreDeleteStmt.executeUpdate();
            }

            // Delete personnel record
            String personnelDeleteQuery = "DELETE FROM personnel WHERE id_personnel = ?";
            try (PreparedStatement personnelDeleteStmt = cnx.prepareStatement(personnelDeleteQuery)) {
                personnelDeleteStmt.setInt(1, id_personnel);
                personnelDeleteStmt.executeUpdate();
            }

            System.out.println("Suppression Personnel effectuée");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Personnel p, int id_personnel) {
        String requete = "update personnel set nom=?,prenom_personnel=?,disponibilite=?,role=?,experience=?,image=?,rating=? where id_personnel=?";
        try{
            pst = cnx.prepareStatement(requete);
            pst.setString(1, p.getNom());
            pst.setString(2, p.getPrenom_personnel());
            pst.setInt(3, p.getDisponibilite());
            pst.setString(4, p.getRole());
            pst.setInt(5, p.getExperience());
            pst.setString(6, p.getImage());
            pst.setInt(7, p.getRating());
            pst.setInt(8, id_personnel);
            this.pst.executeUpdate();


        }catch (SQLException e) {
            throw  new RuntimeException(e);
        }
    }

    @Override
    public List<Personnel> getAll(){
        String requete = "SELECT * FROM personnel";
        List<Personnel> list = new ArrayList<>();

        try {
            Statement statement = cnx.createStatement();
            ResultSet resultSet = statement.executeQuery(requete);

            while (resultSet.next()) {
                int id_personnel = resultSet.getInt("id_personnel");
                int user_id_id = resultSet.getInt("user_id_id");
                int experience = resultSet.getInt("experience");
                int disponibilite = resultSet.getInt("disponibilite");
                int rating = resultSet.getInt("rating");
                String nom = resultSet.getString("nom");
                String prenom_personnel = resultSet.getString("prenom_personnel");
                String role = resultSet.getString("role");
                String image = resultSet.getString("image");

                Personnel pers = new Personnel(id_personnel, nom, prenom_personnel, disponibilite, role, experience, image, rating,user_id_id);
                list.add(pers);
            }

            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
    }

    @Override
    public Personnel getById(int id_personnel) {
        return null;
    }
}
