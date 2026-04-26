package com.hypnoelectronic.dao;

import com.hypnoelectronic.model.Proveedor;
import com.hypnoelectronic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    public List<Proveedor> listarTodos() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Proveedor obtenerPorId(int id) {
        String sql = "SELECT * FROM proveedores WHERE id_proveedor = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insertar(Proveedor p) {
        String sql = "INSERT INTO proveedores (nombre, nit, contacto, email) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getNit());
            ps.setString(3, p.getContacto());
            ps.setString(4, p.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean actualizar(Proveedor p) {
        String sql = "UPDATE proveedores SET nombre=?, nit=?, contacto=?, email=? WHERE id_proveedor=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getNit());
            ps.setString(3, p.getContacto());
            ps.setString(4, p.getEmail());
            ps.setInt(5, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM proveedores WHERE id_proveedor = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Proveedor mapear(ResultSet rs) throws SQLException {
        return new Proveedor(
            rs.getInt("id_proveedor"),
            rs.getString("nombre"),
            rs.getString("nit"),
            rs.getString("contacto"),
            rs.getString("email")
        );
    }
}