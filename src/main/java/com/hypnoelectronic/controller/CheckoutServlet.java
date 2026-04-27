package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.PedidoDAO;
import com.hypnoelectronic.model.DetallePedido;
import com.hypnoelectronic.model.ItemCarrito;
import com.hypnoelectronic.model.Pedido;
import com.hypnoelectronic.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* @WebServlet("/checkout") */ // Comentado para usar web.xml
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<Integer, ItemCarrito> carrito = (Map<Integer, ItemCarrito>) session.getAttribute("carrito");
        
        double total = 0;
        if (carrito != null) {
            for (ItemCarrito item : carrito.values()) {
                total += item.getSubtotal();
            }
        }
        
        request.setAttribute("totalFinal", total);
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("user");
        Map<Integer, ItemCarrito> carrito = (Map<Integer, ItemCarrito>) session.getAttribute("carrito");

        // Validaciones de seguridad y negocio
        if (usuario == null) {
            response.sendRedirect("login.jsp?error=login_required");
            return;
        }
        if (carrito == null || carrito.isEmpty()) {
            response.sendRedirect("carrito.jsp?error=empty_cart");
            return;
        }

        try {
            // 1. Capturar datos de envío del formulario
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            String ciudad = request.getParameter("ciudad");
            
            // 2. Guardar temporalmente en sesión para cuando el pago sea exitoso
            session.setAttribute("temp_direccion", direccion);
            session.setAttribute("temp_telefono", telefono);
            session.setAttribute("temp_ciudad", ciudad);
            
            // 3. (Opcional) Actualizar perfil del usuario si no tenía estos datos
            if (usuario.getDireccion() == null || usuario.getDireccion().isEmpty()) {
                usuario.setDireccion(direccion);
                usuario.setTelefono(telefono);
                usuario.setCiudad(ciudad);
                new com.hypnoelectronic.dao.UsuarioDAO().actualizarUsuario(usuario);
            }

            // 4. Redirigir a la simulación de pasarela de pago
            response.sendRedirect("pasarela.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("carrito.jsp?error=internal_error&msg=" + e.getMessage());
        }
    }
}