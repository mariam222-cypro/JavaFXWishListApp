package mariam.wishlist.client.ui;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

class ContributionDetailsView implements View {

    private static final Logger LOG = Logger.getLogger(ContributionDetailsView.class.getName());
    private Parent root;
    private final ContributionAmountDetails amountDetails;
    @FXML
    private TextField amountTextField;
    @FXML
    private Label contributionsLabel;

    ContributionDetailsView(ContributionAmountDetails amountDetails) {
        this.amountDetails = amountDetails;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/contribution-details-pane.fxml"));
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
        amountTextField.skinProperty().addListener((observable) -> {
            amountTextField.requestFocus();
        });
        contributionsLabel.setText(String.format("%s%.2f out of %s%.2f already contributed", currencySymbol(), amountDetails.contributedAmount(), currencySymbol(), amountDetails.totalAmount()));
    }

    Optional<BigDecimal> getContributedAmount() {
        Dialog<BigDecimal> dialog = new Dialog<>();

        dialog.setTitle("Make a Contribution");
        
        dialog.setDialogPane((DialogPane) root);
        
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("Contribute");

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                try {
                    return BigDecimal.valueOf(Double.parseDouble(amountTextField.getText()));
                } catch (NumberFormatException ex) {
                    LOG.log(Level.INFO, "Couldn't read amount: {0}", ex.getMessage());
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private static String currencySymbol() {
        return Currency.getInstance(Locale.getDefault()).getSymbol();
    }

}
