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
    private Timestamp createdAt;

    // Constructor vacío (Obligatorio para Java Beans y Frameworks)
    public Usuario() {
    }

    // Constructor completo para registros rápidos
    public Usuario(String username, String password, String fullName, String email, String userType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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