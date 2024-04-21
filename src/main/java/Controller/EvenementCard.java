package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class EvenementCard {

    @FXML
    private VBox card;

    @FXML
    private Label titleLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ImageView eventImage;

    public EvenementCard() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Fxml/EvenementCard.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    public void setDate(String date) {
        dateLabel.setText(date);
    }

    public void setImage(Image image) {
        eventImage.setImage(image);
    }

    public VBox getCard() {
        return card;
    }
}
