package com.hypnoelectronic.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnection {
    
    private static HikariDataSource dataSource;
    
    static {
        try {
            Properties props = new Properties();
            try (InputStream is = DatabaseConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (is == null) throw new RuntimeException("No se encontró db.properties");
                props.load(is);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(props.getProperty("db.url"));
            config.setUsername(props.getProperty("db.user"));
            config.setPassword(props.getProperty("db.password"));
            config.setDriverClassName(props.getProperty("db.driver"));
            
            // Configuraciones profesionales adicionales
            config.setMaximumPoolSize(10);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");

            dataSource = new HikariDataSource(config);
            System.out.println("Pool de conexiones HikariCP inicializado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}