package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cambiar-rol")
public class CambiarRolServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Mi opinión: Seguridad ante todo. Solo un admin puede cambiar roles.
        Usuario adminLogueado = (Usuario) request.getSession().getAttribute("user");
        if (adminLogueado == null || !"admin".equalsIgnoreCase(adminLogueado.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int idUsuario = Integer.parseInt(request.getParameter("id"));
            UsuarioDAO dao = new UsuarioDAO();
            
            // 1. Buscamos el usuario actual
            Usuario u = dao.obtenerUsuarioPorId(idUsuario);
            
            if (u != null) {
                // 2. Lógica de intercambio (Admin <-> Cliente)
                // En su DB: 1 es Admin, 2 es Cliente
                if ("admin".equalsIgnoreCase(u.getUserType())) {
                    u.setUserType("cliente"); // Esto el DAO lo convertirá en role_id = 2
                } else {
                    u.setUserType("admin");   // Esto el DAO lo convertirá en role_id = 1
                }
                
                // 3. Guardamos el cambio
                dao.actualizarUsuario(u);
            }
            
            response.sendRedirect("usuarios-admin?success=role");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("usuarios-admin?error=role");
        }
    }
}