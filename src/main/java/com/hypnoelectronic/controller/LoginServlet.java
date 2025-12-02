package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Mostrar formulario de login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("[LoginServlet] Intentando login para usuario: " + username);
        
        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.validarLogin(username, password);
            
            if (usuario != null) {
                System.out.println("[LoginServlet] Login exitoso: " + usuario.getFullName());
                
                // Crear sesión con el objeto Usuario completo
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                session.setAttribute("username", usuario.getUsername());
                session.setAttribute("userType", usuario.getUserType());
                
                // Redirigir según tipo de usuario
                if ("admin".equals(usuario.getUserType())) {
                    response.sendRedirect("admin/dashboard.jsp");
                } else {
                    response.sendRedirect("dashboard.jsp");
                }
                
            } else {
                System.out.println("[LoginServlet] Login fallido para: " + username);
                
                // Credenciales incorrectas
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("[LoginServlet] Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error en el servidor: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}