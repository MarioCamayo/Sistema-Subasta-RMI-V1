package server.dao;

import common.Product;
import java.sql.*;
import java.util.*;
import server.DBConnection;

public class ProductDAO {

    // ================================
    //  OBTENER TODOS LOS PRODUCTOS
    // ================================
    public List<Product> getAllProducts() {

        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT id_producto,
                   nombre,
                   precio_inicial,
                   puja_actual,
                   estado
            FROM productos
            ORDER BY id_producto
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Product p = new Product(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_inicial"), // 🔒 NO cambia
                        rs.getDouble("puja_actual"),    // ⚙️ solo lógica
                        rs.getString("estado")
                );

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================================
    //  OBTENER PRODUCTO POR ID
    // ================================
    public Product getProductById(int productId) {

        String sql = """
            SELECT id_producto,
                   nombre,
                   precio_inicial,
                   puja_actual,
                   estado
            FROM productos
            WHERE id_producto = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("id_producto"),
                        rs.getString("nombre"),
                        rs.getDouble("precio_inicial"),
                        rs.getDouble("puja_actual"),
                        rs.getString("estado")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ================================
    //  ACTUALIZAR PUJA ACTUAL
    // ================================
    public void updateBid(int productId, double amount) {

        String sql = """
            UPDATE productos
            SET puja_actual = ?
            WHERE id_producto = ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, amount);
            ps.setInt(2, productId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================================
    //  CERRAR SUBASTAS EXPIRADAS
    // ================================
    public void closeExpiredAuctions() {

        String sql = """
            UPDATE productos
            SET estado = 'CERRADA'
            WHERE fecha_fin IS NOT NULL
              AND fecha_fin <= NOW()
              AND estado = 'ACTIVA'
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int closed = ps.executeUpdate();

            if (closed > 0) {
                System.out.println(" Subastas cerradas automaticamente: " + closed);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
