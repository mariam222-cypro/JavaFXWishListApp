package mariam.wishlist.client.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import mariam.wishlist.core.model.Wishlist;

class WishlistDetailsCellView implements View {

    private Parent root;
    private final Wishlist wishlist;
    @FXML
    private Button deleteButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button editButton;
    @FXML
    private Label titleLabel;

    WishlistDetailsCellView(Wishlist wishlist) {
        this.wishlist = wishlist;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/wishlist-details-cell.fxml"));
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
        titleLabel.setText(wishlist.getName());
        descriptionLabel.setText(wishlist.getDescription());
    }

    Button getEditButton() {
        return editButton;
    }

    Button getDeleteButton() {
        return deleteButton;
    }

}
