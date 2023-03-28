package mariam.wishlist.client.ui;

import java.io.IOException;
import java.math.BigDecimal;
import static java.math.BigDecimal.ZERO;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import mariam.wishlist.client.service.CrudService;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.core.model.Wishlist;

public class MainView implements View {

    private static final Logger LOG = Logger.getLogger(MainView.class.getName());
    private final ObjectProperty<Friend> friendProperty;
    private final ObservableList<Wishlist> wishlists;
    private final ObservableList<Gift> gifts;
    private final ObservableList<Item> allGiftItems;
    private final ObservableList<Notification> notifications;
    private final CrudService crudService;
    private Parent root;
    private final Stage primaryStage;
    @FXML
    private ToggleGroup menuRadioButtonsGroup;
    @FXML
    private RadioButton wishlistsRadioButon;
    @FXML
    private RadioButton itemsRadioButton;
    @FXML
    private RadioButton profileRadioButton;
    @FXML
    private Button notificationsButton;
    @FXML
    private RadioButton friendsRadioButton;
    @FXML
    private StackPane contentStackpane;
    @FXML
    private BorderPane friendsPane;
    @FXML
    private BorderPane profilePane;
    // Items
    @FXML
    private BorderPane itemsPane;
    @FXML
    private ListView<Item> allGiftItemsListView;
    // Wishlists
    @FXML
    private BorderPane wishlistsPane;
    @FXML
    private Label wishlistsHeaderLabel;
    @FXML
    private VBox wishlistActionsBox;
    @FXML
    private ComboBox<Friend> wishlistOwnerCombobox;
    @FXML
    private Button addWishlistButton;
    @FXML
    private Button addGiftButton;
    @FXML
    private ListView<Wishlist> wishlistsListView;
    @FXML
    private ListView<Gift> giftItemsListView;
    // Friends
    @FXML
    private ListView<Friend> friendsListView;
    // Profile
    @FXML
    private Label profileHeaderLabel;
    @FXML
    private Label profileNameLabel;
    @FXML
    private Button editProfileNameButton;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private Label shownPasswordLabel;
    @FXML
    private ToggleButton showPasswordToggleButton;
    @FXML
    private Button editPasswordButton;

