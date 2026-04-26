package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.CategoriaDAO;
import com.hypnoelectronic.model.Categoria;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin-categorias")
public class CategoriaAdminServlet extends HttpServlet {
    
    private CategoriaDAO dao = new CategoriaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Seguridad: Solo admin
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        if ("eliminar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.eliminar(id);
        } else if ("editar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Categoria cat = dao.obtenerPorId(id);
            if (cat != null) {
                request.setAttribute("categoria", cat);
                request.getRequestDispatcher("editar-categoria.jsp").forward(request, response);
                return;
            }
        }

        // Cargar lista y mostrar JSP
        request.setAttribute("categorias", dao.listarTodas());
        request.getRequestDispatcher("categorias.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String accion = request.getParameter("accion");
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");

        if ("actualizar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Categoria cat = new Categoria(id, nombre, descripcion);
            if (dao.actualizar(cat)) {
                response.sendRedirect("admin-categorias?success=update");
            } else {
                response.sendRedirect("admin-categorias?error=update");
            }
        } else if (nombre != null && !nombre.trim().isEmpty()) {
            // Lógica de inserción (desde el modal de nueva categoría)
            Categoria nuevaCat = new Categoria();
            nuevaCat.setNombre(nombre);
            nuevaCat.setDescripcion(descripcion);
            
            if (dao.insertar(nuevaCat)) {
                response.sendRedirect("admin-categorias?success=add");
            } else {
                response.sendRedirect("admin-categorias?error=db");
            }
        } else {
            response.sendRedirect("admin-categorias?error=empty");
        }
    }
}