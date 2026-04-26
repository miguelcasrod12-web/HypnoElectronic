package com.hypnoelectronic.dao;

import com.hypnoelectronic.model.DetallePedido;
import com.hypnoelectronic.model.Pedido;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public boolean crearPedido(Pedido pedido, List<DetallePedido> detalles) throws SQLException {
        final String SQL_INSERT_ORDER = "INSERT INTO pedidos (usuario_id, total, estado) VALUES (?, ?, ?)";
        final String SQL_INSERT_DETAIL = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        final String SQL_UPDATE_STOCK = "UPDATE productos SET stock_actual = stock_actual - ? WHERE id_producto = ?";

        try (Connection dbConnection = DatabaseConnection.getConnection()) {
            dbConnection.setAutoCommit(false); 

            try (PreparedStatement stmtOrder = dbConnection.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtDetail = dbConnection.prepareStatement(SQL_INSERT_DETAIL);
                 PreparedStatement stmtStock = dbConnection.prepareStatement(SQL_UPDATE_STOCK)) {
                
                // Fase 1: Cabecera del pedido
                stmtOrder.setInt(1, pedido.getUsuarioId());
                stmtOrder.setBigDecimal(2, pedido.getTotal());
                stmtOrder.setString(3, pedido.getEstado());
                stmtOrder.executeUpdate();

                int newOrderId = 0;
                try (ResultSet generatedKeys = stmtOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newOrderId = generatedKeys.getInt(1);
                        pedido.setId(newOrderId);
                    } else {
                        throw new SQLException("No se pudo recuperar el ID del nuevo pedido.");
                    }
                }
                pedido.setId(newOrderId); // Asegurarse de que el ID se asigne al objeto Pedido

                // Fase 2: Procesar ítems y descontar inventario
                for (DetallePedido item : detalles) {
                    stmtDetail.setInt(1, newOrderId);
                    stmtDetail.setInt(2, item.getProductoId());
                    stmtDetail.setInt(3, item.getCantidad());
                    stmtDetail.setBigDecimal(4, item.getPrecioUnitario());
                    stmtDetail.addBatch();

                    stmtStock.setInt(1, item.getCantidad());
                    stmtStock.setInt(2, item.getProductoId());
                    stmtStock.addBatch();
                }

                stmtDetail.executeBatch();
                stmtStock.executeBatch();

                dbConnection.commit(); 
                return true;

            } catch (SQLException e) {
                dbConnection.rollback();
                System.err.println("CRITICAL: Error en proceso de compra -> " + e.getMessage());
                throw e;
            }
        }
    }

    // Métodos para listar pedidos de un usuario, obtener detalles de un pedido, etc.
    // ...

    public List<Pedido> obtenerPedidosPorUsuario(int usuarioId) {
        List<Pedido> lista = new ArrayList<>();
        // Ordenamos por ID descendente para ver las compras más recientes primero
        String sql = "SELECT * FROM pedidos WHERE usuario_id = ? ORDER BY id_pedido DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pedido p = new Pedido();
                    p.setId(rs.getInt("id_pedido"));
                    p.setUsuarioId(rs.getInt("usuario_id"));
                    p.setTotal(rs.getBigDecimal("total"));
                    p.setEstado(rs.getString("estado"));
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Pedido obtenerPedidoConDetalles(int pedidoId) {
        // Implementar lógica para obtener un pedido con sus detalles
        return null;
    }
}