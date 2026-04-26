package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/actualizar-usuario-ejecutar")
public class ActualizarUsuarioServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        Usuario u = new Usuario();
        u.setId(Integer.parseInt(request.getParameter("id")));
        u.setFullName(request.getParameter("nombre"));
        u.setEmail(request.getParameter("email"));
        u.setUsername(request.getParameter("username"));
        u.setPassword(request.getParameter("password"));
        
        // Buscamos el tipo actual para no perderlo
        UsuarioDAO dao = new UsuarioDAO();
        Usuario actual = dao.obtenerUsuarioPorId(u.getId());
        u.setUserType(actual.getUserType());

        if (dao.actualizarUsuario(u)) {
            response.sendRedirect("usuarios-admin?success=updated");
        } else {
            response.sendRedirect("usuarios-admin?error=fail");
        }
    }
}