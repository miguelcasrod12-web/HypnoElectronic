package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/eliminar-usuario")
public class EliminarUsuarioServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario admin = (Usuario) request.getSession().getAttribute("user");
        int idEliminar = Integer.parseInt(request.getParameter("id"));

        // Mi opinión: Evitar el "autosuicidio" de cuenta admin
        if (admin != null && admin.getId() == idEliminar) {
            response.sendRedirect("usuarios-admin?error=selfdelete");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        dao.eliminarUsuario(idEliminar);
        response.sendRedirect("usuarios-admin?success=delete");
    }
}