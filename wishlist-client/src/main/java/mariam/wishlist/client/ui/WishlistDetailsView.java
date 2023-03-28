package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import mariam.wishlist.client.ui.WishlistDetails;
import mariam.wishlist.core.model.Wishlist;

class WishlistDetailsView implements View {

    private Parent root;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField titleTextField;
    private Optional<Wishlist> optionalWishlist;

    WishlistDetailsView() {
        this(Optional.empty());
    }

    WishlistDetailsView(Wishlist wishlist) {
        this(Optional.of(wishlist));
    }

    private WishlistDetailsView(Optional<Wishlist> optionalWishlist) {
        this.optionalWishlist = optionalWishlist;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wishlist-details-pane.fxml"));
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
        titleTextField.skinProperty().addListener((observable) -> {
            titleTextField.requestFocus();
            optionalWishlist.ifPresent(wishlist -> titleTextField.selectAll());
        });

        optionalWishlist
                .ifPresent(wishlist -> {
                    titleTextField.setText(wishlist.getName());
                    descriptionTextArea.setText(wishlist.getDescription());
                });
    }

    Optional<WishlistDetails> getWishlistDetails() {
        Dialog<WishlistDetails> dialog = new Dialog<>();

        dialog.setTitle("New Wishlist");

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

        okButton.setText("Save");

        titleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null || newValue.isBlank());
        });

        okButton.setDisable(titleTextField.getText() == null || titleTextField.getText().isBlank());

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                return new WishlistDetails(titleTextField.getText(), descriptionTextArea.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

}
