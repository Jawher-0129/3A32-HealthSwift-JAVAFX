package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;
import service.StatisticsService;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatisticsController {
    @FXML
    private BarChart<String, Number> donationsPerCampaignChart;
    @FXML
    private BarChart<String, Number> donationsPerMonthChart;
    @FXML
    private BarChart<String, Number> campaignsPerMonthChart;

    private StatisticsService statisticsService;

    @FXML
    private PieChart donationsWithWithoutCampaignChart;

    @FXML
    public void initialize() {
        statisticsService = new StatisticsService();
        loadCharts();
        loadDonationsWithAndWithoutCampaign(); // Load pie chart data
    }


    private void loadDonationsWithAndWithoutCampaign() {
        Map<Boolean, Integer> stats = statisticsService.countDonationsWithWithoutCampaign();
        Platform.runLater(() -> {
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            stats.forEach((associated, count) -> {
                String label = associated ? "With Campaign" : "Without Campaign";
                pieChartData.add(new PieChart.Data(label, count));
            });
            donationsWithWithoutCampaignChart.setData(pieChartData);
        });
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
                    addTooltipToData(series.getData().get(series.getData().size() - 1));
                }
            });
            donationsPerCampaignChart.getData().clear();
            donationsPerCampaignChart.getData().add(series);
            formatChartAxes(donationsPerCampaignChart);
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
                    addTooltipToData(series.getData().get(series.getData().size() - 1));
                }
            });
            donationsPerMonthChart.getData().clear();
            donationsPerMonthChart.getData().add(series);
            formatChartAxes(donationsPerMonthChart);
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
                    addTooltipToData(series.getData().get(series.getData().size() - 1));
                }
            });
            campaignsPerMonthChart.getData().clear();
            campaignsPerMonthChart.getData().add(series);
            formatChartAxes(campaignsPerMonthChart);
        });
    }

    private void formatChartAxes(BarChart<String, Number> chart) {
        CategoryAxis xAxis = (CategoryAxis) chart.getXAxis();
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();

        // Removed the lines that set the axis labels
        // xAxis.setLabel("Categories");
        // yAxis.setLabel("Values");

        xAxis.setTickLabelFont(new Font("Arial", 10));
        xAxis.setTickLabelRotation(-50);

        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(calculateUpperBound(chart));
        yAxis.setTickUnit(calculateTickUnit(chart));
    }

    private double calculateTickUnit(BarChart<String, Number> chart) {
        // Replace with dynamic calculation if needed
        return 1; // For example, each tick represents one unit
    }

    private double calculateUpperBound(BarChart<String, Number> chart) {
        double maxDataValue = chart.getData().stream()
                .flatMap(series -> series.getData().stream())
                .mapToDouble(data -> data.getYValue().doubleValue())
                .max()
                .orElse(0);

        return Math.ceil(maxDataValue); // Round up to the nearest integer if you want integer values
    }

    private void addTooltipToData(XYChart.Data<String, Number> data) {
        Tooltip tooltip = new Tooltip(data.getXValue());
        Tooltip.install(data.getNode(), tooltip);
    }
}
