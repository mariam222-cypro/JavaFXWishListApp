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

class FriendsConfirmationView implements View {

    private final Friend requestSender;
    @FXML
    private Label questionLabel;
    private Parent root;

    enum Response {
        YES, NO;
    }

    FriendsConfirmationView(Friend requestSender) {
        this.requestSender = requestSender;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/friends-confirmation-pane.fxml"));
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
        questionLabel.setText(String.format("%s wants to be friends with you. Accept?", requestSender.getName()));
    }

    Optional<Response> getResponse() {
        Dialog<Response> dialog = new Dialog<>();

        dialog.setTitle("Incoming Friend Request");

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        dialog.setResultConverter(type -> (type == ButtonType.YES) ? Response.YES : Response.NO);

        return dialog.showAndWait();
    }

}
