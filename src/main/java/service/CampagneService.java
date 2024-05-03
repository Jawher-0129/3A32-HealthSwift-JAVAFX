package service;

import entite.Campagne;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CampagneService implements CrudService<Campagne> {
    private Connection cnx;

    public CampagneService() {
        cnx = DataSource.getInstance().getConnection();
    }

    @Override
    public Campagne save(Campagne campagne) {
        String sql = "INSERT INTO campagne (titre, description, date_debut, date_fin, image, directeur_id) VALUES (?, ?, ?, ?, ?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, campagne.getTitre());
            ps.setString(2, campagne.getDescription());
            ps.setString(3, campagne.getDate_debut());
            ps.setString(4, campagne.getDate_fin());
            ps.setString(5, campagne.getImage());  // Include image
            ps.setInt(6, campagne.getDirecteur_id());
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        campagne.setId(generatedKeys.getInt(1));
                        return campagne;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Campagne> findById(int id) {
        String sql = "SELECT * FROM campagne WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(mapToCampagne(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Campagne> findAll() {
        List<Campagne> campagnes = new ArrayList<>();
        String sql = "SELECT * FROM campagne";
        try (Statement s = cnx.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                campagnes.add(mapToCampagne(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campagnes;
    }

    @Override
    public Campagne update(Campagne campagne) {
        String sql = "UPDATE campagne SET titre = ?, description = ?, date_debut = ?, date_fin = ?, image = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, campagne.getTitre());
            ps.setString(2, campagne.getDescription());
            ps.setString(3, campagne.getDate_debut());
            ps.setString(4, campagne.getDate_fin());
            ps.setString(5, campagne.getImage());  // Include image
            ps.setInt(6, campagne.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                return campagne;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM campagne WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Campagne mapToCampagne(ResultSet rs) throws SQLException {
        return new Campagne(
                rs.getInt("id"),
                rs.getString("titre"),
                rs.getString("description"),
                rs.getString("date_debut"),
                rs.getString("date_fin"),
                rs.getString("image")  // Retrieve image from ResultSet
        );
    }

    @Override
    public String toString() {
        return "CampagneService{" +
                "cnx=" + cnx +
                '}';
    }
}
