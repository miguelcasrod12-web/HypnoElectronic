package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Usuario;
import com.hypnoelectronic.model.ItemCarrito;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Simplemente mostrar la página de login
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("[LoginServlet] Validando en BD a: " + username);
        
        try {
            // Usamos el método 'login' del DAO actualizado
            Usuario usuario = usuarioDAO.login(username, password);
            
            if (usuario != null) {
                System.out.println("[LoginServlet] Entrada autorizada para: " + usuario.getFullName() + " con rol: " + usuario.getUserType());
                
                HttpSession session = request.getSession();
                // IMPORTANTE: El nombre "user" debe coincidir con el que usa en dashboard.jsp
                session.setAttribute("user", usuario);
                
                // LÓGICA DINÁMICA: Redirigir según contexto del carrito (Estilo Mercado Libre)
                Map<Integer, ItemCarrito> carrito = (Map<Integer, ItemCarrito>) session.getAttribute("carrito");
                
                if (carrito != null && !carrito.isEmpty()) {
                    // Si tenía cosas en el carrito, lo mandamos directo a finalizar la compra
                    response.sendRedirect("checkout");
                } else if (usuario.getUserType() != null && usuario.getUserType().equalsIgnoreCase("admin")) {
                    // Si es admin, al panel de control
                    response.sendRedirect("dashboard.jsp");
                } else {
                    // Si es cliente sin carrito, al inicio
                    response.sendRedirect("home"); 
                }
                
            } else {
                System.out.println("[LoginServlet] Credenciales inválidas para: " + username);
                request.setAttribute("errorMessage", "Usuario o contraseña incorrectos");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("[LoginServlet] ERROR: " + e.getMessage());
            request.setAttribute("errorMessage", "Error de conexión con la base de datos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}