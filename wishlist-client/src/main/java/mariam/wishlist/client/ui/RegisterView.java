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

public class RegisterView implements View {

    private final CrudService crudService;
    private final Stage primaryStage;
    private Parent root;
    @FXML
    private TextField nameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerButton;
    @FXML
    private Label errorLabel;
    private final ObservableList<Notification> notifications;

    public RegisterView(Stage primaryStage, ObservableList<Notification> notifications) {
        this.primaryStage = primaryStage;
        this.notifications = notifications;

        crudService = new CrudService();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/register-pane.fxml"));
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
        primaryStage.setMaximized(true);

        registerButton.setDisable(true);

        registerButton.addEventFilter(ActionEvent.ACTION, event -> {
            register(event);
        });

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue == null || newValue.isBlank() || passwordField.getText() == null || passwordField.getText().isBlank());
        });

        nameTextField.skinProperty().addListener(observable -> {
            nameTextField.requestFocus();
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue == null || newValue.isBlank() || nameTextField.getText() == null || nameTextField.getText().isBlank());
        });
    }

    @FXML
    protected void register(ActionEvent event) {
        String name = nameTextField.getText();
        String password = passwordField.getText();
        if (friendDoesntExist(name)) {
            Friend registeredFriend = registerFriend(name, password);
            if (registeredFriend != null) {
                primaryStage.setScene(new Scene(new SignInView(primaryStage, notifications).getRoot()));
            }
        } else {
            errorLabel.setVisible(true);
            event.consume();
        }
    }

    @FXML
    protected void cancelRegistration(ActionEvent event) {
        primaryStage.setScene(new Scene(new SignInView(primaryStage, notifications).getRoot()));
    }

    private boolean friendDoesntExist(String name) {
        List<Friend> allFriends = crudService.getAllFriends();

        return allFriends.stream().map(Friend::getName).noneMatch(s -> Objects.equals(s, name));
    }

    private Friend registerFriend(String name, String password) {
        return crudService.createFriend(new Friend(name, password));
    }

}
