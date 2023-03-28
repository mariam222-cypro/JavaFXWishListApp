package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.FriendRequest;
import mariam.wishlist.server.database.DatabaseHandler;

public class FriendRequestCrud {

    private final DatabaseHandler handler;
    private final FriendCrud friendCrud;

    public FriendRequestCrud(DatabaseHandler handler) {
        this.handler = handler;

        friendCrud = new FriendCrud(handler);
    }

    public List<FriendRequest> getAllFriendRequests() throws SQLException {
        String query = "SELECT * FROM FriendRequest";

        try (ResultSet rs = handler.executeQuery(query)) {
            List<FriendRequest> friendRequests = new ArrayList<>();
            while (rs.next()) {
                FriendRequest friendRequest = new FriendRequest(
                        rs.getInt("request_id"),
                        friendCrud.getFriendById(rs.getInt("sender_id")),
                        friendCrud.getFriendById(rs.getInt("receiver_id")),
                        FriendRequest.Status.from(rs.getString("status"))
                );
                friendRequests.add(friendRequest);
            }
            return friendRequests;
        }
    }

    public FriendRequest getFriendRequestById(int id) throws SQLException {
        String query = "SELECT * FROM FriendRequest WHERE request_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new FriendRequest(
                            rs.getInt("request_id"),
                            friendCrud.getFriendById(rs.getInt("sender_id")),
                            friendCrud.getFriendById(rs.getInt("receiver_id")),
                            FriendRequest.Status.from(rs.getString("status"))
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public FriendRequest createFriendRequest(FriendRequest friendRequest) throws SQLException {
        String query = "INSERT INTO FriendRequest (sender_id, receiver_id, status) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setInt(1, friendRequest.getSender().getId());
            stmt.setInt(2, friendRequest.getReceiver().getId());
            stmt.setString(3, friendRequest.getStatus().name());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new FriendRequest(rs.getInt(1), friendRequest.getSender(), friendRequest.getReceiver(), friendRequest.getStatus());
                } else {
                    return null;
                }
            }
        }
    }

    public void updateFriendRequest(FriendRequest friendRequest) throws SQLException {
        String query = "UPDATE FriendRequest SET sender_id = ?, receiver_id = ?, status = ? WHERE request_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, friendRequest.getSender().getId());
            stmt.setInt(2, friendRequest.getReceiver().getId());
            stmt.setString(3, friendRequest.getStatus().name());
            stmt.setInt(4, friendRequest.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteFriendRequest(int id) throws SQLException {
        String query = "DELETE FROM FriendRequest WHERE request_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        handler.disconnect();
    }

}
