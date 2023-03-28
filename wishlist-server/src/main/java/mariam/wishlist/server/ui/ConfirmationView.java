package mariam.wishlist.server.ui;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

public class ConfirmationView {

    private Parent root;
    @FXML
    private Label questionLabel;

    public enum Response {
        YES, NO;
    }

    public ConfirmationView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/confirmation-pane.fxml"));
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

    public Optional<Response> getResponse(String title, String question) {
        Dialog<Response> dialog = new Dialog<>();

        dialog.setTitle(title);
        questionLabel.setText(question);

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        dialog.setResultConverter(type -> (type == ButtonType.YES) ? Response.YES : Response.NO);

        return dialog.showAndWait();
    }
}
