package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Trade;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TradeRepository {
    private final Database database = new Database();
    public Trade save(Trade trade) {
        String SAVE_TRADE_SQL = "INSERT INTO trade (tradeid, cardtotradeid, cardtype, minimumdamage, user1id) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_TRADE_SQL)) {
            pstmt.setObject(1, trade.getTradeId());
            pstmt.setObject(2, trade.getCardToTradeId());
            pstmt.setString(3, trade.getCardType());
            pstmt.setDouble(4, trade.getMinimumDamage());
            pstmt.setInt(5, trade.getUser1Id());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating trade failed, no rows affected.");
            }
            return trade;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Trade> findAllTrades() {
        List<Trade> trades = new ArrayList<>();
        String FIND_ALL_TRADES_SQL = "SELECT * FROM trade";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_ALL_TRADES_SQL)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    trades.add(createTradeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trades;
    }
    public void deleteById(UUID tradeId) {
        String DELETE_TRADE_SQL = "DELETE FROM trade WHERE tradeid = ?";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(DELETE_TRADE_SQL)) {
            pstmt.setObject(1, tradeId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting trade failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private Trade createTradeFromResultSet(ResultSet rs) throws SQLException {
        Trade trade = new Trade();
        trade.setTradeId(UUID.fromString(rs.getString("tradeid")));
        trade.setCardToTradeId(UUID.fromString(rs.getString("cardtotradeid")));
        trade.setCardType(rs.getString("cardtype"));
        trade.setMinimumDamage(rs.getDouble("minimumdamage"));
        trade.setUser1Id(rs.getInt("user1id"));
        trade.setUser2Id(rs.getObject("user2id") != null ? rs.getInt("user2id") : null);
        // You'll need to handle the possibility of user2id being null if the trade hasn't been accepted yet.
        return trade;
    }
    public Optional<Trade> findById(UUID tradeId) {
        String FIND_TRADE_SQL = "SELECT * FROM trade WHERE tradeid = ?";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_TRADE_SQL)) {
            pstmt.setObject(1, tradeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createTradeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
