package com.hypnoelectronic.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/HypnoElectronic";
    private static final String USER = "root";
    private static final String PASSWORD = "";  // Cambia si tienes contraseña
    
    static {
        try {
            // Registrar el driver (necesario para versiones antiguas)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar el driver MySQL", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Método para probar la conexión
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Conexión a MySQL establecida correctamente");
            return true;
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión a MySQL: " + e.getMessage());
            return false;
        }
    }
}
