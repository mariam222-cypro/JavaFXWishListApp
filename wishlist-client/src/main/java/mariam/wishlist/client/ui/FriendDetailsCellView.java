package mariam.wishlist.client.ui;

import java.io.IOException;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;

class FriendDetailsCellView implements View {

    private static final Logger LOG = Logger.getLogger(FriendDetailsCellView.class.getName());
    private final Friend friendRequestReceiver;
    private final ObjectProperty<FriendRequest> friendRequestProperty;
    private Parent root;
    @FXML
    private Label nameLabel;
    @FXML
    private Hyperlink requestActionsLink;

    FriendDetailsCellView(ObjectProperty<FriendRequest> friendRequestProperty, Friend friendRequestReceiver) {
        this.friendRequestProperty = friendRequestProperty;
        this.friendRequestReceiver = friendRequestReceiver;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/friend-details-cell.fxml"));
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
        nameLabel.setText(friendRequestReceiver.getName());

        updateRequestLink();

        friendRequestProperty.addListener((observable, oldValue, newValue) -> updateRequestLink());
    }

    Hyperlink getRequestActionsLink() {
        return requestActionsLink;
    }

    private void updateRequestLink() throws AssertionError {
        if (friendRequestProperty.getValue() != null) {
            switch (friendRequestProperty.getValue().getStatus()) {
                case CONFIRMED -> {
                    requestActionsLink.setText("Unfriend");
                }
                case DECLINED -> {
                    requestActionsLink.setText("Send friend request");
                }
                case SENT -> {
                    if (friendRequestProperty.getValue().getReceiver().getId() == friendRequestReceiver.getId()) {
                        requestActionsLink.setText("Cancel friend request");
                    } else {
                        requestActionsLink.setText("Accept/Decline friend request");
                    }
                }
            };
        } else {
            requestActionsLink.setText("Send friend request");
        }

    }

}
