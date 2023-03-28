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

class ConfirmationView implements View {

    private Parent root;
    @FXML
    private Label questionLabel;

    enum Response {
        YES, NO;
    }

    ConfirmationView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/confirmation-pane.fxml"));
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

    Optional<Response> getResponse(String title, String question) {
        Dialog<Response> dialog = new Dialog<>();

        dialog.setTitle(title);
        questionLabel.setText(question);

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        dialog.setResultConverter(type -> (type == ButtonType.YES) ? Response.YES : Response.NO);

        return dialog.showAndWait();
    }
}
