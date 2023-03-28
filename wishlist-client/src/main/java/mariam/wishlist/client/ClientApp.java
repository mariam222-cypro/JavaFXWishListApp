package mariam.wishlist.client;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mariam.wishlist.client.ui.Notification;
import mariam.wishlist.client.ui.SignInView;

public class ClientApp extends Application {

    private final ObservableList<Notification> notifications = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new SignInView(primaryStage, notifications).getRoot());

        primaryStage.setScene(scene);
        primaryStage.setTitle("i-Wish");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
