package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.Item;
import mariam.wishlist.server.database.DatabaseHandler;

public class ItemCrud {

    private final DatabaseHandler handler;

    public ItemCrud(DatabaseHandler handler) {
        this.handler = handler;
    }

    public Item createItem(Item item) throws SQLException {
        String query = "INSERT INTO Item (item_name, item_description, cost) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getCost());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Item(rs.getInt(1), item.getName(), item.getDescription(), item.getCost());
                } else {
                    return null;
                }
            }
        }
    }

    public List<Item> getAllItems() throws SQLException {
        String query = "SELECT * FROM Item";

        try (ResultSet rs = handler.executeQuery(query)) {
            List<Item> items = new ArrayList<>();
            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getString("item_description"),
                        rs.getBigDecimal("cost")
                );
                items.add(item);
            }
            return items;
        }
    }

    public Item getItemById(int id) throws SQLException {
        String query = "SELECT * FROM Item WHERE item_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                            rs.getInt("item_id"),
                            rs.getString("item_name"),
                            rs.getString("item_description"),
                            rs.getBigDecimal("cost")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public void updateItem(Item item) throws SQLException {
        String query = "UPDATE Item SET item_name = ?, item_description = ?, cost = ? WHERE item_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getCost());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteItem(int id) throws SQLException {
        String query = "DELETE FROM Item WHERE item_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    void close() throws SQLException {
        handler.disconnect();
    }
}
