package com.exemple.pidevd.Controller;

import com.exemple.pidevd.Model.RendezVous;
import com.exemple.pidevd.Service.RendezVousService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalenderController implements Initializable {
    ZonedDateTime dateFocus;
    ZonedDateTime today;
    @FXML
    private FlowPane calendar;

    @FXML
    private Text month;
    private RendezVousService rendezVousService;

    public CalenderController() {
        rendezVousService = new RendezVousService();
    }

    @FXML
    private Text year;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        // Créer le rendezVousMap en utilisant la méthode createCalendarMap()
        Map<LocalDate, List<RendezVous>> rendezVousMap = createCalendarMap();

        int monthMaxDate = dateFocus.getMonth().maxLength();
        // Check for leap year
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = dateFocus.withDayOfMonth(1).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        LocalDate currentDateLocalDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonth(), currentDate);
                        List<RendezVous> rendezVousList = rendezVousMap.get(currentDateLocalDate);
                        if (rendezVousList != null) {
                            createCalendarActivity(rendezVousList, rectangleHeight, rectangleWidth, stackPane);
                        }
                    }
                    if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                        rectangle.setStroke(Color.BLUE);
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<RendezVous> rendezVousList, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        for (int k = 0; k < rendezVousList.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                stackPane.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    // Sur le clic de "..."
                    System.out.println(rendezVousList); // Affiche tous les rendez-vous pour la date donnée
                });
                break;
            }

            RendezVous rendezVous = rendezVousList.get(k);
            Timestamp timestamp = rendezVous.getDate();

            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            LocalDate rendezVousDate = localDateTime.toLocalDate();
            LocalTime rendezVousTime = localDateTime.toLocalTime();

            Rectangle activityRectangle = new Rectangle();
            activityRectangle.setFill(Color.GREEN);
            activityRectangle.setWidth(rectangleWidth);
            double activityRectangleHeight = rectangleHeight / 2;
            activityRectangle.setHeight(activityRectangleHeight);
            stackPane.getChildren().add(activityRectangle);

            Text activityTime = new Text(rendezVousTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            double textTranslationY = -(activityRectangleHeight / 2) * 0.75;
            activityTime.setTranslateY(textTranslationY);
            stackPane.getChildren().add(activityTime);

            activityRectangle.setOnMouseClicked(mouseEvent -> {
                // Sur le clic de l'activité
                System.out.println(rendezVous); // Affiche le rendez-vous spécifique
            });
        }
    }

    private Map<LocalDate, List<RendezVous>> createCalendarMap() {
        List<RendezVous> rendezVousList = rendezVousService.getAll();
        Map<LocalDate, List<RendezVous>> rendezVousMap = new HashMap<>();

        for (RendezVous rendezVous : rendezVousList) {
            Timestamp rendezVousTimestamp = rendezVous.getDate();
            LocalDateTime rendezVousDateTime = rendezVousTimestamp.toLocalDateTime();
            LocalDate rendezVousDate = rendezVousDateTime.toLocalDate();

            List<RendezVous> dayRendezVousList = rendezVousMap.getOrDefault(rendezVousDate, new ArrayList<>());
            dayRendezVousList.add(rendezVous);
            rendezVousMap.put(rendezVousDate, dayRendezVousList);
        }

        return rendezVousMap;
    }
}