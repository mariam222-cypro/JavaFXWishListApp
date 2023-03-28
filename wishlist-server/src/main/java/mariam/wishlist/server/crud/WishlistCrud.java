package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Wishlist;
import mariam.wishlist.server.database.DatabaseHandler;

public class WishlistCrud {

    private final DatabaseHandler handler;
    private final FriendCrud friendCrud;

    public WishlistCrud(DatabaseHandler handler) {
        this.handler = handler;

        friendCrud = new FriendCrud(handler);
    }

    public Wishlist createWishlist(Wishlist wishList) throws SQLException {
        String query = "INSERT INTO Wishlist (friend_id, wishList_name, wishList_description) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setInt(1, wishList.getOwner().getId());
            stmt.setString(2, wishList.getName());
            stmt.setString(3, wishList.getDescription());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Wishlist(rs.getInt(1), wishList.getOwner(), wishList.getName(), wishList.getDescription());
                } else {
                    return null;
                }
            }
        }
    }

    public Wishlist getWishlistById(int id) throws SQLException {
        String query = "SELECT * FROM Wishlist WHERE wishList_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Wishlist(
                            rs.getInt("wishList_id"),
                            friendCrud.getFriendById(rs.getInt("friend_id")),
                            rs.getString("wishList_name"),
                            rs.getString("wishList_description")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public List<Wishlist> getAllWishlists() throws SQLException {
        String query = "SELECT * FROM Wishlist";

        try (ResultSet rs = handler.executeQuery(query)) {
            List<Wishlist> wishLists = new ArrayList<>();
            while (rs.next()) {
                Wishlist wishList = new Wishlist(
                        rs.getInt("wishList_id"),
                        friendCrud.getFriendById(rs.getInt("friend_id")),
                        rs.getString("wishList_name"),
                        rs.getString("wishList_description")
                );
                wishLists.add(wishList);
            }
            return wishLists;
        }
    }

    public void updateWishlist(Wishlist wishList) throws SQLException {
        String query = "UPDATE Wishlist SET friend_id = ?, wishList_name = ?, wishList_description = ? WHERE wishList_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, wishList.getOwner().getId());
            stmt.setString(2, wishList.getName());
            stmt.setString(3, wishList.getDescription());
            stmt.setInt(4, wishList.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteWishlist(int id) throws SQLException {
        /*GiftCrud giftCrud = new GiftCrud(handler);
        
        for (Gift gift : giftCrud.getAllGifts()) {
        giftCrud.deleteGift(gift.getId());
        }*/
        String query = "DELETE FROM Wishlist WHERE wishList_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        handler.disconnect();
    }

}
