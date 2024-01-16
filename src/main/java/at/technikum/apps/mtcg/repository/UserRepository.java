package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.Scoreboard;
import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.mtcg.entity.UserStats;
import at.technikum.apps.task.data.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
                    user.setName(rs.getString("Name"));
                    user.setBio(rs.getString("Bio"));
                    user.setImage(rs.getString("Image"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    public Optional<User> findById(int id) {
        String FIND_BY_ID_SQL = "SELECT * FROM users WHERE userid = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_BY_ID_SQL)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("UserID"));
                    user.setUsername(rs.getString("Username"));
                    user.setPassword(rs.getString("Password"));
                    user.setCoins(rs.getInt("Coins"));
                    user.setElo(rs.getInt("ELO"));
                    user.setName(rs.getString("Name"));
                    user.setBio(rs.getString("Bio"));
                    user.setImage(rs.getString("Image"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public User update(User user) {
        String UPDATE_USERS_SQL = "UPDATE users SET name = ?, bio = ?, image = ?, coins = ?, wins = ?, losses = ?, elo = ?  WHERE username = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(UPDATE_USERS_SQL)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getBio());
            pstmt.setString(3, user.getImage());
            pstmt.setInt(4, user.getCoins());
            pstmt.setInt(5, user.getWins());
            pstmt.setInt(6, user.getLosses());
            pstmt.setInt(7, user.getElo());
            pstmt.setString(8, user.getUsername());

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
    //---------------------------------------------------------- User Management ---------------------------------------------------------
    public UserStats getUserStats(int userId) {
        String query = "SELECT wins, losses, elo FROM users WHERE userid = ?";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int wins = rs.getInt("wins");
                    int losses = rs.getInt("losses");
                    int elo = rs.getInt("elo");
                    int gamesPlayed = wins + losses;
                    double winPercentage = gamesPlayed > 0 ? (double) wins / gamesPlayed * 100 : 0;

                    return new UserStats(wins, losses, gamesPlayed, winPercentage, elo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //---------------------------------------------------------- User Stats ---------------------------------------------------------
    public List<Scoreboard> getScoreboard() {
        List<Scoreboard> scoreboard = new ArrayList<>();
        String query = "SELECT username, elo FROM users ORDER BY elo DESC";
        try (Connection con = database.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    int elo = rs.getInt("elo");
                    scoreboard.add(new Scoreboard(username, elo));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scoreboard;
    }
}