package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/producto-detalle")
public class DetalleProductoServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        
        ProductoDAO dao = new ProductoDAO();
        Producto producto = dao.obtenerProductoPorId(id);
        
        if (producto != null) {
            request.setAttribute("p", producto);
            request.getRequestDispatcher("detalle.jsp").forward(request, response);
        } else {
            response.sendRedirect("home");
        }
    }
}