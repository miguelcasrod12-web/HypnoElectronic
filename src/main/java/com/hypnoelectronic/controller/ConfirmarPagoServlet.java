package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.PedidoDAO;
import com.hypnoelectronic.model.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class ConfirmarPagoServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");
        Map<Integer, ItemCarrito> carrito = (Map<Integer, ItemCarrito>) session.getAttribute("carrito");
        
        // Recuperar datos de envío guardados en el paso anterior
        String direccion = (String) session.getAttribute("temp_direccion");
        String telefono = (String) session.getAttribute("temp_telefono");
        String ciudad = (String) session.getAttribute("temp_ciudad");

        if (user == null || carrito == null || carrito.isEmpty()) {
            response.sendRedirect("carrito.jsp");
            return;
        }

        try {
            // 1. Preparar el Pedido
            BigDecimal total = BigDecimal.ZERO;
            List<DetallePedido> detalles = new ArrayList<>();
            
            for (ItemCarrito item : carrito.values()) {
                DetallePedido d = new DetallePedido(item.getProducto().getId(), item.getCantidad(), item.getProducto().getPrecio());
                detalles.add(d);
                total = total.add(BigDecimal.valueOf(d.getSubtotal()));
            }

            Pedido pedido = new Pedido();
            pedido.setUsuarioId(user.getId());
            pedido.setTotal(total.doubleValue());
            pedido.setEstado("pagado");
            pedido.setDireccionEnvio(direccion);
            pedido.setTelefonoContacto(telefono);
            pedido.setCiudad(ciudad);

            // 2. Guardar en Base de Datos (Transaccional)
            if (new PedidoDAO().crearPedido(pedido, detalles)) {
                // 3. Limpiar todo si hubo éxito
                session.removeAttribute("carrito");
                session.removeAttribute("listaCarrito");
                session.removeAttribute("temp_direccion");
                session.removeAttribute("temp_telefono");
                session.removeAttribute("temp_ciudad");
                response.sendRedirect("confirmacion-compra.jsp?pedidoId=" + pedido.getId());
            } else {
                response.sendRedirect("carrito.jsp?error=db_fail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("carrito.jsp?error=critical");
        }
    }
}