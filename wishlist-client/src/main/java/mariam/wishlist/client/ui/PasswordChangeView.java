package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class PasswordChangeView implements View {

    private Parent root;
    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private Label errorLabel;
    @FXML
    private PasswordField newPasswordField;

    public PasswordChangeView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/password-change-pane.fxml"));
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
        newPasswordField.skinProperty().addListener((observable) -> {
            newPasswordField.requestFocus();
        });
    }

    Optional<String> getNewPassword() {
        Dialog<String> dialog = new Dialog<>();
        
        dialog.setTitle("Change Password");
        
        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

        okButton.setText("Save");
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmNewPasswordField.getText();

        newPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null || newValue.isBlank() || confirmPassword == null || confirmPassword.isBlank());
            errorLabel.setVisible(false);
        });
        confirmNewPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null || newValue.isBlank() || newPassword == null || newPassword.isBlank());
            errorLabel.setVisible(false);
        });
        
        okButton.setDisable(newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank());

        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (!Objects.equals(newPassword, confirmPassword)) {
                errorLabel.setVisible(true);
                event.consume();
            }
        });

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                return newPassword;
            }
            return null;
        });

        return dialog.showAndWait();
    }

}
