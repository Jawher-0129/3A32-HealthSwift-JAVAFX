package Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PaginationController {

    private static final int ITEMS_PER_PAGE = 9;

    @FXML
    private Pagination pagination;

    @FXML
    private VBox cardsContainer;


    private int totalItems = 27;

    public void initialize() {
        int pageCount = (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
        pagination.setPageCount(pageCount);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox pageContent = new VBox(10);

        int startIndex = pageIndex * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, totalItems);

        for (int i = startIndex; i < endIndex; i += 3) {
            HBox row = new HBox(10); // HBox to hold 3 cards in a row

            for (int j = 0; j < 3 && i + j < endIndex; j++) {
                // Example: create and add a Label for each item
                Label label = new Label("Item " + (i + j + 1));
                row.getChildren().add(label);
            }

            pageContent.getChildren().add(row);
        }

        return pageContent;
    }
}
