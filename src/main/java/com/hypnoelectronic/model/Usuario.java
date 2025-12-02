package com.hypnoelectronic.model;

import java.sql.Timestamp;

public class Usuario {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String userType;
    private Timestamp createdAt;
    
    // Constructores
    public Usuario() {}
    
    public Usuario(String username, String password, String fullName, String email, String userType) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.userType = userType;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "Usuario [id=" + id + ", username=" + username + ", fullName=" + fullName + ", email=" + email + ", userType=" + userType + "]";
    }
}
