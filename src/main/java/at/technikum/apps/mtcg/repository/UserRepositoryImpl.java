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

public class UserRepositoryImpl implements UserRepo {

    private final String FIND_ALL_USERS_SQL = "SELECT * FROM users";
    private final String SAVE_USERS_SQL = "INSERT INTO users(username, password, coins, elo) VALUES(?, ?, ?, ?)";
    private final Database database = new Database();

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(FIND_ALL_USERS_SQL);
                ResultSet rs = pstmt.executeQuery()
        ) {
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
    public User save(User user) {
        try (
                Connection con = database.getConnection();
                PreparedStatement pstmt = con.prepareStatement(SAVE_USERS_SQL);
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getCoins());
            pstmt.setInt(4, user.getElo());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Erstellen fehltgeschlagen, keine Benutzer betroffen");
            }
            return user;
        } catch (SQLException e) {
            //Fehlerbehandlung
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<User> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public User update(User entity) {
        return null;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }
}