package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mariam.wishlist.client.service.CrudService;
import mariam.wishlist.core.model.Friend;

public class SignInView implements View {

    private final Parent root;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signInButton;
    private final Stage primaryStage;
    private final CrudService crudService;
    private final ObservableList<Notification> notifications;

    public SignInView(Stage primaryStage, ObservableList<Notification> notifications) {
        this.primaryStage = primaryStage;
        this.notifications = notifications;

        crudService = new CrudService();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/signin-pane.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
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
        primaryStage.setMaximized(true);

        signInButton.addEventFilter(ActionEvent.ACTION, event -> {
            signIn(event);
        });

        signInButton.setDisable(true);

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            signInButton.setDisable(newValue == null || newValue.isBlank());
        });

        nameTextField.skinProperty().addListener(observable -> {
            nameTextField.requestFocus();
        });
    }

    @FXML
    protected void register(ActionEvent event) {
        primaryStage.setScene(new Scene(new RegisterView(primaryStage, notifications).getRoot()));
    }

    @FXML
    protected void exit(ActionEvent event) {
        System.exit(0);
    }

    private void signIn(ActionEvent event) {
        String name = nameTextField.getText();

        if (isValidFriend(name, passwordField.getText())) {
            primaryStage.setScene(new Scene(new MainView(primaryStage, findFriend(name), notifications).getRoot()));
        } else {
            errorLabel.setVisible(true);
            event.consume();
        }
    }

    private boolean isValidFriend(String name, String password) {

        List<Friend> allFriends = crudService.getAllFriends();

        return allFriends.stream()
                .map(f -> new Credentials(f.getName(), f.getPassword()))
                .anyMatch(c -> c.equals(new Credentials(name, password)));
    }

    private Friend findFriend(String name) {
        List<Friend> allFriends = crudService.getAllFriends();

        return allFriends.stream()
                .filter(f -> Objects.equals(f.getName(), name))
                .findFirst()
                .orElse(null);
    }

}
