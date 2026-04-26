package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Recoger datos del formulario
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");

        // 2. Crear objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setFullName(fullName);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPassword(password); // Se hashea en el DAO, no aquí
        nuevoUsuario.setUserType(userType);

        // 3. Intentar guardar en la base de datos
        if (usuarioDAO.registrar(nuevoUsuario)) {
            // Si funciona, lo mandamos al login con éxito
            response.sendRedirect("login.jsp?success=true");
        } else {
            // Si falla, volvemos al registro con error
            request.setAttribute("errorMessage", "No se pudo crear la cuenta. El usuario o correo ya existen.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}