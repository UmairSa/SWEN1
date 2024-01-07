package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;
import at.technikum.apps.task.data.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final Database database = new Database();

    //private static final String FIND_BY_ID_SQL = "SELECT * FROM users WHERE id = ?";


    @Override
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
            //Fehlerbehandlung
            //e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findAll() {

        List<User> users = new ArrayList<>();

        String FIND_ALL_USERS_SQL = "SELECT * FROM users";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_ALL_USERS_SQL); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("UserID"));
                user.setUsername(rs.getString("Username"));
                user.setPassword(rs.getString("Password")); // #Hash?
                user.setCoins(rs.getInt("Coins"));
                user.setElo(rs.getInt("ELO"));
                users.add(user);
            }
        } catch (SQLException e) {
            // Exception
        }

        return users;
    }

    @Override
    public Optional<User> findById(Integer userid) {

        String FIND_BY_ID_SQL = "SELECT * FROM users WHERE UserID = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_BY_ID_SQL)) {

            pstmt.setInt(1, userid);

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
            //Fehlerbehandlung
            //e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public User update(User user) {
        String UPDATE_USERS_SQL = "UPDATE users SET Username = ?, Password = ?, Coins = ?, ELO = ? WHERE UserID = ?";

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
            //Fehlerbehandlung
            //e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteById(Integer userid) {
        String DELETE_USER_SQL = "DELETE FROM users WHERE UserID = ?";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(DELETE_USER_SQL)) {
            pstmt.setInt(1, userid);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Löschen fehlgeschlagen, kein Benutzer betroffen.");
            }
        } catch (SQLException e) {
            //Fehlerbehandlung
            //e.printStackTrace();
        }

    }

    @Override
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
            //Fehlerbehandlung
            //e.printStackTrace();
        }
        return Optional.empty();
    }
}