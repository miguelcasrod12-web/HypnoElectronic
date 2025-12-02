import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Cargar configuración
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            
            System.out.println("🔗 Probando conexión a: " + url);
            
            // Establecer conexión
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexión exitosa!");
            
            // Verificar tablas
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            
            System.out.println("📊 Tablas en la base de datos:");
            while (rs.next()) {
                System.out.println("   - " + rs.getString(1));
            }
            
            // Contar usuarios
            rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios");
            if (rs.next()) {
                System.out.println("👤 Usuarios en sistema: " + rs.getInt(1));
            }
            
            // Contar productos
            rs = stmt.executeQuery("SELECT COUNT(*) FROM productos");
            if (rs.next()) {
                System.out.println("📦 Productos en sistema: " + rs.getInt(1));
            }
            
            conn.close();
            System.out.println("🎉 Prueba completada exitosamente!");
            
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
