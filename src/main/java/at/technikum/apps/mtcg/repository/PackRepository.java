package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Pack;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PackRepository {
    private final Database database = new Database();
    private static final Logger logger = Logger.getLogger(PackRepository.class.getName());

    public Pack save(Pack pack) {
        String SAVE_PACKAGE_SQL = "INSERT INTO packages (packageid, price) VALUES (?, 5)";

        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_PACKAGE_SQL)) {
            pstmt.setObject(1, pack.getPackId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating package failed, no rows affected.");
            }
            return pack;
        } catch (SQLException e) {
            logger.severe("Error creating package: " + e.getMessage());
            return null;
        }
    }

}