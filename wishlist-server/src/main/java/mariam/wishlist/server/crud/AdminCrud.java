package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.Admin;
import mariam.wishlist.server.database.DatabaseHandler;

public class AdminCrud {

    private final DatabaseHandler handler;

    public AdminCrud(DatabaseHandler handler) {
        this.handler = handler;
    }

    public List<Admin> getAllAdmins() throws SQLException {
        String query = "SELECT * FROM Admin";

        try (ResultSet rs = handler.executeQuery(query)) {
            List<Admin> admins = new ArrayList<>();
            while (rs.next()) {
                Admin user = new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                admins.add(user);
            }
            return admins;
        }
    }

    public Admin getAdminById(int id) throws SQLException {
        String query = "SELECT * FROM Admin WHERE admin_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("admin_id"),
                            rs.getString("username"),
                            rs.getString("password")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public Admin createAdmin(Admin admin) throws SQLException {
        String query = "INSERT INTO Admin (username, password) VALUES (?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Admin(rs.getInt(1), admin.getUsername(), admin.getPassword());
                } else {
                    return null;
                }
            }
        }
    }

    public void updateAdmin(Admin admin) throws SQLException {
        String query = "UPDATE Admin SET username = ?, password = ? WHERE admin_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setInt(3, admin.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteAdmin(int id) throws SQLException {
        String query = "DELETE FROM Admin WHERE admin_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    void close() throws SQLException {
        handler.disconnect();
    }
}
