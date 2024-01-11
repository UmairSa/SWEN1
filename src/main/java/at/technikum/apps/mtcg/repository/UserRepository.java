package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.task.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {
    private final Database database = new Database();

    public User save(User user) {
        String SAVE_USERS_SQL = "INSERT INTO users(username, password, coins, elo) VALUES(?, ?, ?, ?)";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_USERS_SQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getCoins());
            pstmt.setInt(4, user.getElo());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Erstellen fehlgeschlagen, keine Benutzer betroffen");
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Optional<User> findByUsername(String username) {

        String FIND_BY_USERNAME_SQL = "SELECT * FROM users WHERE username = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_BY_USERNAME_SQL)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setCoins(rs.getInt("Coins"));
                    user.setElo(rs.getInt("ELO"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public User update(User user) {
        String UPDATE_USERS_SQL = "UPDATE users SET username = ?, password = ?, coins = ?, elo = ? WHERE userid = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(UPDATE_USERS_SQL)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getCoins());
            pstmt.setInt(4, user.getElo());
            pstmt.setInt(5, user.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Aktualisierung fehlgeschlagen, kein User betroffen.");
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void deleteByUsername(String userName) {
        String DELETE_USER_SQL = "DELETE FROM users WHERE username = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(DELETE_USER_SQL)) {
            pstmt.setString(1, userName);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Löschen fehlgeschlagen, kein Benutzer betroffen.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}