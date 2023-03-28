package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import mariam.wishlist.core.model.Friend;

class DeleteFriendConfirmationView implements View {

    private final Friend friendToDelete;
    private Parent root;
    @FXML
    private Label questionLabel;

    enum Response {
        YES, NO;
    }

    DeleteFriendConfirmationView(Friend friendToDelete) {
        this.friendToDelete = friendToDelete;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/delete-friend-confirmation-pane.fxml"));
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
        questionLabel.setText(String.format("Delete %s's account permanently?", friendToDelete.getName()));
    }

    Optional<Response> getResponse() {
        Dialog<Response> dialog = new Dialog<>();

        dialog.setTitle("Delete Profile");

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        dialog.setResultConverter(type -> (type == ButtonType.YES) ? Response.YES : Response.NO);

        return dialog.showAndWait();
    }
}
