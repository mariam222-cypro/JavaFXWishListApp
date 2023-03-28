package mariam.wishlist.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.server.crud.ItemCrud;
import mariam.wishlist.server.database.DatabaseHandler;
import mariam.wishlist.server.ui.ConfirmationView;
import mariam.wishlist.server.ui.ConfirmationView.Response;
import mariam.wishlist.server.ui.ItemDetailsCellView;
import mariam.wishlist.server.ui.ItemDetailsView;
import mariam.wishlist.server.ui.MainView;

public class ServerApp extends Application {

    private static final Logger LOG = Logger.getLogger(ServerApp.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView mainView = new MainView();
        int port = 5000;
        DatabaseHandler databaseHandler = createDatabaseHandler();
        Server server = new Server(port, databaseHandler);
        ToggleButton startServerToggle = mainView.getStartServerToggleButton();
        Label serverStatusLabel = mainView.getServerStatusLabel();

        startServerToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            boolean selected = newValue;

            if (selected) {
                try {
                    server.start();
                    serverStatusLabel.setText(String.format("Server started on port %d", port));
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    serverStatusLabel.setText(ex.getMessage());
                }
            } else {
                try {
                    server.stop();
                    serverStatusLabel.setText("Server stopped");
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                    serverStatusLabel.setText(ex.getMessage());
                }
            }
        });

        ItemCrud itemCrud = new ItemCrud(databaseHandler);
        ObservableList<Item> items = FXCollections.observableArrayList(itemCrud.getAllItems());
        ListView<Item> itemsListView = mainView.getItemsListView();

        itemsListView.setItems(items);

        itemsListView.setCellFactory(listView -> {
            return new ListCell<Item>() {

                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        ItemDetailsCellView itemDetailsCellView = new ItemDetailsCellView(item);
                        HBox root = (HBox) itemDetailsCellView.getRoot();

                        root.maxWidthProperty().bind(Bindings.createDoubleBinding(
                                () -> (itemsListView.getWidth() - 18),
                                itemsListView.widthProperty()));

                        itemDetailsCellView.getEditButton().setOnAction(event -> {
                            new ItemDetailsView(item)
                                    .getItemDetails()
                                    .ifPresent(i -> {
                                        try {
                                            Item newItem = new Item(item.getId(), i.name(), i.description(), i.cost());
                                            itemCrud.updateItem(newItem);
                                            items.stream()
                                                    .filter(it -> it.getId() == newItem.getId())
                                                    .map(items::indexOf)
                                                    .findFirst()
                                                    .ifPresent(it -> items.set(it, newItem));
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    });
                        });

                        itemDetailsCellView.getDeleteButton().setOnAction(event -> {
                            String title = "Delete Item";
                            String question = String.format("Delete item \"%s\" permanently?", item.getName());
                            new ConfirmationView().getResponse(title, question)
                                    .filter(res -> res == Response.YES)
                                    .ifPresent(res -> {
                                        try {
                                            itemCrud.deleteItem(item.getId());
                                            items.remove(item);
                                        } catch (SQLException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    });
                        });

                        setGraphic(root);
                    }
                }

            };
        });

        mainView.getAddItemButton().setOnAction(event -> {
            new ItemDetailsView()
                    .getItemDetails()
                    .ifPresent(details -> {
                        try {
                            Item newItem = itemCrud.createItem(new Item(details.name(), details.description(), details.cost()));
                            items.add(newItem);

                            items.stream()
                                    .filter(i -> i.getId() == newItem.getId())
                                    .map(items::indexOf)
                                    .findFirst()
                                    .ifPresent(itemsListView.selectionModelProperty().get()::select);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
        });

        primaryStage.setOnCloseRequest(event -> {
            if (server.isRunning()) {
                try {
                    server.stop();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            }
        });

        Scene scene = new Scene(mainView.getRoot());

        primaryStage.setScene(scene);
        primaryStage.setTitle("i-Wish Server");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private DatabaseHandler createDatabaseHandler() throws SQLException {
        String dbUrl = "jdbc:mysql://localhost:3306/client_server_test";
        String dbUser = "admin";
        String dbPassword = "password";

        DatabaseHandler handler = new DatabaseHandler();
        handler.connect(dbUrl, dbUser, dbPassword);

        return handler;
    }

}
