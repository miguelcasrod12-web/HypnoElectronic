package com.hypnoelectronic.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Modelo de Usuario sincronizado con la base de datos 'mydb'
 * Autor: Miguel Castillo
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String userType; // Aquí guardaremos "admin" o "cliente"
    private String direccion;
    private String telefono;
    private String ciudad;
    private Timestamp createdAt;

    // Constructor vacío (Obligatorio para Java Beans y Frameworks)
    public Usuario() {
    }

    // Constructor completo para registros rápidos
    public Usuario(String username, String password, String fullName, String email, String userType, String direccion, String telefono, String ciudad) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
        this.direccion = direccion;
        this.telefono = telefono;
        this.ciudad = ciudad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * userType mapea directamente al nombre_role de la tabla roles
     */
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Método útil para depuración en consola
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + userType + '\'' +
                '}';
    }
}