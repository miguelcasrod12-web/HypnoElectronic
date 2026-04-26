package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.util.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/entrada-almacen")
public class EntradaAlmacenServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        int productoId = Integer.parseInt(request.getParameter("productoId"));
        int cantidad = Integer.parseInt(request.getParameter("cantidad"));
        double precioCompra = Double.parseDouble(request.getParameter("precioCompra"));

        ProductoDAO productoDAO = new ProductoDAO();
        Producto productoExistente = productoDAO.obtenerProductoPorId(productoId);

        if (productoExistente == null) {
            response.sendRedirect("admin-inventario?error=producto_no_encontrado");
            return;
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Registrar la entrada en entradas_almacen
            String sqlInsertEntrada = "INSERT INTO entradas_almacen (producto_id, cantidad, precio_compra) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertEntrada)) {
                ps.setInt(1, productoId);
                ps.setInt(2, cantidad);
                ps.setDouble(3, precioCompra);
                ps.executeUpdate();
            }

            // 2. Actualizar el stock_actual del producto
            String sqlUpdateStock = "UPDATE productos SET stock_actual = stock_actual + ? WHERE id_producto = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateStock)) {
                ps.setInt(1, cantidad);
                ps.setInt(2, productoId);
                ps.executeUpdate();
            }

            conn.commit(); // Confirmar transacción
            response.sendRedirect("admin-inventario?success=entrada_registrada");
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } // Revertir transacción
            e.printStackTrace();
            response.sendRedirect("admin-inventario?error=error_db_entrada");
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}