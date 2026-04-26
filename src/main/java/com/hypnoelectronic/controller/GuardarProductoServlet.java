package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/guardar-producto")
public class GuardarProductoServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String imagenUrl = request.getParameter("imagen_url");
            int categoriaId = Integer.parseInt(request.getParameter("categoriaId"));
            int proveedorId = Integer.parseInt(request.getParameter("proveedorId"));
            double precio = Double.parseDouble(request.getParameter("precio"));
            int stock = Integer.parseInt(request.getParameter("stock"));

            Producto p = new Producto();
            p.setNombre(nombre);
            p.setDescripcion(descripcion);
            p.setImagen_url(imagenUrl);
            p.setCategoriaId(categoriaId);
            p.setProveedorId(proveedorId);
            p.setPrecio(precio);
            p.setStock(stock);

            ProductoDAO dao = new ProductoDAO();
            boolean success = dao.insertarProducto(p);

            if (success) {
                // Volvemos al panel de inventario con éxito
                response.sendRedirect("admin-inventario?success=true");
            } else {
                response.sendRedirect("admin-inventario?error=db");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-inventario?error=data");
        }
    }
}