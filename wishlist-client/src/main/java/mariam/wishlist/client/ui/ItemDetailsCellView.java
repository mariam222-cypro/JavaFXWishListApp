package mariam.wishlist.client.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mariam.wishlist.core.model.Item;

class ItemDetailsCellView implements View {

    private final Item item;
    private final BigDecimal contributedAmount;
    private final ActionType actionType;
    private Parent root;
    @FXML
    private Button contributeButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label titleLabel;

    enum ActionType {
        DELETE, CONTRIBUTE;
    }

    ItemDetailsCellView(Item item, BigDecimal contributedAmount, ActionType actionType) {
        this.item = item;
        this.contributedAmount = contributedAmount;
        this.actionType = actionType;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/items-details-cell-pane.fxml"));
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
        titleLabel.setText(String.format("%s %s%.2f/%s%.2f", item.getName(), currencySymbol(), contributedAmount, currencySymbol(), item.getCost()));
        descriptionLabel.setText(item.getDescription());

        deleteButton.visibleProperty().bind(contributeButton.visibleProperty().not());
        
        contributeButton.setVisible(actionType == ActionType.CONTRIBUTE);
    }

    Button getDeleteButton() {
        return deleteButton;
    }

    Button getContributeButton() {
        return contributeButton;
    }

    private static String currencySymbol() {
        return Currency.getInstance(Locale.getDefault()).getSymbol();
    }

}
