package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.model.ItemCarrito;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/carrito")
public class CarritoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // 1. Recuperamos o inicializamos el carrito
        Map<Integer, ItemCarrito> carrito = (Map<Integer, ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new LinkedHashMap<>();
        }

        String accion = request.getParameter("accion");
        String idParam = request.getParameter("id");
        int id = (idParam != null) ? Integer.parseInt(idParam) : 0;

        if (accion != null && id > 0) {
            ProductoDAO dao = new ProductoDAO();
            
            if ("agregar".equals(accion) || "aumentar".equals(accion)) {
                // VALIDACIÓN DE STOCK PROFESIONAL
                Producto p = dao.obtenerProductoPorId(id);
                int cantidadActualEnCarrito = carrito.containsKey(id) ? carrito.get(id).getCantidad() : 0;

                if (p != null && (cantidadActualEnCarrito + 1) <= p.getStock()) {
                    // Hay stock suficiente
                    if (carrito.containsKey(id)) {
                        carrito.get(id).setCantidad(cantidadActualEnCarrito + 1);
                    } else {
                        carrito.put(id, new ItemCarrito(p, 1));
                    }
                    session.removeAttribute("errorStock"); 
                } else {
                    // No hay stock: Mi opinión es que el usuario debe saber exactamente por qué falló
                    String msg = "Stock insuficiente en Cajicá. Solo quedan " + (p != null ? p.getStock() : 0) + " unidades.";
                    session.setAttribute("errorStock", msg);
                }

            } else if ("disminuir".equals(accion)) {
                if (carrito.containsKey(id)) {
                    int nuevaCant = carrito.get(id).getCantidad() - 1;
                    if (nuevaCant <= 0) {
                        carrito.remove(id);
                    } else {
                        carrito.get(id).setCantidad(nuevaCant);
                    }
                }
            } else if ("eliminar".equals(accion)) {
                carrito.remove(id);
            }
        }

        // 2. Sincronizamos la sesión
        session.setAttribute("carrito", carrito);
        session.setAttribute("listaCarrito", new ArrayList<>(carrito.values()));
        
        // 3. Redirección logística: evita perder la posición del usuario
        if ("agregar".equals(accion)) {
            response.sendRedirect("home#prod-" + id);
        } else {
            response.sendRedirect("carrito.jsp#fila-" + id);
        }
    }
}