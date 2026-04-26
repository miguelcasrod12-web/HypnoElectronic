package com.hypnoelectronic.dao;

import com.hypnoelectronic.model.Categoria;
import com.hypnoelectronic.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Categoria(
                    rs.getInt("id_categoria"),
                    rs.getString("nombre"),
                    rs.getString("descripcion")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    public Categoria obtenerPorId(int id) {
        String sql = "SELECT * FROM categorias WHERE id_categoria = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("descripcion")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean insertar(Categoria cat) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cat.getNombre());
            ps.setString(2, cat.getDescripcion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean actualizar(Categoria cat) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id_categoria = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cat.getNombre());
            ps.setString(2, cat.getDescripcion());
            ps.setInt(3, cat.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id_categoria = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return false; 
        }
    }
}