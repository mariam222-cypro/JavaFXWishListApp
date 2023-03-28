package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NameChangeView implements View {

    private final Parent root;
    private final String oldName;
    private final Set<String> existingNames;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField newNameTextField;

    public NameChangeView(String oldName, Set<String> existingNames) {
        this.oldName = oldName;
        this.existingNames = new HashSet<>(existingNames);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/name-change-pane.fxml"));
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
        newNameTextField.skinProperty().addListener((observable) -> {
            newNameTextField.requestFocus();
            newNameTextField.selectAll();
        });

        newNameTextField.setText(oldName);
    }

    Optional<String> getNewName() {
        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Change Name");

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

        okButton.setText("Save");

        newNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue == null || newValue.isBlank());
            errorLabel.setVisible(false);
        });
        
        okButton.setDisable(newNameTextField.getText() == null || newNameTextField.getText().isBlank());

        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            if (existingNames.contains(newNameTextField.getText())) {
                errorLabel.setVisible(true);
                event.consume();
            }
        });

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                return newNameTextField.getText();
            }
            return null;
        });

        return dialog.showAndWait();
    }

}
