package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editar-usuario-form")
public class EditarUsuarioFormServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.obtenerUsuarioPorId(id);

        if (u != null) {
            request.setAttribute("usuarioEdit", u);
            request.getRequestDispatcher("editar-usuario.jsp").forward(request, response);
        } else {
            response.sendRedirect("usuarios-admin?error=notfound");
        }
    }
}