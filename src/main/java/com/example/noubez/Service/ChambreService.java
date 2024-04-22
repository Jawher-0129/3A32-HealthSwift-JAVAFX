package com.example.noubez.Service;

import com.example.noubez.Model.Chambre;
import com.example.noubez.util.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


    public class ChambreService implements com.example.noubez.Service.IService <Chambre> {
        private Connection cnx;
        private Statement ste;
        private PreparedStatement pst;
        public ChambreService(){
            cnx= DataSource.getInstance().getConnection();
        }

        public void add(Chambre c){
            String requete="insert into chambre(numero,personnel,disponibilite,nombre_lits_total,nmbr_lits_disponible) " +
                    "values('"+c.getNumero()+"','"+c.getPersonnel()+"','"+c.getDisponibilite()+"','"+c.getNombre_lits_total()+"','"+c.getNmbr_lits_disponible()+"')";
            try {
                ste=cnx.createStatement();
                ste.executeUpdate(requete);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }


        @Override
        public void delete(int numero) {
            String requete="delete from chambre where numero=?";
            try {
                pst=cnx.prepareStatement(requete);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pst.setInt(1,numero);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Suppression Chambre effectué");

        }

        @Override
        public void update(Chambre c, int numero) {
            String requete = "update chambre set numero=?,personnel=?,disponibilite=?,nombre_lits_total=?,nmbr_lits_disponible=? where numero=?";
            try{
                pst = cnx.prepareStatement(requete);
                pst.setInt(1, c.getNumero());
                pst.setInt(2, c.getPersonnel());
                pst.setInt(3, c.getDisponibilite());
                pst.setInt(4, c.getNombre_lits_total());
                pst.setInt(5, c.getNmbr_lits_disponible());

                this.pst.executeUpdate();


            }catch (SQLException e) {
                throw  new RuntimeException(e);
            }
        }



        @Override
        public List<Chambre> getAll() {
            String requete="Select * from Chambre";
            List<Chambre> list=new ArrayList<>();
            try {
                ste= cnx.createStatement();
                ResultSet rs=ste.executeQuery(requete);
                while (rs.next()) {
                    list.add(new Chambre(
                            rs.getInt("numero"),
                            rs.getInt("personnel"),
                            rs.getInt("disponibilite"),
                            rs.getInt("nombre_lits_total"),
                            rs.getInt("nmbr_lits_disponible")));

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return list;

        }

        @Override
        public Chambre getById(int numero) {
            return null;
        }
    }

