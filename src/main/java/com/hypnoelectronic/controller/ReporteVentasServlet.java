package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.CategoriaDAO;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.model.Usuario;
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

        // Instanciar DAOs para obtener datos vivos
        ProductoDAO pDao = new ProductoDAO();
        UsuarioDAO uDao = new UsuarioDAO();
        CategoriaDAO cDao = new CategoriaDAO();

        List<Producto> productos = pDao.listarTodos();
        int stockCritico = 0;
        double valorTotal = 0;
        
        // Agrupar stock por categoría para la gráfica de pastel
        Map<String, Integer> stockPorCat = new HashMap<>();

        for (Producto p : productos) {
            if (p.getStock() < 5) stockCritico++;
            valorTotal += (p.getPrecio() * p.getStock());
            
            String catNom = (p.getCategoriaNombre() != null) ? p.getCategoriaNombre() : "Sin Categoría";
            stockPorCat.put(catNom, stockPorCat.getOrDefault(catNom, 0) + p.getStock());
        }

        request.setAttribute("productos", productos);
        request.setAttribute("totalUsers", uDao.obtenerTodosUsuarios().size());
        request.setAttribute("stockCriticoCount", stockCritico);
        request.setAttribute("valorInv", valorTotal);
        request.setAttribute("catData", stockPorCat);

        request.getRequestDispatcher("reportes.jsp").forward(request, response);
    }
}