// package service;
package service;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import util.DataSource;

public class StatisticsService {
    private Connection cnx;

    public StatisticsService() {
        this.cnx = DataSource.getInstance().getConnection();
    }

    public Map<String, Integer> countDonationsPerCampaign() {
        String sql = "SELECT campagne_id, COUNT(*) as donation_count FROM don GROUP BY campagne_id";
        Map<String, Integer> result = new HashMap<>();
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("campagne_id"), rs.getInt("donation_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> countDonationsPerMonth() {
        String sql = "SELECT DATE_FORMAT(date_remise, '%Y-%m') as month, COUNT(*) as donation_count " +
                "FROM don GROUP BY DATE_FORMAT(date_remise, '%Y-%m')";
        Map<String, Integer> result = new HashMap<>();
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("donation_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> countCampaignsPerMonth() {
        String sql = "SELECT DATE_FORMAT(date_debut, '%Y-%m') as month, COUNT(*) as campaign_count " +
                "FROM campagne GROUP BY DATE_FORMAT(date_debut, '%Y-%m')";
        Map<String, Integer> result = new HashMap<>();
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("month"), rs.getInt("campaign_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


}
