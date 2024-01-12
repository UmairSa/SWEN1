package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Pack;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PackRepository {
    private final Database database = new Database();
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
            e.printStackTrace();
        }
        return null;
    }
}
