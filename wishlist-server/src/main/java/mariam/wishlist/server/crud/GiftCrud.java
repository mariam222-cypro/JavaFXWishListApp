package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.Gift;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.server.database.DatabaseHandler;

public class GiftCrud {

    private final DatabaseHandler handler;
    private final ItemCrud itemCrud;
    private final WishlistCrud wishlistCrud;

    public GiftCrud(DatabaseHandler handler) {
        this.handler = handler;

        itemCrud = new ItemCrud(handler);
        wishlistCrud = new WishlistCrud(handler);
    }

    public Gift createGift(Gift gift) throws SQLException {
        String query = "INSERT INTO Gift (item_id, wishlist_id) VALUES (?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setInt(1, gift.getItem().getId());
            stmt.setInt(2, gift.getWishlist().getId());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Gift(rs.getInt(1), gift.getItem(), gift.getWishlist());
                } else {
                    return null;
                }
            }
        }
    }

    public List<Gift> getAllGifts() throws SQLException {
        String query = "SELECT * FROM Gift";
        List<Gift> gifts = new ArrayList<>();
        
         try (ResultSet rs = handler.executeQuery(query)) {
             while (rs.next()) {                 
                 gifts.add(getGiftById(rs.getInt("gift_id")));
             }
         }
         
         return gifts;
    }

    public Gift getGiftById(int id) throws SQLException {
        String query = "SELECT * FROM Gift WHERE gift_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Gift(
                            rs.getInt("gift_id"),
                            itemCrud.getItemById(rs.getInt("item_id")),
                            wishlistCrud.getWishlistById(rs.getInt("wishlist_id"))
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public void updateGift(Gift gift) throws SQLException {
        String query = "UPDATE Gift SET item_id = ?, wishlist_id = ? WHERE gift_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, gift.getItem().getId());
            stmt.setInt(2, gift.getWishlist().getId());
            stmt.setInt(3, gift.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteGift(int id) throws SQLException {
        String query = "DELETE FROM Gift WHERE gift_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        handler.disconnect();
    }

}
