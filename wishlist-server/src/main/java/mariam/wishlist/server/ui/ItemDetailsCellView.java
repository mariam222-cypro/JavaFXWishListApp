package mariam.wishlist.server.ui;

import java.io.IOException;
import java.util.Currency;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mariam.wishlist.core.model.Item;

public class ItemDetailsCellView {

    private final Item item;
    private Parent root;
    @FXML
    private Button deleteButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button editButton;
    @FXML
    private Label titleLabel;

    public ItemDetailsCellView(Item item) {
        this.item = item;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/item-details-cell.fxml"));
        fxmlLoader.setController(this);
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Parent getRoot() {
        return root;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }

    @FXML
    protected void initialize() {
        titleLabel.setText(String.format("%s %s%.2f", item.getName(), currencySymbol(), item.getCost()));
        descriptionLabel.setText(item.getDescription());
    }

    private static String currencySymbol() {
        return Currency.getInstance(Locale.getDefault()).getSymbol();
    }
}
