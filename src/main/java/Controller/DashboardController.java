package Controller;

import Service.EvenementService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label totalEventsLabel;

    @FXML
    private Label availableEventsLabel;

    @FXML
    private BarChart<String, Integer> eventsByDateChart;

    @FXML
    private LineChart<String, Integer> eventsByDurationChart;

    private EvenementService evenementService = new EvenementService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateTotalEvents();
        updateAvailableEvents();
        updateEventsByDateChart();
        updateEventsByDurationChart();
    }

    private void updateTotalEvents() {
        int totalEvents = evenementService.getTotalEvents();
        totalEventsLabel.setText(String.valueOf(totalEvents));
    }

    private void updateAvailableEvents() {
        int availableEvents = evenementService.getAvailableEvents();
        availableEventsLabel.setText(String.valueOf(availableEvents));
    }

    private void updateEventsByDateChart() {
        List<String> dates = evenementService.getDistinctDatesOfEvents();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (String date : dates) {
            int count = evenementService.getEventCountByDate(date);
            series.getData().add(new XYChart.Data<>(date, count));
        }
        eventsByDateChart.getData().add(series);
    }

    private void updateEventsByDurationChart() {
        List<Integer> durations = evenementService.getDistinctDurationsOfEvents();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (Integer duration : durations) {
            int count = evenementService.getEventCountByDuration(duration);
            series.getData().add(new XYChart.Data<>(String.valueOf(duration), count));
        }
        eventsByDurationChart.getData().add(series);
    }


}
