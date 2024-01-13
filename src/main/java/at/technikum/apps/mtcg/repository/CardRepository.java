package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.task.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

public class CardRepository {
    private final Database database = new Database();
    private static final Logger logger = Logger.getLogger(CardRepository.class.getName());

    public Card save(Card card) {
        String SAVE_CARD_SQL = "INSERT INTO cards (cardid, name, damage, elementtype, cardtype, ownerid) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_CARD_SQL)) {
            pstmt.setObject(1, card.getCardId());
            pstmt.setString(2, card.getName());
            pstmt.setDouble(3, card.getDamage());
            pstmt.setString(4, card.getElementType());
            pstmt.setString(5, card.getCardType());
            pstmt.setObject(6, card.getOwnerId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating card failed, no rows affected.");
            }
            return card;
        } catch (SQLException e) {
            logger.severe("Error ccreating card: " + e.getMessage());
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

}
