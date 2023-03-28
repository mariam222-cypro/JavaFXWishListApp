package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import mariam.wishlist.core.model.Item;

class ItemsSelectionView implements View {

    private Parent root;
    @FXML
    private ListView<Item> itemsListView;
    private final ObservableList<Item> items;

    ItemsSelectionView(Collection<Item> items) {
        this.items = FXCollections.observableArrayList(items);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/items-selection-pane.fxml"));
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
        itemsListView.setItems(items);
        itemsListView.setCellFactory(listView -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        SimpleItemDetailsCellView simpleItemDetailsCellView = new SimpleItemDetailsCellView(item);
                        HBox node = (HBox) simpleItemDetailsCellView.getRoot();
                        node.maxWidthProperty()
                                .bind(Bindings.createDoubleBinding(
                                        () -> (itemsListView.widthProperty().get() - 18),
                                        itemsListView.widthProperty()
                                ));
                        
                        setGraphic(node);
                    }
                }

            };
        });

    }

    Optional<List<Item>> getSelectedGiftItems() {
        Dialog<List<Item>> dialog = new Dialog<>();

        dialog.setTitle("Select Gift Items");

        DialogPane dialogPane = (DialogPane) root;

        dialog.setDialogPane(dialogPane);

        Button okButton = (Button) dialogPane.lookupButton(ButtonType.OK);

        okButton.setText("Save");

        dialog.setResultConverter(b -> {
            if (b == ButtonType.OK) {
                return itemsListView.selectionModelProperty().get().getSelectedItems();
            }
            return null;
        });

        return dialog.showAndWait();
    }

}
