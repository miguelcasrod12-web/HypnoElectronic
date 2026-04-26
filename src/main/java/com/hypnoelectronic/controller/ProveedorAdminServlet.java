package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProveedorDAO;
import com.hypnoelectronic.model.Proveedor;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ProveedorAdminServlet extends HttpServlet {
    
    private ProveedorDAO dao = new ProveedorDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accion = request.getParameter("accion");
        if ("eliminar".equals(accion)) {
            dao.eliminar(Integer.parseInt(request.getParameter("id")));
        } else if ("editar".equals(accion)) {
            Proveedor p = dao.obtenerPorId(Integer.parseInt(request.getParameter("id")));
            if (p != null) {
                request.setAttribute("proveedor", p);
                request.getRequestDispatcher("editar-proveedor.jsp").forward(request, response);
                return;
            }
        }

        request.setAttribute("proveedores", dao.listarTodos());
        request.getRequestDispatcher("proveedores.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        
        Proveedor p = new Proveedor();
        p.setNombre(request.getParameter("nombre"));
        p.setNit(request.getParameter("nit"));
        p.setContacto(request.getParameter("contacto"));
        p.setEmail(request.getParameter("email"));

        if ("actualizar".equals(accion)) {
            p.setId(Integer.parseInt(request.getParameter("id")));
            dao.actualizar(p);
            response.sendRedirect("admin-proveedores?success=update");
        } else {
            dao.insertar(p);
            response.sendRedirect("admin-proveedores?success=add");
        }
    }
}