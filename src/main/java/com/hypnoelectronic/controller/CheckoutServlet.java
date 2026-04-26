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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* @WebServlet("/checkout") */ // Comentado para usar web.xml
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Simplemente redirige a la página de confirmación de checkout
        // Aquí se podría mostrar un resumen final antes de confirmar
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
            // Calcular total del pedido
            BigDecimal totalPedido = BigDecimal.ZERO;
            List<DetallePedido> detallesPedido = new ArrayList<>();

            for (ItemCarrito item : carrito.values()) {
                DetallePedido detalle = new DetallePedido();
                detalle.setProductoId(item.getProducto().getId());
                detalle.setCantidad(item.getCantidad());
                detalle.setPrecioUnitario(BigDecimal.valueOf(item.getProducto().getPrecio()));
                detallesPedido.add(detalle);
                totalPedido = totalPedido.add(detalle.getSubtotal());
            }

            // Crear objeto Pedido
            Pedido pedido = new Pedido();
            pedido.setUsuarioId(usuario.getId());
            pedido.setTotal(totalPedido);
            pedido.setEstado("pagado"); // O 'pendiente' si hay un proceso de pago real

            // Guardar pedido y detalles en la base de datos, y actualizar stock
            PedidoDAO pedidoDAO = new PedidoDAO();
            boolean exito = pedidoDAO.crearPedido(pedido, detallesPedido);

            if (exito) {
                session.removeAttribute("carrito"); // Limpiar carrito después de la compra
                session.removeAttribute("listaCarrito");
                response.sendRedirect("confirmacion-compra.jsp?pedidoId=" + pedido.getId());
            } else {
                response.sendRedirect("carrito.jsp?error=checkout_failed");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("carrito.jsp?error=db_error&msg=" + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("carrito.jsp?error=internal_error&msg=" + e.getMessage());
        }
    }
}