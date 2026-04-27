package com.hypnoelectronic.dao;

import com.hypnoelectronic.model.Pedido;
import com.hypnoelectronic.model.DetallePedido;
import com.hypnoelectronic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.nombre_completo " +
                     "FROM pedidos p " +
                     "LEFT JOIN usuarios u ON p.usuario_id = u.id_usuario " +
                     "ORDER BY p.fecha_pedido DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pedido p = new Pedido();
                p.setId(rs.getInt("id_pedido"));
                String nombre = rs.getString("nombre_completo");
                p.setUsuarioNombre(nombre != null ? nombre : "Usuario Invitado/Eliminado");
                p.setFecha(rs.getTimestamp("fecha_pedido"));
                p.setTotal(rs.getDouble("total"));
                p.setEstado(rs.getString("estado"));
                p.setDireccionEnvio(rs.getString("direccion_envio"));
                p.setTelefonoContacto(rs.getString("telefono_contacto"));
                p.setCiudad(rs.getString("ciudad"));
                p.setNumeroGuia(rs.getString("numero_guia"));
                lista.add(p);
            }
        } catch (SQLException e) { 
            System.err.println("❌ ERROR CRÍTICO en PedidoDAO.listarTodos: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    public boolean crearPedido(Pedido pedido, List<DetallePedido> detalles) {
        String sqlPedido = "INSERT INTO pedidos (usuario_id, total, estado, direccion_envio, telefono_contacto, ciudad) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET stock_actual = stock_actual - ? WHERE id_producto = ?";
        
        Connection con = null;
        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // Iniciar transacción

            try (PreparedStatement psP = con.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                psP.setInt(1, pedido.getUsuarioId());
                psP.setDouble(2, pedido.getTotal());
                psP.setString(3, pedido.getEstado() != null ? pedido.getEstado() : "pendiente");
                psP.setString(4, pedido.getDireccionEnvio());
                psP.setString(5, pedido.getTelefonoContacto());
                psP.setString(6, pedido.getCiudad());
                psP.executeUpdate();

                ResultSet rs = psP.getGeneratedKeys();
                if (rs.next()) {
                    int idPedido = rs.getInt(1);
                    pedido.setId(idPedido); // Sincronizamos el ID para la vista de éxito
                    
                    try (PreparedStatement psD = con.prepareStatement(sqlDetalle);
                         PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {
                    for (DetallePedido d : detalles) {
                            psD.setInt(1, idPedido);
                            psD.setInt(2, d.getProductoId());
                            psD.setInt(3, d.getCantidad());
                            psD.setDouble(4, d.getPrecioUnitario());
                            psD.addBatch();
                            
                            // Aprovechamos la transacción para bajar el stock
                            psStock.setInt(1, d.getCantidad());
                            psStock.setInt(2, d.getProductoId());
                            psStock.addBatch();
                        }
                        psD.executeBatch();
                        psStock.executeBatch();
                    }
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT * FROM pedidos WHERE usuario_id = ? ORDER BY fecha_pedido DESC";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id_pedido"));
                    p.setUsuarioId(rs.getInt("usuario_id"));
                    p.setFecha(rs.getTimestamp("fecha_pedido"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setNumeroGuia(rs.getString("numero_guia"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public boolean actualizarEstado(int id, String estado, String guia) {
        String sql = "UPDATE pedidos SET estado = ?, numero_guia = ? WHERE id_pedido = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, guia);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public Pedido obtenerPorId(int id) {
        String sql = "SELECT p.*, u.nombre_completo FROM pedidos p " +
                     "JOIN usuarios u ON p.usuario_id = u.id_usuario WHERE p.id_pedido = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id_pedido"));
                    p.setUsuarioNombre(rs.getString("nombre_completo"));
                    p.setFecha(rs.getTimestamp("fecha_pedido"));
                    p.setTotal(rs.getDouble("total"));
                    p.setEstado(rs.getString("estado"));
                    p.setNumeroGuia(rs.getString("numero_guia"));
                    p.setCiudad(rs.getString("ciudad"));
                    return p;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}