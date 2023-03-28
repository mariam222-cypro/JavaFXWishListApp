package mariam.wishlist.server.ui;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import mariam.wishlist.core.model.Item;

public class MainView {

    private Parent root;
    @FXML
    private ToggleButton startServerToggleButton;
    @FXML
    private Label startServerLabel;
    @FXML
    private Label serverStatusLabel;
    @FXML
    private Button addItemButton;
    @FXML
    private ListView<Item> itemsListView;

    public MainView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-pane.fxml"));
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

    public ToggleButton getStartServerToggleButton() {
        return startServerToggleButton;
    }

    public Label getServerStatusLabel() {
        return serverStatusLabel;
    }

    public Button getAddItemButton() {
        return addItemButton;
    }

    public ListView<Item> getItemsListView() {
        return itemsListView;
    }

    @FXML
    protected void initialize() {
        updateStartServerText(startServerToggleButton.isSelected());

        startServerToggleButton.selectedProperty().addListener((observable, oldValue, newValue) -> updateStartServerText(newValue));
    }

    private void updateStartServerText(Boolean newValue) {
        startServerLabel.setText(String.format("%s Server", newValue ? "Stop" : "Start"));
    }
}
