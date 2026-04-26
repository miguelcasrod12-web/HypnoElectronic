package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import org.mindrot.jbcrypt.BCrypt; // Importar BCrypt
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/update-profile")
public class UpdateProfileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario userActual = (Usuario) session.getAttribute("user");

        if (userActual == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String newPass = request.getParameter("newPassword");

        userActual.setFullName(fullName);
        userActual.setEmail(email);
        
        // Se envía la contraseña plana al DAO, él se encarga de hashear
        if (newPass != null && !newPass.trim().isEmpty()) { // Si se envió una nueva contraseña
            userActual.setPassword(newPass); 
        }

        UsuarioDAO dao = new UsuarioDAO();
        boolean success = dao.actualizarUsuario(userActual);

        if (success) {
            session.setAttribute("user", userActual);
            response.sendRedirect("configuracion.jsp?success=true");
        } else {
            response.sendRedirect("configuracion.jsp?error=true");
        }
    }
}