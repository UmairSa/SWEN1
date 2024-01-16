package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Battle;
import at.technikum.apps.task.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BattleRepository {
    private final Database database = new Database();

    public Battle save(Battle battle) {
        String SAVE_BATTLE_SQL = "INSERT INTO battle (player1id, player2id, battlelog, battleoutcome) VALUES (?, ?, ?, ?) RETURNING battleid";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_BATTLE_SQL)) {
            pstmt.setInt(1, battle.getPlayer1Id());
            pstmt.setInt(2, battle.getPlayer2Id());
            pstmt.setString(3, battle.getBattleLog());
            pstmt.setString(4, battle.getBattleOutcome());

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                battle.setBattleId(rs.getInt("battleid"));
            }
            return battle;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Battle> findById(int battleId) {
        String sql = "SELECT * FROM battle WHERE battleid = ?";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, battleId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Battle battle = new Battle();
                battle.setBattleId(rs.getInt("battleid"));
                battle.setPlayer1Id(rs.getInt("player1id"));
                battle.setPlayer2Id(rs.getInt("player2id"));
                battle.setBattleLog(rs.getString("battlelog"));
                battle.setBattleOutcome(rs.getString("battleoutcome"));
                battle.setCreatedAt(rs.getTimestamp("createdat"));
                return Optional.of(battle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }







}
