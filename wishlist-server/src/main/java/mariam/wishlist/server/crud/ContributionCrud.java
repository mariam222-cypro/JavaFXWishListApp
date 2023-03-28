package mariam.wishlist.server.crud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import mariam.wishlist.core.model.Contribution;
import mariam.wishlist.server.database.DatabaseHandler;

public class ContributionCrud {

    private final DatabaseHandler handler;
    private final GiftCrud giftCrud;
    private final FriendCrud friendCrud;

    public ContributionCrud(DatabaseHandler handler) {
        this.handler = handler;

        giftCrud = new GiftCrud(handler);
        friendCrud = new FriendCrud(handler);
    }

    public Contribution createContribution(Contribution contribution) throws SQLException {
        String query = "INSERT INTO Contribution (gift_id, contibutor_id, amount) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = handler.getPreparedStatementReturningGeneratedKeys(query);) {
            stmt.setInt(1, contribution.getGift().getId());
            stmt.setInt(2, contribution.getContributor().getId());
            stmt.setBigDecimal(3, contribution.getAmount());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Contribution(rs.getInt(1), contribution.getGift(), contribution.getContributor(), contribution.getAmount());
                } else {
                    return null;
                }
            }
        }
    }

    public List<Contribution> getAllContributions() throws SQLException {
        String query = "SELECT * FROM Contribution";

        try (ResultSet rs = handler.executeQuery(query)) {
            List<Contribution> contributions = new ArrayList<>();
            while (rs.next()) {
                contributions.add(getContributionById(rs.getInt("contribution_id")));
            }
            return contributions;
        }
    }

    public Contribution getContributionById(int id) throws SQLException {
        String query = "SELECT * FROM Contribution WHERE contribution_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contribution(
                            rs.getInt("contribution_id"),
                            giftCrud.getGiftById(rs.getInt("gift_id")),
                            friendCrud.getFriendById(rs.getInt("contibutor_id")),
                            rs.getBigDecimal("amount")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public void updateContribution(Contribution contribution) throws SQLException {
        String query = "UPDATE Contribution SET gift_id = ?, contibutor_id = ?, amount = ? WHERE contribution_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, contribution.getGift().getId());
            stmt.setInt(2, contribution.getContributor().getId());
            stmt.setBigDecimal(3, contribution.getAmount());
            stmt.setInt(4, contribution.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteContribution(int id) throws SQLException {
        String query = "DELETE FROM Contribution WHERE contribution_id = ?";

        try (PreparedStatement stmt = handler.getPreparedStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void close() throws SQLException {
        handler.disconnect();
    }

}
