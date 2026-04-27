package com.hypnoelectronic.dao; // <-- Asegúrese de que diga DAO

import com.hypnoelectronic.model.Usuario;
import com.hypnoelectronic.util.DatabaseConnection;
import java.sql.Connection;          // Import fundamental
import org.mindrot.jbcrypt.BCrypt;   // Import para BCrypt
import java.sql.PreparedStatement;   // Import fundamental
import java.sql.ResultSet;           // Import fundamental
import java.sql.SQLException;        // Import fundamental
import java.sql.Statement;           // Import fundamental
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * LOGIN PRINCIPAL: Valida credenciales y trae el nombre del rol (admin/cliente)
     */
    public Usuario login(String username, String password) { // Añadido BCrypt
        Usuario user = null;
        // Cambiado a LEFT JOIN para evitar que falle si la tabla roles está incompleta
        String sql = "SELECT u.*, r.nombre_role " +
                     "FROM usuarios u " +
                     "LEFT JOIN roles r ON u.role_id = r.idrole " +
                     "WHERE u.username = ?";
        
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("🔍 Debug Login: Usuario encontrado -> " + username);
                // Verificamos la contraseña con BCrypt (antes no estaba)
                String hashedPassword = rs.getString("password"); // Obtener el hash de la DB
                // Comparar el password ingresado con el hash almacenado
                if (BCrypt.checkpw(password, hashedPassword)) {
                    user = extraerUsuarioDeResultSet(rs);
                    System.out.println("✅ Debug Login: Password coincide.");
                } else {
                    System.out.println("❌ Debug Login: Password NO coincide.");
                }
            } else {
                System.out.println("❌ Debug Login: Username no encontrado en DB.");
            }
        } catch (Exception e) {
            System.err.println("Error en login: " + e.getMessage());
        }
        return user;
    }

    // Método de compatibilidad para AuthAPIServlet
    public Usuario validarLogin(String username, String password) {
        return login(username, password);
    }

    /**
     * REGISTRO: Inserta con el role_id correspondiente
     */
    public boolean registrar(Usuario usuario) { // Añadido BCrypt
        // 1 para admin, 2 para cliente
        int roleId = (usuario.getUserType() != null && usuario.getUserType().equalsIgnoreCase("admin")) ? 1 : 2;
        String hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()); // Hash de la contraseña
        
        String sql = "INSERT INTO usuarios (nombre_completo, email, username, password, role_id) VALUES (?, ?, ?, ?, ?)"; 
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, usuario.getFullName());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getUsername());
            pstmt.setString(4, hashedPassword); // Guardamos la contraseña hasheada
            pstmt.setInt(5, roleId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en registro: " + e.getMessage());
            return false;
        }
    }

    // Método de compatibilidad para APIs
    public boolean insertarUsuario(Usuario usuario) {
        return registrar(usuario);
    }

    public Usuario obtenerUsuarioPorId(int id) {
        String sql = "SELECT u.*, r.nombre_role FROM usuarios u " +
                     "LEFT JOIN roles r ON u.role_id = r.idrole " +
                     "WHERE u.id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extraerUsuarioDeResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Usuario> obtenerTodosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.*, r.nombre_role FROM usuarios u " +
                     "LEFT JOIN roles r ON u.role_id = r.idrole";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(extraerUsuarioDeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Método para compatibilidad con versiones anteriores
    public List<Usuario> listarUsuarios() { return obtenerTodosUsuarios(); }

    public boolean actualizarUsuario(Usuario usuario) {
        int roleId = (usuario.getUserType() != null && usuario.getUserType().equalsIgnoreCase("admin")) ? 1 : 2;
        String sql;
        String hashedPassword = null;
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            hashedPassword = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt()); // Hash de la nueva contraseña
            sql = "UPDATE usuarios SET nombre_completo = ?, email = ?, username = ?, direccion = ?, telefono = ?, ciudad = ?, password = ?, role_id = ? WHERE id_usuario = ?";
        } else {
            sql = "UPDATE usuarios SET nombre_completo = ?, email = ?, username = ?, direccion = ?, telefono = ?, ciudad = ?, role_id = ? WHERE id_usuario = ?";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int i = 1;
            pstmt.setString(i++, usuario.getFullName());
            pstmt.setString(i++, usuario.getEmail());
            pstmt.setString(i++, usuario.getUsername());
            pstmt.setString(i++, usuario.getDireccion());
            pstmt.setString(i++, usuario.getTelefono());
            pstmt.setString(i++, usuario.getCiudad());
            if (hashedPassword != null) {
                pstmt.setString(i++, hashedPassword);
            }
            pstmt.setInt(i++, roleId);
            pstmt.setInt(i++, usuario.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mapea el ResultSet a un objeto Usuario
     */
    private Usuario extraerUsuarioDeResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setFullName(rs.getString("nombre_completo"));
        usuario.setEmail(rs.getString("email"));
        usuario.setDireccion(rs.getString("direccion"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setCiudad(rs.getString("ciudad"));
        // Verificación segura de la existencia de la columna de rol
        String role = rs.getString("nombre_role");
        usuario.setUserType(role != null ? role : "cliente");
        return usuario;
    }
}