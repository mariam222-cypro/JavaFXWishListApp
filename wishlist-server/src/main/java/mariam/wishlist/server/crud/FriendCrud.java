package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.Friend;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.server.database.DatabaseHandler;

public class FriendCrud {

    private final DatabaseHandler handler;

    public FriendCrud(DatabaseHandler databaseHandler) {
        this.handler = databaseHandler;
    }

    public List<Friend> getAllFriends() throws SQLException {
        String query = "SELECT * FROM Friend";

        try (ResultSet rs = handler.executeQuery(query)) {
            List<Friend> friends = new ArrayList<>();
            while (rs.next()) {
                Friend friend = new Friend(
                        rs.getInt("friend_id"),
                        rs.getString("name"),
                        rs.getString("password")
                );
                friends.add(friend);
            }
            return friends;
        }
    }

    public Friend getFriendById(int id) throws SQLException {
        String query = "SELECT * FROM Friend WHERE friend_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Friend(
                            rs.getInt("friend_id"),
                            rs.getString("name"),
                            rs.getString("password")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public Friend createFriend(Friend friend) throws SQLException {
        String query = "INSERT INTO Friend (name, password) VALUES (?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setString(1, friend.getName());
            stmt.setString(2, friend.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Friend(rs.getInt(1), friend.getName(), friend.getPassword());
                } else {
                    return null;
                }
            }
        }
    }

    public void updateFriend(Friend friend) throws SQLException {
        String query = "UPDATE Friend SET name = ?, password = ? WHERE friend_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setString(1, friend.getName());
            stmt.setString(2, friend.getPassword());
            stmt.setInt(3, friend.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteFriend(int id) throws SQLException {
        /* FriendRequestCrud friendRequestCrud = new FriendRequestCrud(handler);
        List<FriendRequest> requests = friendRequestCrud.getAllFriendRequests()
        .stream()
        .filter(req -> req.getSender().getId() == id || req.getReceiver().getId() == id)
        .toList();
        
        for (FriendRequest request : requests) {
        friendRequestCrud.deleteFriendRequest(request.getId());
        }
        
        WishlistCrud wishlistCrud = new WishlistCrud(handler);
        List<Wishlist> wishlists = wishlistCrud.getAllWishlists()
        .stream()
        .filter(w -> w.getOwner().getId() == id)
        .toList();
        
        for (Wishlist wishlist : wishlists) {
        wishlistCrud.deleteWishlist(wishlist.getId());
        }*/

        String query = "DELETE FROM Friend WHERE friend_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        handler.disconnect();
    }

}
