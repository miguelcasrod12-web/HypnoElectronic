package com.hypnoelectronic.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/HypnoElectronic";
    private static final String USER = "root";
    private static final String PASSWORD = "0000"; // Tu contraseña
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL cargado correctamente");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se pudo cargar el driver MySQL");
            e.printStackTrace();
            throw new RuntimeException("Error cargando driver MySQL", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        System.out.println("Intentando conectar a: " + URL);
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("¡Conexión exitosa a la base de datos!");
        return conn;
    }
    
    // Método de prueba (opcional)
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            System.out.println("Prueba de conexión: OK");
            conn.close();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            e.printStackTrace();
        }
    }
}