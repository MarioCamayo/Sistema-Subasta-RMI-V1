package server;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println(" Conectado a PostgreSQL correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
