package com.hypnoelectronic.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Configuración MySQL (igual que LoginServlet)
    private String jdbcURL = "jdbc:mysql://localhost:3306/HypnoElectronic";
    private String jdbcUsername = "root";
    private String jdbcPassword = "Root1234";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // Obtener parámetros del formulario
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            // Cargar driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Conectar a la base de datos
            conn = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
            
            // Insertar usuario
            String sql = "INSERT INTO usuarios (username, password, fullName, email, userType) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setString(4, email);
            stmt.setString(5, userType);
            
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                // Registro exitoso - redirigir con parámetro de éxito
                response.sendRedirect("register.jsp?success=true");
            } else {
                out.println("<h3>Error en el registro</h3>");
                out.println("<a href='register.jsp'>Intentar de nuevo</a>");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3>Error de base de datos: " + e.getMessage() + "</h3>");
            out.println("<a href='register.jsp'>Volver</a>");
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Redirigir al formulario de registro
        response.sendRedirect("register.jsp");
    }
}
