package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.CategoriaDAO;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String catParam = request.getParameter("categoria"); // Filtro por categoría
            String ordenParam = request.getParameter("orden");   // Ordenamiento
            String busquedaParam = request.getParameter("buscar"); // Búsqueda por texto

            ProductoDAO productoDAO = new ProductoDAO();
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            
            // Lógica unificada: aplica filtros y búsqueda si existen, si no, lista todo
            List<Producto> listaProductos = productoDAO.listarConFiltros(catParam, ordenParam, busquedaParam);

            // Cargamos los datos necesarios para la vista
            request.setAttribute("productos", listaProductos);
            request.setAttribute("categorias", categoriaDAO.listarTodas());

            // Único despacho: se eliminan forward duplicados
            request.getRequestDispatcher("catalogo.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error logístico: No se pudo cargar el inventario.");
        }
    }
}