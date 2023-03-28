package mariam.wishlist.server.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mariam.wishlist.core.model.Item;

public class ItemDetailsView {

    private static final Logger LOG = Logger.getLogger(ItemDetailsView.class.getName());
    private Parent root;
    @FXML
    private TextField costTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField nameTextField;
    private Optional<Item> optionalItem;

    public ItemDetailsView() {
        this(Optional.empty());
    }

    public ItemDetailsView(Item item) {
        this(Optional.of(item));
    }

    private ItemDetailsView(Optional<Item> optionalItem) {
        this.optionalItem = optionalItem;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/item-details-pane.fxml"));
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

    public Optional<ItemDetails> getItemDetails() {
        Dialog<ItemDetails> dialog = new Dialog<>();

        dialog.setTitle("Item Details");

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

        okButton.setText("Save");

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null || newValue.isBlank());
        });
        
        okButton.setDisable(nameTextField.getText() == null || nameTextField.getText().isBlank());

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                BigDecimal cost = BigDecimal.ZERO;
                try {
                    cost = BigDecimal.valueOf(Double.parseDouble(costTextField.getText()));
                } catch (NumberFormatException ex) {
                    LOG.log(Level.WARNING, "Could't get a double: {0}", ex.getMessage());
                }
                return new ItemDetails(nameTextField.getText(), descriptionTextArea.getText(), cost);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    @FXML
    protected void initialize() {
        nameTextField.skinProperty().addListener((observable) -> {
            nameTextField.requestFocus();
            optionalItem.ifPresent(item -> nameTextField.selectAll());
        });

        optionalItem.ifPresent(item -> {
            nameTextField.setText(item.getName());
            costTextField.setText(item.getCost().toString());
            descriptionTextArea.setText(item.getDescription());
        });
    }
}
