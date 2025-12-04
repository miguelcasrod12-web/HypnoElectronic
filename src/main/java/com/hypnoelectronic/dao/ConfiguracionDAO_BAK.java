package com.hypnoelectronic.dao;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ConfiguracionDAO {
    private static Map<String, String> configCache = new HashMap<>();
    private static boolean cacheLoaded = false;
    
    public static String getValor(String clave) {
        if (!cacheLoaded) {
            cargarConfiguracion();
        }
        return configCache.getOrDefault(clave, "");
    }
    
    private static void cargarConfiguracion() {
        String sql = "SELECT clave, valor FROM configuracion";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                configCache.put(rs.getString("clave"), rs.getString("valor"));
            }
            cacheLoaded = true;
            System.out.println(" Configuración cargada: " + configCache.size() + " parámetros");
            
        } catch (SQLException e) {
            System.err.println(" Error cargando configuración: " + e.getMessage());
            // Valores por defecto
            configCache.put("db_url", "jdbc:mysql://localhost:3306/HypnoElectronic");
            configCache.put("db_user", "root");
            configCache.put("db_password", "0000");
        }
    }
    
    public static void actualizarValor(String clave, String valor) {
        String sql = "INSERT INTO configuracion (clave, valor) VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE valor = ?, fecha_actualizacion = CURRENT_TIMESTAMP";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, clave);
            pstmt.setString(2, valor);
            pstmt.setString(3, valor);
            pstmt.executeUpdate();
            
            configCache.put(clave, valor);
            System.out.println(" Configuración actualizada: " + clave + " = " + valor);
            
        } catch (SQLException e) {
            System.err.println(" Error actualizando configuración: " + e.getMessage());
        }
    }
    
    public static Map<String, String> getAllConfig() {
        if (!cacheLoaded) {
            cargarConfiguracion();
        }
        return new HashMap<>(configCache);
    }
}