    public MainView(Stage primaryStage, Friend friend, ObservableList<Notification> notifications) {
        this.primaryStage = primaryStage;
        this.notifications = notifications;

        crudService = new CrudService();
        friendProperty = new SimpleObjectProperty<>(friend);
        wishlists = FXCollections.observableArrayList(getWishListsFor(friend));
        gifts = FXCollections.observableArrayList();
        allGiftItems = FXCollections.observableArrayList(getAllGiftItems());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-pane.fxml"));
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

        profileRadioButton.textProperty().bind(Bindings.createStringBinding(() -> {
            String newName = friendProperty.getValue().getName();
            LOG.log(Level.INFO, "friend name changed to: {0}", newName);
            return newName;
        }, friendProperty));

        // Notifications
        notificationsButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notifications");
            String createNotificationText = createNotificationText();
            alert.setHeaderText((createNotificationText == null || createNotificationText.isBlank()) ? "No gifts bought" : "Gifts have been bought");
            alert.setContentText(createNotificationText);
            alert.showAndWait();
        });

        wishlistsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Wishlist selectedWishlist = newValue;
            updateNotifications(selectedWishlist);
        });
        
        updateNotifications(wishlistsListView.getSelectionModel().getSelectedItem());

        // Pane selection and switching
        selectAndSetVisiblePane();
        menuRadioButtonsGroup
                .getToggles()
                .stream()
                .map(RadioButton.class::cast)
                .forEach(r -> r.selectedProperty().addListener((observable, oldValue, newValue) -> selectAndSetVisiblePane()));

        // Wishlists pane
        wishlistsPane.visibleProperty().addListener((observable, oldValue, newValue) -> {
            boolean visible = newValue;

            if (visible) {
                refreshWishlistsView();

            }
        });
        wishlistOwnerCombobox.selectionModelProperty().get().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                wishlistsHeaderLabel.setText(String.format("%s Wishlists", newValue.getName()));

                wishlists.setAll(getWishListsFor(newValue));

                Platform.runLater(() -> wishlistsListView.selectionModelProperty().get().select(0));
            }

            boolean show = (newValue != null) && (newValue.getId() == friendProperty.getValue().getId());

            addWishlistButton.setVisible(show);
            addGiftButton.setVisible(show);
        });
        wishlistOwnerCombobox.setConverter(new StringConverter<Friend>() {
            @Override
            public String toString(Friend object) {
                return (object != null) ? ((object.getId() == friendProperty.getValue().getId()) ? "Me" : object.getName()) : null;
            }

            @Override
            public Friend fromString(String string) {
                return null;
            }
        });
        wishlistsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                FilteredList<Gift> filteredGifts = new FilteredList<>(FXCollections.observableArrayList(getGiftsFor(newValue)), g -> g.getItem().getCost().doubleValue() > getContributedAmountFor(g).doubleValue());
                gifts.setAll(filteredGifts);
            } else {
                gifts.clear();
            }
        });
        wishlistsListView.setItems(wishlists);
        wishlistsListView.setCellFactory(listView -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Wishlist wishlist, boolean empty) {
                    super.updateItem(wishlist, empty);

                    if (wishlist == null || empty) {
                        setGraphic(null);
                    } else {
                        WishlistDetailsCellView wishlistDetailsCellView = new WishlistDetailsCellView(wishlist);

                        Button deleteButton = wishlistDetailsCellView.getDeleteButton();
                        Button editButton = wishlistDetailsCellView.getEditButton();
                        
                        deleteButton.setVisible(wishlist.getOwner().getId() == friendProperty.getValue().getId());
                        editButton.setVisible(wishlist.getOwner().getId() == friendProperty.getValue().getId());
                        
                        editButton.setOnAction(event -> {
                            WishlistDetailsView wishlistDetailsView = new WishlistDetailsView(wishlist);
                            wishlistDetailsView.getWishlistDetails()
                                    .ifPresent(details -> {
                                        Wishlist newWishlist = new Wishlist(wishlist.getId(), wishlist.getOwner(), details.title(), details.description());
                                        crudService.updateWishlist(newWishlist);
                                        wishlists.stream()
                                                .filter(w -> w.getId() == wishlist.getId())
                                                .map(wishlists::indexOf)
                                                .findFirst()
                                                .ifPresent(i -> {
                                                    wishlists.set(i, newWishlist);
                                                    wishlistsListView.selectionModelProperty().get().select(i);
                                                });
                                    });
                        });

                        deleteButton.setOnAction(event -> {
                            String question = String.format("Delete wishlist \"%s\" permanently?", wishlist.getName());
                            String title = "Delete Wishlist";
                            new ConfirmationView()
                                    .getResponse(title, question)
                                    .filter(response -> response == ConfirmationView.Response.YES)
                                    .ifPresent(response -> {
                                        crudService.deleteWishlist(wishlist);
                                        wishlists.remove(wishlist);
                                    });
                        });

                        HBox node = (HBox) wishlistDetailsCellView.getRoot();
                        node.maxWidthProperty()
                                .bind(Bindings.createDoubleBinding(
                                        () -> (wishlistsListView.widthProperty().get() - 18),
                                        wishlistsListView.widthProperty()
                                ));

                        setGraphic(node);
                    }
                }

            };
        });

        giftItemsListView.setItems(gifts);
        giftItemsListView.setCellFactory(listView -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Gift gift, boolean empty) {
                    super.updateItem(gift, empty);

                    if (gift == null || empty) {
                        setGraphic(null);
                    } else {
                        BigDecimal contributedAmount = getContributedAmountFor(gift);
                        boolean canDelete = gift.getWishlist().getOwner().getId() == friendProperty.getValue().getId();
                        ItemDetailsCellView.ActionType actionType = canDelete ? ItemDetailsCellView.ActionType.DELETE : ItemDetailsCellView.ActionType.CONTRIBUTE;
                        ItemDetailsCellView itemDetailsCellView = new ItemDetailsCellView(gift.getItem(), contributedAmount, actionType);

                        switch (actionType) {
                            case DELETE -> {
                                itemDetailsCellView.getDeleteButton().setOnAction(event -> {
                                    new ConfirmationView().getResponse("Remove Gift Item", String.format("Remove \"%s\" from the wishlist?", gift.getItem()))
                                            .filter(res -> res == ConfirmationView.Response.YES)
                                            .ifPresent(res -> {
                                                crudService.deleteGift(gift);
                                                gifts.remove(gift);
                                            });
                                });
                            }
                            case CONTRIBUTE -> {
                                itemDetailsCellView.getContributeButton().setOnAction(event -> {
                                    ContributionAmountDetails amountDetails = new ContributionAmountDetails(gift.getItem().getCost(), contributedAmount);
                                    new ContributionDetailsView(amountDetails).getContributedAmount()
                                            .ifPresent(amount -> {
                                                crudService.createContribution(new Contribution(gift, friendProperty.getValue(), amount));
                                                updateItem(gift, empty);
                                            });
                                });
                            }

                            default ->
                                throw new AssertionError(actionType.name());
                        }

                        HBox cell = (HBox) itemDetailsCellView.getRoot();
                        cell.maxWidthProperty()
                                .bind(Bindings.createDoubleBinding(
                                        () -> (giftItemsListView.widthProperty().get() - 18),
                                        giftItemsListView.widthProperty()
                                ));

                        setGraphic(cell);
                    }
                }

            };
        });

        refreshWishlistsView();

        // Items pane
        allGiftItemsListView.setItems(allGiftItems);
        allGiftItemsListView.setCellFactory(listView -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(Item item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        SimpleItemDetailsCellView simpleItemDetailsCellView = new SimpleItemDetailsCellView(item);
                        HBox cell = (HBox) simpleItemDetailsCellView.getRoot();
                        cell.maxWidthProperty()
                                .bind(Bindings.createDoubleBinding(
                                        () -> (allGiftItemsListView.widthProperty().get() - 18),
                                        allGiftItemsListView.widthProperty()
                                ));
                        
                        setGraphic(cell);
                    }
                }

            };
        });

        // Friends pane
        friendsListView.setItems(FXCollections.observableList(getAllOtherFriends()));
        friendsListView.setCellFactory(listView -> {
            return new ListCell<>() {

                @Override
                protected void updateItem(Friend friend, boolean empty) {
                    super.updateItem(friend, empty);

                    if (friend == null || empty) {
                        setGraphic(null);
                    } else {
                        Friend sender = friendProperty.getValue();
                        Friend receiver = friend;
                        ObjectProperty<FriendRequest> friendRequestProperty = new SimpleObjectProperty<>(findAnyFriendRequest(sender, receiver));
                        FriendDetailsCellView friendDetailsCellView = new FriendDetailsCellView(friendRequestProperty, receiver);
                        friendDetailsCellView.getRequestActionsLink()
                                .setOnAction(event -> {
                                    FriendRequest request = friendRequestProperty.getValue();

                                    if (request != null) {
                                        FriendRequest.Status status = request.getStatus();
                                        switch (status) {
                                            case CONFIRMED -> {
                                                deleteFriendRequest(sender, receiver);
                                                friendRequestProperty.setValue(null);
                                            }
                                            case DECLINED -> {
                                                FriendRequest newRequest = new FriendRequest(request.getId(), sender, receiver, FriendRequest.Status.SENT);
                                                updateFriendRequest(newRequest);
                                                friendRequestProperty.setValue(newRequest);
                                            }
                                            case SENT -> {
                                                if (sender.getId() == request.getSender().getId()) {
                                                    deleteFriendRequest(sender, receiver);
                                                    friendRequestProperty.setValue(null);
                                                } else {
                                                    new FriendsConfirmationView(receiver)
                                                            .getResponse()
                                                            .ifPresent(response -> {
                                                                FriendRequest.Status selectedStatus = switch (response) {
                                                                    case YES ->
                                                                        FriendRequest.Status.CONFIRMED;
                                                                    case NO ->
                                                                        FriendRequest.Status.DECLINED;
                                                                    default ->
                                                                        throw new AssertionError();
                                                                };
                                                                FriendRequest newRequest = new FriendRequest(request.getId(), receiver, sender, selectedStatus);
                                                                updateFriendRequest(newRequest);
                                                                friendRequestProperty.setValue(newRequest);
                                                            });
                                                }
                                            }
                                            default ->
                                                throw new AssertionError(status.name());
                                        }
                                    } else {
                                        FriendRequest newFriendRequest = createFriendRequest(sender, receiver, FriendRequest.Status.SENT);
                                        friendRequestProperty.setValue(newFriendRequest);
                                    }
                                });

                        setGraphic(friendDetailsCellView.getRoot());
                    }
                }

                private List<FriendRequest> getAllFriendRequests() {
                    return crudService.getAllFriendRequests();
                }

                private FriendRequest createFriendRequest(Friend sender, Friend receiver, FriendRequest.Status status) {
                    return crudService.createFriendRequest(new FriendRequest(sender, receiver, status));
                }

                private void deleteFriendRequest(Friend sender, Friend receiver) {
                    getAllFriendRequests()
                            .stream()
                            .filter(req -> req.getSender().getId() == sender.getId() && req.getReceiver().getId() == receiver.getId())
                            .findFirst()
                            .ifPresent(req -> crudService.deleteFriendRequest(req));
                }

                private FriendRequest findAnyFriendRequest(Friend sender, Friend receiver) {
                    return getAllFriendRequests()
                            .stream()
                            .filter(req -> {
                                return (req.getSender().getId() == sender.getId() && req.getReceiver().getId() == receiver.getId())
                                        || (req.getSender().getId() == receiver.getId() && req.getReceiver().getId() == sender.getId());
                            })
                            .findFirst()
                            .orElse(null);
                }

                private void updateFriendRequest(FriendRequest request) {
                    crudService.updateFriendRequest(request);
                }

            };
        });

        // Profile pane
        profileHeaderLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String newName = friendProperty.getValue().getName();
            LOG.log(Level.INFO, "friend name changed to: {0}", newName);
            return String.format("%s's Profile", newName);
        }, friendProperty));
        profileNameLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            String newName = friendProperty.getValue().getName();
            LOG.log(Level.INFO, "friend name changed to: {0}", newName);
            return newName;
        }, friendProperty));
        editProfileNameButton.setOnAction(event -> {
            new NameChangeView(profileNameLabel.getText(), getAllExistingFriendNames())
                    .getNewName()
                    .ifPresent(newName -> {
                        Friend oldFriend = friendProperty.getValue();
                        Friend newFriend = new Friend(oldFriend.getId(), newName, oldFriend.getPassword());

                        crudService.updateFriend(newFriend);
                        friendProperty.setValue(newFriend);
                    });
        });
        currentPasswordField.textProperty().bind(Bindings.createStringBinding(() -> {
            String newPassword = friendProperty.getValue().getPassword();
            LOG.log(Level.INFO, "friend password changed to: {0}", newPassword);
            return newPassword;
        }, friendProperty));
        shownPasswordLabel.textProperty().bind(currentPasswordField.textProperty());
        shownPasswordLabel.visibleProperty().bind(currentPasswordField.visibleProperty().not());
        currentPasswordField.visibleProperty().bind(showPasswordToggleButton.selectedProperty().not());
        editPasswordButton.setOnAction(event -> {
            new PasswordChangeView()
                    .getNewPassword()
                    .ifPresent(password -> {
                        Friend oldFriend = friendProperty.getValue();
                        Friend newFriend = new Friend(oldFriend.getId(), oldFriend.getName(), password);

                        crudService.updateFriend(newFriend);
                        friendProperty.setValue(newFriend);
                    });
        });
    }

    private void updateNotifications(Wishlist selectedWishlist) {
        if (selectedWishlist != null) {
            List<Gift> giftsForWishlist = getGiftsFor(selectedWishlist);

            List<Notification> notifys = giftsForWishlist.stream()
                    .map(g -> {
                        return new Notification(
                                g.getWishlist().getOwner(),
                                crudService.getAllContributions()
                                        .stream()
                                        .filter(c -> c.getGift().getId() == g.getId())
                                        .map(Contribution::getContributor).toList(),
                                g, getContributedAmountFor(g));
                    })
                    .distinct()
                    .toList();

            notifys.stream()
                    .filter(n -> n.amountContributed().doubleValue() >= n.giftItem().getItem().getCost().doubleValue())
                    .filter(n -> !n.contributors().isEmpty()).filter(n -> !notifications.contains(n))
                    .forEach(notifications::add);

            long nc = notifications.stream()
                    .filter(n -> n.receiver().getId() == friendProperty.getValue().getId()
                    || n.contributors().stream().allMatch(f -> f.getId() == friendProperty.getValue().getId()))
                    .count();
            notificationsButton.setText(String.format("(%d)", nc));
        }
    }

    private void refreshWishlistsView() {
        wishlistOwnerCombobox.setItems(FXCollections.observableArrayList(getAllActualFriends()));
        Platform.runLater(() -> wishlistOwnerCombobox.selectionModelProperty().get().select(0));
    }

    @FXML
    protected void signOut(ActionEvent event) {
        primaryStage.setScene(new Scene(new SignInView(primaryStage, notifications).getRoot()));
    }

    @FXML
    protected void deleteProfile(ActionEvent event) {
        Friend friendToDelete = friendProperty.getValue();

        new DeleteFriendConfirmationView(friendToDelete)
                .getResponse()
                .filter(r -> r == DeleteFriendConfirmationView.Response.YES)
                .ifPresent(r -> {
                    crudService.deleteFriend(friendToDelete);
                    primaryStage.setScene(new Scene(new SignInView(primaryStage, notifications).getRoot()));
                });
    }

    @FXML
    protected void createWishlist(ActionEvent event) {
        new WishlistDetailsView()
                .getWishlistDetails()
                .map(wishlist -> crudService.createWishlist(new Wishlist(friendProperty.getValue(), wishlist.title(), wishlist.description())))
                .ifPresent(wishlist -> {
                    wishlists.add(wishlist);
                    wishlists.stream()
                            .filter(w -> w.getId() == wishlist.getId())
                            .map(wishlists::indexOf)
                            .findFirst()
                            .ifPresent(wishlistsListView.selectionModelProperty().get()::select);
                });
    }

    @FXML
    protected void createGift(ActionEvent event) {
        List<Gift> allGifts = getAllGifts();
        ItemsSelectionView itemsSelectionView = new ItemsSelectionView(getAllGiftItems());
        itemsSelectionView
                .getSelectedGiftItems()
                .ifPresent(items -> {
                    for (Item item : items) {
                        Wishlist selectedWishlist = wishlistsListView.getSelectionModel().getSelectedItem();
                        boolean doesntExist = allGifts
                                .stream()
                                .noneMatch(g -> g.getItem().getId() == item.getId() && g.getWishlist().getId() == selectedWishlist.getId());

                        if (doesntExist) {
                            Gift newGift = crudService.createGift(new Gift(item, selectedWishlist));
                            gifts.add(newGift);
                            giftItemsListView.selectionModelProperty().get().select(newGift);
                        }
                    }
                });

    }

    private void selectAndSetVisiblePane() {
        RadioButton selectedRadioButton = menuRadioButtonsGroup
                .getToggles()
                .stream()
                .filter(Toggle::isSelected)
                .map(RadioButton.class::cast)
                .findFirst()
                .orElse(wishlistsRadioButon);

        if (selectedRadioButton.equals(wishlistsRadioButon)) {
            setVisiblePane(wishlistsPane);
        } else if (selectedRadioButton.equals(itemsRadioButton)) {
            setVisiblePane(itemsPane);
        } else if (selectedRadioButton.equals(friendsRadioButton)) {
            setVisiblePane(friendsPane);
        } else if (selectedRadioButton.equals(profileRadioButton)) {
            setVisiblePane(profilePane);
        }
    }

    private void setVisiblePane(BorderPane pane) {
        contentStackpane.getChildrenUnmodifiable()
                .stream()
                .forEach(node -> node.setVisible(false));
        contentStackpane.getChildrenUnmodifiable()
                .stream()
                .filter(pane::equals)
                .forEach(node -> node.setVisible(true));
    }

    private Set<String> getAllExistingFriendNames() {
        return crudService.getAllFriends()
                .stream()
                .map(Friend::getName)
                .collect(Collectors.toSet());
    }

    private List<Friend> getAllOtherFriends() {
        return crudService.getAllFriends()
                .stream()
                .filter(friend -> !Objects.equals(friend, friendProperty.getValue()))
                .toList();

    }

    private List<Wishlist> getWishListsFor(Friend friend) {
        return crudService.getAllWishlists()
                .stream()
                .filter(w -> w.getOwner().getId() == friend.getId())
                .toList();
    }

    private List<Friend> getAllActualFriends() {
        Stream<Friend> others = crudService
                .getAllFriendRequests()
                .stream()
                .filter(req -> req.getStatus() == FriendRequest.Status.CONFIRMED)
                .filter(req -> {
                    return (req.getReceiver().getId() == friendProperty.getValue().getId())
                            || (req.getSender().getId() == friendProperty.getValue().getId());
                })
                .flatMap(req -> Stream.of(req.getSender(), req.getReceiver()))
                .distinct();

        return Stream.concat(Stream.of(friendProperty.getValue()), others)
                .distinct()
                .toList();
    }

    private Collection<Item> getAllGiftItems() {
        return crudService.getAllItems();
    }

    private List<Gift> getAllGifts() {
        return crudService.getAllGifts();
    }

    private List<Gift> getGiftsFor(Wishlist wishlist) {
        return getAllGifts()
                .stream()
                .filter(g -> g.getWishlist().getId() == wishlist.getId())
                .toList();
    }

    private BigDecimal getContributedAmountFor(Gift gift) {
        return crudService.getAllContributions()
                .stream()
                .filter(contribution -> contribution.getGift().getId() == gift.getId())
                .map(Contribution::getAmount)
                .reduce(ZERO, BigDecimal::add);
    }

    private String createNotificationText() {
        return notifications.stream()
                .map(n -> {
                    String names = n.contributors().stream().map(f -> f.getName()).collect(Collectors.joining(", "));
                    return String.format("> %s have gifted %s with \"%s\" after paying %.2f", names, n.receiver().getName(), n.giftItem().getItem().getName(), n.amountContributed());
                })
                .collect(Collectors.joining(System.lineSeparator()));
    }

}
