package server.dao;

import java.sql.*;
import server.DBConnection;

public class UserDAO {

    public int getOrCreateUser(String username) {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps =
                conn.prepareStatement(
                    "SELECT id FROM users WHERE username = ?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }

            ps = conn.prepareStatement(
                "INSERT INTO users(username) VALUES (?)",
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
