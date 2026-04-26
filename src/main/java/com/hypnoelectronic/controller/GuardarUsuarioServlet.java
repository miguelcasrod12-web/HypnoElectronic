package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.mindrot.jbcrypt.BCrypt; // Importar BCrypt
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/guardar-usuario")
public class GuardarUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        Usuario u = new Usuario();
        u.setFullName(request.getParameter("nombre"));
        u.setEmail(request.getParameter("email"));
        u.setUsername(request.getParameter("username"));
        u.setPassword(request.getParameter("password")); // El DAO se encarga del hash, no aquí
        u.setUserType(request.getParameter("role"));

        UsuarioDAO dao = new UsuarioDAO();
        if (dao.registrar(u)) {
            response.sendRedirect("usuarios-admin?success=added");
        } else {
            response.sendRedirect("usuarios-admin?error=fail");
        }
    }
}