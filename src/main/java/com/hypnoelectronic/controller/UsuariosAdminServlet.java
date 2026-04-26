package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios-admin")
public class UsuariosAdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        UsuarioDAO dao = new UsuarioDAO();
        // Mi opinión: usamos obtenerTodosUsuarios que es el estándar de su DAO
        List<Usuario> lista = dao.obtenerTodosUsuarios(); 
        request.setAttribute("usuarios", lista);
        request.getRequestDispatcher("usuarios.jsp").forward(request, response);
    }
}