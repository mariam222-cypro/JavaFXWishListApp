package mariam.wishlist.client.service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.core.model.Wishlist;

public class CrudService {

    private static final Logger LOG = Logger.getLogger(CrudService.class.getName());

    public List<Friend> getAllFriends() {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("getAllFriends");
            List<Friend> friends = (List<Friend>) in.readObject();

            System.out.println("All friends: " + friends);

            out.writeObject("disconnect");

            return friends;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    public Friend createFriend(Friend friend) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Creating friend...");

            out.writeObject("createFriend");
            out.writeObject(friend);

            Friend createdFriend = (Friend) in.readObject();

            LOG.log(Level.INFO, "Created friend: {0}", createdFriend);

            out.writeObject("disconnect");

            return createdFriend;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return null;
    }

    public void updateFriend(Friend friend) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Updating friend: {0}...", friend);

            out.writeObject("updateFriend");
            out.writeObject(friend);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public void deleteFriend(Friend friend) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Deleting friend...");

            out.writeObject("deleteFriend");
            out.writeObject(friend);

            LOG.log(Level.INFO, "Deleted friend: {0}", friend);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public List<FriendRequest> getAllFriendRequests() {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("getAllFriendRequests");
            List<FriendRequest> friendRequests = (List<FriendRequest>) in.readObject();

            out.writeObject("disconnect");

            return friendRequests;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    public FriendRequest createFriendRequest(FriendRequest friendRequest) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Creating friend request...");

            out.writeObject("createFriendRequest");
            out.writeObject(friendRequest);

            FriendRequest createdFriendRequest = (FriendRequest) in.readObject();

            LOG.log(Level.INFO, "Created friend request: {0}", createdFriendRequest);

            out.writeObject("disconnect");

            return createdFriendRequest;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return null;
    }

    public void updateFriendRequest(FriendRequest request) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Updating friend request: {0}...", request);

            out.writeObject("updateFriendRequest");
            out.writeObject(request);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public void deleteFriendRequest(FriendRequest friendRequest) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Deleting friend request...");

            out.writeObject("deleteFriendRequest");
            out.writeObject(friendRequest);

            LOG.log(Level.INFO, "Deleted friend request: {0}", friendRequest);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public Wishlist createWishlist(Wishlist wishlist) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Creating wishlist...");

            out.writeObject("createWishlist");
            out.writeObject(wishlist);

            Wishlist createdWishlist = (Wishlist) in.readObject();

            LOG.log(Level.INFO, "Created wishlist: {0}", createdWishlist);

            out.writeObject("disconnect");

            return createdWishlist;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return null;
    }

    public List<Wishlist> getAllWishlists() {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("getAllWishlists");
            List<Wishlist> wishlists = (List<Wishlist>) in.readObject();

            out.writeObject("disconnect");

            return wishlists;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    public void updateWishlist(Wishlist wishlist) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Updating wishlist: {0}...", wishlist);

            out.writeObject("updateWishlist");
            out.writeObject(wishlist);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public void deleteWishlist(Wishlist wishlist) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Deleting wishlist...");

            out.writeObject("deleteWishlist");
            out.writeObject(wishlist);

            LOG.log(Level.INFO, "Deleted wishlist: {0}", wishlist);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public List<Item> getAllItems() {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("getAllItems");
            List<Item> items = (List<Item>) in.readObject();

            out.writeObject("disconnect");

            return items;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    public List<Gift> getAllGifts() {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("getAllGifts");
            List<Gift> gifts = (List<Gift>) in.readObject();

            out.writeObject("disconnect");

            return gifts;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    public Gift createGift(Gift gift) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Creating gift...");

            out.writeObject("createGift");
            out.writeObject(gift);

            Gift createdGift = (Gift) in.readObject();

            LOG.log(Level.INFO, "Created gift: {0}", createdGift);

            out.writeObject("disconnect");

            return createdGift;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return null;
    }

    public void deleteGift(Gift gift) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Deleting gift...");

            out.writeObject("deleteGift");
            out.writeObject(gift);

            LOG.log(Level.INFO, "Deleted gift: {0}", gift);

            out.writeObject("disconnect");
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        }
    }

    public List<Contribution> getAllContributions() {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("getAllContributions");
            List<Contribution> contributions = (List<Contribution>) in.readObject();

            out.writeObject("disconnect");

            return contributions;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return Collections.emptyList();
    }

    public Contribution createContribution(Contribution contribution) {
        try (Socket socket = new Socket("localhost", 5000); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            LOG.log(Level.INFO, "Creating contribution...");

            out.writeObject("createContribution");
            out.writeObject(contribution);

            Contribution createdContribution = (Contribution) in.readObject();

            LOG.log(Level.INFO, "Created contribution: {0}", createdContribution);

            out.writeObject("disconnect");

            return createdContribution;
        } catch (UnknownHostException ex) {
            LOG.log(Level.SEVERE, "Host not found: {0}", ex.getMessage());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error communicating with server: {0}", ex.getMessage());
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error reading object from server: {0}", ex.getMessage());
        }

        return null;
    }

}
