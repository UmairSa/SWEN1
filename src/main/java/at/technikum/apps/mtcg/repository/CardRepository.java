package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.task.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

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

    public List<Card> findByPackId(int packId) {
        List<Card> cards = new ArrayList<>();
        String QUERY = "SELECT * FROM cards WHERE packageid = ?";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(QUERY)) {
            pstmt.setInt(1, packId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card();
                    // Populate the card object from the result set
                    card.setCardId(UUID.fromString(rs.getString("cardid")));
                    card.setName(rs.getString("name"));
                    card.setDamage(rs.getDouble("damage"));
                    card.setElementType(rs.getString("elementtype"));
                    card.setCardType(rs.getString("cardtype"));
                    card.setOwnerId(rs.getObject("ownerid") != null ? rs.getInt("ownerid") : null);
                    card.setInDeck(rs.getBoolean("indeck"));
                    card.setPackId(rs.getInt("packageid"));

                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding cards by package ID: " + e.getMessage());
        }
        return cards;
    }


}
