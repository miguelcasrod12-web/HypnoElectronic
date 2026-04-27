package com.hypnoelectronic.dao;

import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Método para compatibilidad con versiones anteriores
    public List<Producto> listar() {
        return listarTodos();
    }

    public List<Producto> listarTodos() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT p.*, c.nombre as cat_nom, prov.nombre as prov_nom " +
                     "FROM productos p " +
                     "LEFT JOIN categorias c ON p.categoria_id = c.id_categoria " +
                     "LEFT JOIN proveedores prov ON p.proveedor_id = prov.id_proveedor";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    // MÉTODO PUENTE: Para que los otros Servlets (API, Reportes, Test) no fallen
    public List<Producto> obtenerTodosProductos() {
        return listarTodos(); 
    }

    // --- NUEVO MÉTODO: Lógica de Filtrado y Ordenamiento Logístico ---
    public List<Producto> listarConFiltros(String categoria, String orden, String busqueda) {
        List<Producto> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.nombre as cat_nom, prov.nombre as prov_nom, p.categoria_id, p.proveedor_id " +
                                           "FROM productos p " +
                                           "LEFT JOIN categorias c ON p.categoria_id = c.id_categoria " +
                                           "LEFT JOIN proveedores prov ON p.proveedor_id = prov.id_proveedor WHERE 1=1");
        
        // 1. Filtro por categoría
        if (categoria != null && !categoria.isEmpty()) {
            sql.append(" AND p.categoria_id = ?");
        }

        // 2. Filtro por búsqueda de texto
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND (p.nombre LIKE ? OR p.descripcion LIKE ?)");
        }

        // Ordenamiento dinámico
        if (orden != null) {
            switch (orden) {
                // Whitelist para evitar inyección SQL
                case "precio_asc":
                    sql.append(" ORDER BY precio ASC");
                    break;
                case "precio_desc":
                    sql.append(" ORDER BY precio DESC");
                    break;
                case "nombre":
                    sql.append(" ORDER BY nombre ASC");
                    break;
                case "stock":
                    sql.append(" ORDER BY stock_actual DESC");
                default: sql.append(" ORDER BY p.id_producto DESC"); break;
            }
        }

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            
            int paramIndex = 1; // Índice para los parámetros del PreparedStatement
            if (categoria != null && !categoria.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(categoria));
            }
            if (busqueda != null && !busqueda.trim().isEmpty()) {
                String searchTerm = "%" + busqueda.trim() + "%";
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearProducto(rs));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Producto obtenerProductoPorId(int id) {
        String sql = "SELECT p.*, c.nombre as cat_nom, prov.nombre as prov_nom " +
                     "FROM productos p LEFT JOIN categorias c ON p.categoria_id = c.id_categoria " +
                     "LEFT JOIN proveedores prov ON p.proveedor_id = prov.id_proveedor " +
                     "WHERE p.id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearProducto(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insertarProducto(Producto p) {
        String sql = "INSERT INTO productos (nombre, descripcion, imagen_url, categoria_id, proveedor_id, precio, stock_actual) VALUES (?,?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getImagen_url());
            ps.setInt(4, p.getCategoriaId());
            ps.setInt(5, p.getProveedorId());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizarProducto(Producto p) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, imagen_url=?, categoria_id=?, proveedor_id=?, precio=?, stock_actual=? WHERE id_producto=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setString(3, p.getImagen_url());
            ps.setInt(4, p.getCategoriaId());
            ps.setInt(5, p.getProveedorId());
            ps.setDouble(6, p.getPrecio());
            ps.setInt(7, p.getStock());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id_producto"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setImagen_url(rs.getString("imagen_url"));
        p.setPrecio(rs.getDouble("precio"));
        p.setStock(rs.getInt("stock_actual"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setProveedorId(rs.getInt("proveedor_id"));

        // Traer nombres para reportes y catálogo
        try { p.setCategoriaNombre(rs.getString("cat_nom")); } catch (Exception e) {}
        try { p.setProveedorNombre(rs.getString("prov_nom")); } catch (Exception e) {}
        
        return p;
    }
}