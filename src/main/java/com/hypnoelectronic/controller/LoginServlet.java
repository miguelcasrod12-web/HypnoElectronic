package com.hypnoelectronic.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Configuración MySQL 9.5
    private String jdbcURL = "jdbc:mysql://localhost:3306/HypnoElectronic";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Root1234";  // La contraseña que usaste para conectar
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Mostrar formulario de login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            if(authenticateUser(username, password)) {
                // Crear sesión
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                
                // Redirigir al dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                // Credenciales incorrectas
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error en el servidor: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
    
    private boolean authenticateUser(String username, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        
        try {
            // ✅ CORREGIDO: Driver MySQL (no SQL Server)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer conexión
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);
            
            result = statement.executeQuery();
            boolean isValid = result.next();
            
            return isValid;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Cerrar recursos
            try {
                if (result != null) result.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
