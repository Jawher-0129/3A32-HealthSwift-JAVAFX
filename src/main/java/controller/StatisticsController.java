package controller;

// package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import service.StatisticsService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatisticsController {
    @FXML private BarChart<String, Number> donationsPerCampaignChart;
    @FXML private BarChart<String, Number> donationsPerMonthChart;
    @FXML private BarChart<String, Number> campaignsPerMonthChart;



    private StatisticsService statisticsService;

    @FXML
    public void initialize() {
        statisticsService = new StatisticsService();
        loadCharts();
    }

    private void loadCharts() {
        loadDonationsPerCampaign();
        loadDonationsPerMonth();
        loadCampaignsPerMonth();
    }

    private void loadDonationsPerCampaign() {
        Map<String, Integer> stats = statisticsService.countDonationsPerCampaign();
        Platform.runLater(() -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Set<String> filteredKeys = new HashSet<>();
            stats.forEach((campaignId, count) -> {
                if (campaignId != null && count != null && filteredKeys.add(campaignId)) {
                    series.getData().add(new XYChart.Data<>(campaignId, count));
                }
            });
            donationsPerCampaignChart.getData().clear();
            donationsPerCampaignChart.getData().add(series);
        });
    }


    private void loadDonationsPerMonth() {
        Map<String, Integer> stats = statisticsService.countDonationsPerMonth();
        Platform.runLater(() -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Set<String> uniqueKeys = new HashSet<>();
            stats.forEach((month, count) -> {
                if (month != null && count != null && uniqueKeys.add(month)) {
                    series.getData().add(new XYChart.Data<>(month, count));
                }
            });
            donationsPerMonthChart.getData().clear();
            donationsPerMonthChart.getData().add(series);
        });
    }

    private void loadCampaignsPerMonth() {
        Map<String, Integer> stats = statisticsService.countCampaignsPerMonth();
        Platform.runLater(() -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            Set<String> uniqueKeys = new HashSet<>();
            stats.forEach((month, count) -> {
                if (month != null && count != null && uniqueKeys.add(month)) {
                    series.getData().add(new XYChart.Data<>(month, count));
                }
            });
            campaignsPerMonthChart.getData().clear();
            campaignsPerMonthChart.getData().add(series);
        });
    }



}
