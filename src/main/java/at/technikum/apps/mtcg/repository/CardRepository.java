package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.task.data.Database;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class CardRepository {
    private final Database database = new Database();
    private static final Logger logger = Logger.getLogger(CardRepository.class.getName());

    public Card save(Card card) {
        String SAVE_CARD_SQL = "INSERT INTO cards (cardid, name, damage, elementtype, cardtype, ownerid, indeck, packageid) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_CARD_SQL)) {
            pstmt.setObject(1, card.getCardId());
            pstmt.setString(2, card.getName());
            pstmt.setDouble(3, card.getDamage());
            pstmt.setString(4, card.getElementType());
            pstmt.setString(5, card.getCardType());
            pstmt.setObject(6, card.getOwnerId());
            pstmt.setBoolean(7, card.isInDeck());
            pstmt.setInt(8, card.getPackId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating card failed, no rows affected.");
            }
            return card;
        } catch (SQLException e) {
            logger.severe("Error creating card: " + e.getMessage());
        }
        return null;
    }

    public void updateCardOwner(UUID cardId, Integer ownerId) {
        String UPDATE_OWNER_SQL = "UPDATE cards SET ownerid = ? WHERE cardid = ?";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UPDATE_OWNER_SQL)) {
            pstmt.setObject(1, ownerId);
            pstmt.setObject(2, cardId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                logger.warning("Updating card owner failed, no rows affected.");
            }
        } catch (SQLException e) {
            logger.severe("Error updating card owner: " + e.getMessage());
        }
    }
    private Card createCardFromResultSet(ResultSet rs) throws SQLException {
        Card card = new Card();
        card.setCardId(UUID.fromString(rs.getString("cardid")));
        card.setName(rs.getString("name"));
        card.setDamage(rs.getDouble("damage"));
        card.setElementType(rs.getString("elementtype"));
        card.setCardType(rs.getString("cardtype"));
        card.setInDeck(rs.getBoolean("indeck"));
        card.setOwnerId(rs.getObject("ownerid") != null ? rs.getInt("ownerid") : null);
        card.setPackId(rs.getInt("packageid"));
        return card;
    }
    private List<Card> findCards(String query, int id) {
        List<Card> cards = new ArrayList<>();
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cards.add(createCardFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding cards: " + e.getMessage());
        }
        return cards;
    }
    public boolean isCardInDeck(UUID cardId) {
        String query = "SELECT COUNT(*) FROM cards WHERE cardid = ? AND indeck = TRUE";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setObject(1, cardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count is greater than 0, card is in a deck
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking if card is in deck: " + e.getMessage());
        }
        return false;
    }

    public Optional<Card> findById(UUID cardId) {
        String FIND_BY_ID_SQL = "SELECT * FROM cards WHERE cardid = ?";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(FIND_BY_ID_SQL)) {
            pstmt.setObject(1, cardId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createCardFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding card by ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<Card> findByPackId(int packId) {
        return findCards("SELECT * FROM cards WHERE packageid = ?", packId);
    }

    public List<Card> findByOwnerId(int ownerId) {
        return findCards("SELECT * FROM cards WHERE ownerid = ?", ownerId);
    }

    public List<Card> findDeckByUserId(int userId) {
        return findCards("SELECT * FROM cards WHERE ownerid = ? AND indeck = TRUE", userId);
    }
    public void configureDeck(int userId, List<UUID> cardIds) {
        String UPDATE_DECK = "UPDATE cards SET indeck = CASE WHEN cardid = ANY (?) THEN TRUE ELSE FALSE END WHERE ownerid = ?";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(UPDATE_DECK)) {
            Array cardIdArray = con.createArrayOf("UUID", cardIds.toArray());
            pstmt.setArray(1, cardIdArray);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error configuring deck for user ID " + userId + ": " + e.getMessage());
        }
    }
}
