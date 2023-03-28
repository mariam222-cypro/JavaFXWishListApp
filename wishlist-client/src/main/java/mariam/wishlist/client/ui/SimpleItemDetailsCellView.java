package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.Currency;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import mariam.wishlist.core.model.Item;

class SimpleItemDetailsCellView implements View {

    private final Item item;
    private Parent root;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

    SimpleItemDetailsCellView(Item item) {
        this.item = item;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/simple-items-details-cell-pane.fxml"));
        fxmlLoader.setController(this);
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Parent getRoot() {
        return root;
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
