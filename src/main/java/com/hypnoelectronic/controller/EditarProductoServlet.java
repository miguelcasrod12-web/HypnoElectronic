package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.CategoriaDAO;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dao.ProveedorDAO;
import com.hypnoelectronic.model.Producto;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/editar-producto")
public class EditarProductoServlet extends HttpServlet {

    // doGet: Muestra el formulario con los datos actuales
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductoDAO dao = new ProductoDAO();
        Producto p = dao.obtenerProductoPorId(id);

        if (p != null) {
            CategoriaDAO catDao = new CategoriaDAO();
            ProveedorDAO provDao = new ProveedorDAO();

            request.setAttribute("producto", p);
            request.setAttribute("categorias", catDao.listarTodas());
            request.setAttribute("proveedores", provDao.listarTodos());

            request.getRequestDispatcher("editar-producto.jsp").forward(request, response);
        } else {
            response.sendRedirect("admin-inventario?error=notfound");
        }
    }

    // doPost: Recibe los cambios y los guarda
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        Producto p = new Producto();
        p.setId(Integer.parseInt(request.getParameter("id")));
        p.setNombre(request.getParameter("nombre"));
        p.setDescripcion(request.getParameter("descripcion"));
        p.setImagen_url(request.getParameter("imagen_url"));
        p.setCategoriaId(Integer.parseInt(request.getParameter("categoriaId")));
        p.setProveedorId(Integer.parseInt(request.getParameter("proveedorId")));
        p.setPrecio(Double.parseDouble(request.getParameter("precio")));
        p.setStock(Integer.parseInt(request.getParameter("stock")));

        ProductoDAO dao = new ProductoDAO();
        if (dao.actualizarProducto(p)) {
            response.sendRedirect("admin-inventario?success=update");
        } else {
            response.sendRedirect("admin-inventario?error=update");
        }
    }
}