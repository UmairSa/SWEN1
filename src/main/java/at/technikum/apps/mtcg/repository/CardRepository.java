package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Card;
import at.technikum.apps.task.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class CardRepository {
    private final Database database = new Database();

    public Card save(Card card) {
        String SAVE_CARD_SQL = "INSERT INTO cards (cardid, name, damage, elementtype, cardtype, ownerid) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_CARD_SQL)) {
            pstmt.setObject(1, card.getCardId());
            pstmt.setString(2, card.getName());
            pstmt.setDouble(3, card.getDamage());
            pstmt.setString(4, card.getElementType());
            pstmt.setString(5, card.getCardType());
            pstmt.setObject(6, null);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating card failed, no rows affected.");
            }
            return card;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
