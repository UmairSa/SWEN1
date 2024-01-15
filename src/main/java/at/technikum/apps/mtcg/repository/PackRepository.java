package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.data.Database;
import at.technikum.apps.mtcg.entity.Pack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PackRepository {
    private final Database database = new Database();
    private static final Logger logger = Logger.getLogger(PackRepository.class.getName());

    public Pack save(Pack pack) {
        String SAVE_PACKAGE_SQL = "INSERT INTO packages (packageid, price) VALUES (?, 5)";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(SAVE_PACKAGE_SQL)) {
            pstmt.setInt(1, pack.getPackId());
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
    public Pack findById(int packageId) {
        String FIND_PACKAGE_SQL = "SELECT * FROM packages WHERE packageid = ?";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(FIND_PACKAGE_SQL)) {
            pstmt.setInt(1, packageId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Pack pack = new Pack();
                    pack.setPackId(rs.getInt("packageid"));
                    pack.setPrice(rs.getInt("price"));
                    return pack;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error finding package: " + e.getMessage());
        }
        return null;
    }
    public boolean isPackageAcquired(int packageId) {
        String CHECK_PACKAGE_SQL = "SELECT count(*) FROM cards WHERE packageid = ? AND ownerid IS NOT NULL";
        try (Connection con = database.getConnection(); PreparedStatement pstmt = con.prepareStatement(CHECK_PACKAGE_SQL)) {
            pstmt.setInt(1, packageId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.severe("Error checking package acquisition status: " + e.getMessage());
        }
        return false;
    }
}