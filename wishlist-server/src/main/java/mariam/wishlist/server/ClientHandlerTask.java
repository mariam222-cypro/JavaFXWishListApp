package mariam.wishlist.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.server.crud.ContributionCrud;
import mariam.wishlist.server.crud.FriendCrud;
import mariam.wishlist.server.crud.FriendRequestCrud;
import mariam.wishlist.server.crud.GiftCrud;
import mariam.wishlist.server.crud.ItemCrud;
import mariam.wishlist.server.crud.WishlistCrud;
import mariam.wishlist.server.database.DatabaseHandler;

class ClientHandlerTask implements Runnable {

    private static final Logger LOG = Logger.getLogger(ClientHandlerTask.class.getName());
    private final UUID clientId;
    private final Socket clientSocket;
    private boolean running;
    private final Server server;
    private final FriendCrud friendCrud;
    private final FriendRequestCrud friendRequestCrud;
    private final WishlistCrud wishlistCrud;
    private final ItemCrud itemCrud;
    private final GiftCrud giftCrud;
    private final ContributionCrud contributionCrud;

    ClientHandlerTask(UUID clientId, Socket clientSocket, Server server) {
        this.clientId = clientId;
        this.clientSocket = clientSocket;
        this.server = server;

        running = true;

        DatabaseHandler databaseHandler = server.getDatabaseHandler();

        itemCrud = new ItemCrud(databaseHandler);
        friendCrud = new FriendCrud(databaseHandler);
        friendRequestCrud = new FriendRequestCrud(databaseHandler);
        wishlistCrud = new WishlistCrud(databaseHandler);
        giftCrud = new GiftCrud(databaseHandler);
        contributionCrud = new ContributionCrud(databaseHandler);
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()); ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
            // handle the client request
            while (running) {
                String command = (String) in.readObject();

                switch (command) {
                    case "getAllFriends" -> {
                        out.writeObject(friendCrud.getAllFriends());
                    }
                    case "createFriend" -> {
                        LOG.log(Level.INFO, "Creating friend...");
                        Friend friendToCreate = (Friend) in.readObject();
                        LOG.log(Level.INFO, "Friend to create: {0}", friendToCreate);
                        Friend createdFriend = friendCrud.createFriend(friendToCreate);
                        LOG.log(Level.INFO, "Created friend: {0}", createdFriend);
                        out.writeObject(createdFriend);
                    }
                    case "updateFriend" -> {
                        LOG.log(Level.INFO, "Updating friend...");
                        Friend friendToUpdate = (Friend) in.readObject();
                        LOG.log(Level.INFO, "Friend to update: {0}", friendToUpdate);
                        friendCrud.updateFriend(friendToUpdate);
                        LOG.log(Level.INFO, "Updated friend: {0}", friendToUpdate);
                    }
                    case "deleteFriend" -> {
                        LOG.log(Level.INFO, "Deleting friend...");
                        Friend friendToDelete = (Friend) in.readObject();
                        LOG.log(Level.INFO, "Friend to delete: {0}", friendToDelete);
                        friendCrud.deleteFriend(friendToDelete.getId());
                        LOG.log(Level.INFO, "Deleted friend: {0}", friendToDelete);
                    }
                    case "getAllFriendRequests" -> {
                        out.writeObject(friendRequestCrud.getAllFriendRequests());
                    }
                    case "createFriendRequest" -> {
                        LOG.log(Level.INFO, "Creating friend request...");
                        FriendRequest requestToCreate = (FriendRequest) in.readObject();
                        LOG.log(Level.INFO, "Friend request to create: {0}", requestToCreate);
                        FriendRequest createdRequest = friendRequestCrud.createFriendRequest(requestToCreate);
                        LOG.log(Level.INFO, "Created friend request: {0}", createdRequest);
                        out.writeObject(createdRequest);
                    }
                    case "updateFriendRequest" -> {
                        LOG.log(Level.INFO, "Updating friend request...");
                        FriendRequest requestToUpdate = (FriendRequest) in.readObject();
                        LOG.log(Level.INFO, "Friend request to update: {0}", requestToUpdate);
                        friendRequestCrud.updateFriendRequest(requestToUpdate);
                        LOG.log(Level.INFO, "Updated friend request: {0}", requestToUpdate);
                    }
                    case "deleteFriendRequest" -> {
                        LOG.log(Level.INFO, "Deleting friend request...");
                        FriendRequest requestToDelete = (FriendRequest) in.readObject();
                        LOG.log(Level.INFO, "Friend request to delete: {0}", requestToDelete);
                        friendRequestCrud.deleteFriendRequest(requestToDelete.getId());
                        LOG.log(Level.INFO, "Deleted friend request: {0}", requestToDelete);
                    }
                    case "createWishlist" -> {
                        LOG.log(Level.INFO, "Creating wishlist...");
                        Wishlist wishlistToCreate = (Wishlist) in.readObject();
                        LOG.log(Level.INFO, "Wishlist to create: {0}", wishlistToCreate);
                        Wishlist createdWishlist = wishlistCrud.createWishlist(wishlistToCreate);
                        LOG.log(Level.INFO, "Created wishlist: {0}", createdWishlist);
                        out.writeObject(createdWishlist);
                    }
                    case "getAllWishlists" -> {
                        out.writeObject(wishlistCrud.getAllWishlists());
                    }
                    case "updateWishlist" -> {
                        LOG.log(Level.INFO, "Updating wishlist...");
                        Wishlist wishlistToUpdate = (Wishlist) in.readObject();
                        LOG.log(Level.INFO, "Wishlist to update: {0}", wishlistToUpdate);
                        wishlistCrud.updateWishlist(wishlistToUpdate);
                        LOG.log(Level.INFO, "Updated wishlist: {0}", wishlistToUpdate);
                    }
                    case "deleteWishlist" -> {
                        LOG.log(Level.INFO, "Deleting wishlist...");
                        Wishlist wishlistToDelete = (Wishlist) in.readObject();
                        LOG.log(Level.INFO, "Wishlist to delete: {0}", wishlistToDelete);
                        wishlistCrud.deleteWishlist(wishlistToDelete.getId());
                        LOG.log(Level.INFO, "Deleted wishlist: {0}", wishlistToDelete);
                    }
                    case "getAllItems" -> {
                        out.writeObject(itemCrud.getAllItems());
                    }
                    /*case "createItem" -> {
                    Item itemToCreate = (Item) in.readObject();
                    Item createdItem = itemCrud.createItem(itemToCreate);
                    out.writeObject(createdItem);
                    }*/
                    case "getAllGifts" -> {
                        out.writeObject(giftCrud.getAllGifts());
                    }
                    case "createGift" -> {
                        LOG.log(Level.INFO, "Creating gift...");
                        Gift giftToCreate = (Gift) in.readObject();
                        LOG.log(Level.INFO, "Gift to create: {0}", giftToCreate);
                        Gift createdGift = giftCrud.createGift(giftToCreate);
                        LOG.log(Level.INFO, "Created gift: {0}", createdGift);
                        out.writeObject(createdGift);
                    }
                    case "deleteGift" -> {
                        LOG.log(Level.INFO, "Deleting gift...");
                        Gift giftToDelete = (Gift) in.readObject();
                        LOG.log(Level.INFO, "Gift to delete: {0}", giftToDelete);
                        giftCrud.deleteGift(giftToDelete.getId());
                        LOG.log(Level.INFO, "Deleted gift: {0}", giftToDelete);
                    }
                    case "createContribution" -> {
                        LOG.log(Level.INFO, "Creating contribution...");
                        Contribution contributionToCreate = (Contribution) in.readObject();
                        LOG.log(Level.INFO, "Contribution to create: {0}", contributionToCreate);
                        Contribution createdContribution = contributionCrud.createContribution(contributionToCreate);
                        LOG.log(Level.INFO, "Created contribution: {0}", createdContribution);
                        out.writeObject(createdContribution);
                    }
                    case "getAllContributions" -> {
                        out.writeObject(contributionCrud.getAllContributions());
                    }
                    case "updateContribution" -> {
                        LOG.log(Level.INFO, "Updating contribution...");
                        Contribution contributionToUpdate = (Contribution) in.readObject();
                        LOG.log(Level.INFO, "Contribution to update: {0}", contributionToUpdate);
                        contributionCrud.updateContribution(contributionToUpdate);
                        LOG.log(Level.INFO, "Updated contribution: {0}", contributionToUpdate);
                    }
                    case "deleteContribution" -> {
                        LOG.log(Level.INFO, "Deleting contribution...");
                        Contribution contributionToDelete = (Contribution) in.readObject();
                        LOG.log(Level.INFO, "Contribution to delete: {0}", contributionToDelete);
                        contributionCrud.deleteContribution(contributionToDelete.getId());
                        LOG.log(Level.INFO, "Deleted contribution: {0}", contributionToDelete);
                    }
                    case "disconnect" -> {
                        if (command != null && command.equalsIgnoreCase("disconnect")) {
                            stop();
                        }
                    }
                    default -> {
                        LOG.log(Level.WARNING, "Invalid command: {0}", command);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    void stop() {
        running = false;
        try {
            if (!clientSocket.isClosed()) {
                try (clientSocket) {
                    clientSocket.shutdownInput();
                    clientSocket.shutdownOutput();
                }
            }
            server.removeClient(clientId);
            LOG.log(Level.INFO, "client {0} disconnected", clientId);
        } catch (IOException e) {
            System.err.println("Error stopping client handler: " + e.getMessage());
        }
    }

}
