package Service;

import entite.Evenement;
import util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EvenementService implements IService<Evenement> {
    private Connection cnx;
    private PreparedStatement pst;
    private Statement ste;
    private ResultSet rs;
    private Set<Integer> usedActualiteIds;

    public EvenementService() {
        cnx = DataSource.getInstance().getConnection();
        usedActualiteIds = new HashSet<>();
        loadUsedActualiteIds(); // Load existing used ids from the database
    }


    private void loadUsedActualiteIds() {
        String query = "SELECT DISTINCT actualite_id FROM evenement WHERE actualite_id IS NOT NULL";
        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(query);
            while (rs.next()) {
                usedActualiteIds.add(rs.getInt("actualite_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Check if an actualiteId is already used
    private boolean isActualiteIdUsed(int actualiteId) {
        return usedActualiteIds.contains(actualiteId);
    }

    @Override
    public void add(Evenement evenement) {
        // Check if id_actualite is provided and not already used
        if (evenement.getId_actualite() != -1 && isActualiteIdUsed(evenement.getId_actualite())) {
            throw new IllegalArgumentException("id_actualite is already used.");
        }

        String sql = "INSERT INTO evenement (Titre, Date, Duree, lieu, Objectif, image, actualite_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            pst = cnx.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, evenement.getTitre());
            pst.setDate(2, new java.sql.Date(evenement.getDate().getTime()));
            pst.setInt(3, evenement.getDuree());
            pst.setString(4, evenement.getLieu());
            pst.setString(5, evenement.getObjectif());
            pst.setString(6, evenement.getImage());

            // Check if id_actualite is provided, if not, set it to -1
            if (evenement.getId_actualite() != -1) {
                pst.setInt(7, evenement.getId_actualite());
                // Add the used actualiteId to the set
                usedActualiteIds.add(evenement.getId_actualite());
            } else {
                pst.setNull(7, Types.INTEGER);
            }

            pst.executeUpdate();
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                evenement.setId_evenement(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM evenement WHERE id_evenement = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Evenement evenement, int id) {
        // Check if id_actualite is provided and not already used
        if (evenement.getId_actualite() != -1 && isActualiteIdUsedByOtherEvent(evenement.getId_evenement(), evenement.getId_actualite())) {
            throw new IllegalArgumentException("id_actualite is already used by another event.");
        }

        String sql = "UPDATE evenement SET Titre = ?, Date = ?, Duree = ?, lieu = ?, Objectif = ?, image = ?, actualite_id = ? WHERE id_evenement = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setString(1, evenement.getTitre());
            pst.setDate(2, new java.sql.Date(evenement.getDate().getTime()));
            pst.setInt(3, evenement.getDuree());
            pst.setString(4, evenement.getLieu());
            pst.setString(5, evenement.getObjectif());
            pst.setString(6, evenement.getImage());

            // Check if id_actualite is provided, if not, set it to -1
            if (evenement.getId_actualite() != -1) {
                pst.setInt(7, evenement.getId_actualite());
            } else {
                pst.setNull(7, Types.INTEGER);
            }

            pst.setInt(8, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isActualiteIdUsedByOtherEvent(int eventId, int actualiteId) {
        String sql = "SELECT COUNT(*) AS count FROM evenement WHERE id_evenement <> ? AND actualite_id = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, eventId);
            pst.setInt(2, actualiteId);
            rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<Evenement> getAll() {
        List<Evenement> evenementList = new ArrayList<>();
        String sql = "SELECT * FROM evenement";
        try {
            pst = cnx.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                int id_evenement = rs.getInt("id_evenement");
                String titre = rs.getString("Titre");
                Date date = rs.getDate("Date");
                int duree = rs.getInt("Duree");
                String lieu = rs.getString("lieu");
                String objectif = rs.getString("Objectif");
                String image = rs.getString("image");
                int actualiteId = rs.getInt("actualite_id");

                Evenement evenement = new Evenement(id_evenement, titre, date, duree, lieu, objectif, image, actualiteId);
                evenementList.add(evenement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenementList;
    }

    @Override
    public Evenement getById(int id) {
        String sql = "SELECT * FROM evenement WHERE id_evenement = ?";
        try {
            pst = cnx.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                int id_evenement = rs.getInt("id_evenement");
                String titre = rs.getString("Titre");
                Date date = rs.getDate("Date");
                int duree = rs.getInt("Duree");
                String lieu = rs.getString("lieu");
                String objectif = rs.getString("Objectif");
                String image = rs.getString("image");
                int actualiteId = rs.getInt("actualite_id");

                return new Evenement(id_evenement, titre, date, duree, lieu, objectif, image, actualiteId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getActualiteIdByTitle(String title) {
        String query = "SELECT id_actualite FROM actualite WHERE titre = ?";
        try {
            pst = cnx.prepareStatement(query);
            pst.setString(1, title);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_actualite");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // ya33tini -1 kn famech
    }
    public List<String> getAllActualiteTitles() {
        List<String> titles = new ArrayList<>();
        String query = "SELECT titre FROM actualite";
        try {
            ste = cnx.createStatement();
            ResultSet rs = ste.executeQuery(query);
            while (rs.next()) {
                String titre = rs.getString("titre");
                titles.add(titre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return titles;
    }
}

