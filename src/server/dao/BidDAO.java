package server.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import common.Bid;
import server.DBConnection;
import common.WinnerInfo;

public class BidDAO {

    public void saveBid(int productId, int userId, double amount) {
        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO bids(product_id, user_id, amount) VALUES (?,?,?)");

            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setDouble(3, amount);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // HISTORIAL DE PUJAS (RMI)
    public List<Bid> getBidsByProduct(int productId) {

        List<Bid> bids = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM bids WHERE product_id=? ORDER BY id DESC");

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bids.add(new Bid(
                    String.valueOf(rs.getInt("user_id")),
                    rs.getDouble("amount")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bids;
    }

    //  GANADOR DE LA SUBASTA (SOLO SI ESTÁ CERRADA)
    public WinnerInfo getWinnerByProduct(int productId) {

        String sql = """
            SELECT p.id_producto,
                   p.nombre AS producto,
                   u.username AS usuario,
                   b.amount
            FROM bids b
            JOIN productos p ON b.product_id = p.id_producto
            JOIN users u ON b.user_id = u.id
            WHERE p.id_producto = ?
              AND p.estado = 'CERRADA'
            ORDER BY b.amount DESC
            LIMIT 1
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new WinnerInfo(
                    rs.getInt("id_producto"),
                    rs.getString("producto"),
                    rs.getString("usuario"),
                    rs.getDouble("amount")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
