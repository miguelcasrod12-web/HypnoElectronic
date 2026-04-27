package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.CategoriaDAO;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Categoria;
import com.hypnoelectronic.model.Producto;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * Controlador que prepara la vista de detalle y las recomendaciones
 */
public class DetalleProductoServlet extends HttpServlet {
    private ProductoDAO productoDAO = new ProductoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            
            // 1. Obtener producto principal
            Producto p = productoDAO.obtenerProductoPorId(id);
            
            if (p != null) {
                // 2. Cargar listas para recomendaciones (EL PASO QUE FALTABA)
                List<Producto> todosLosProductos = productoDAO.listarTodos();
                List<Categoria> todasLasCategorias = categoriaDAO.listarTodas();

                request.setAttribute("p", p);
                request.setAttribute("productos", todosLosProductos);
                request.setAttribute("categorias", todasLasCategorias);
                
                request.getRequestDispatcher("detalle.jsp").forward(request, response);
            } else {
                response.sendRedirect("home");
            }
        } catch (Exception e) {
            response.sendRedirect("home");
        }
    }
}