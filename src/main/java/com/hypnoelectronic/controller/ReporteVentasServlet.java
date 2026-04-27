package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.CategoriaDAO;
import com.hypnoelectronic.dao.PedidoDAO;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Pedido;
import com.hypnoelectronic.model.Usuario;
import com.hypnoelectronic.model.Producto;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la gestión de reportes de ventas
 * Ruta: /reporte-ventas
 */
public class ReporteVentasServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificación de seguridad
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        ProductoDAO pDao = new ProductoDAO();
        UsuarioDAO uDao = new UsuarioDAO();
        PedidoDAO pedDao = new PedidoDAO();

        // 1. Obtener la lista maestra una sola vez para garantizar consistencia
        List<Producto> productos = pDao.listarTodos();
        
        // 2. Calcular métricas basadas EXACTAMENTE en la misma lista
        long stockCritico = productos.stream()
                            .filter(p -> p.getStock() < 5)
                            .count();

        double valorTotal = productos.stream()
                            .mapToDouble(p -> p.getPrecio() * p.getStock())
                            .sum();

        // 3. Procesar datos de Ventas
        List<Pedido> pedidos = pedDao.listarTodos();
        double ventasTotales = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
        
        Map<String, Integer> stockPorCat = new HashMap<>();
        Map<String, Integer> stockPorProv = new HashMap<>();
        Map<String, Integer> pedidosPorEstado = new HashMap<>();

        for (Pedido ped : pedidos) {
            String est = (ped.getEstado() != null) ? ped.getEstado() : "pendiente";
            pedidosPorEstado.put(est, pedidosPorEstado.getOrDefault(est, 0) + 1);
        }

        for (Producto p : productos) {
            // Agrupar por Categoría
            String catNom = (p.getCategoriaNombre() != null) ? p.getCategoriaNombre() : "Otros";
            stockPorCat.put(catNom, stockPorCat.getOrDefault(catNom, 0) + p.getStock());
            
            // Agrupar por Proveedor
            String provNom = (p.getProveedorNombre() != null) ? p.getProveedorNombre() : "Sin Asignar";
            stockPorProv.put(provNom, stockPorProv.getOrDefault(provNom, 0) + p.getStock());
        }

        // Contar solo CLIENTES activos (excluyendo administradores) para evitar desfases
        long clientesReales = uDao.obtenerTodosUsuarios().stream()
                                .filter(u -> "cliente".equalsIgnoreCase(u.getUserType()))
                                .count();

        request.setAttribute("productos", productos);
        request.setAttribute("pedidos", pedidos);
        request.setAttribute("totalUsers", (int) clientesReales);
        request.setAttribute("stockCriticoCount", (int) stockCritico);
        request.setAttribute("valorInv", valorTotal);
        request.setAttribute("ventasTotales", ventasTotales);
        request.setAttribute("catData", stockPorCat);
        request.setAttribute("provData", stockPorProv);
        request.setAttribute("statusData", pedidosPorEstado);

        request.getRequestDispatcher("reportes.jsp").forward(request, response);
    }
}