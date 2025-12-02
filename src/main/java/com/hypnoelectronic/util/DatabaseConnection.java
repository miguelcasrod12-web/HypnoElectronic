package com.hypnoelectronic.util;

import com.hypnoelectronic.dao.ConfiguracionDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Estos valores ahora se obtienen DINÁMICAMENTE de la BD
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    static {
        cargarConfiguracion();
    }
    
    private static void cargarConfiguracion() {
        URL = ConfiguracionDAO.getValor("db_url");
        USER = ConfiguracionDAO.getValor("db_user");
        PASSWORD = ConfiguracionDAO.getValor("db_password");
        
        System.out.println("[DatabaseConnection] Configuración cargada:");
        System.out.println("  URL: " + URL);
        System.out.println("  User: " + USER);
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println(" Driver MySQL cargado");
        } catch (ClassNotFoundException e) {
            System.err.println(" Driver no encontrado: " + e.getMessage());
            throw new RuntimeException("Error al cargar el driver MySQL", e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            cargarConfiguracion(); // Recargar si es necesario
        }
        
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Conexión establecida a: " + URL);
            return conn;
        } catch (SQLException e) {
            System.err.println(" Error de conexión:");
            System.err.println("  URL: " + URL);
            System.err.println("  User: " + USER);
            System.err.println("  Error: " + e.getMessage());
            throw e;
        }
    }
    
    public static void actualizarConfiguracion(String url, String user, String password) {
        ConfiguracionDAO.actualizarValor("db_url", url);
        ConfiguracionDAO.actualizarValor("db_user", user);
        ConfiguracionDAO.actualizarValor("db_password", password);
        
        // Actualizar variables estáticas
        URL = url;
        USER = user;
        PASSWORD = password;
    }
}