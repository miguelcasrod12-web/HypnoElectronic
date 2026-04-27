package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.PedidoDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class PedidoAdminServlet extends HttpServlet {
    private PedidoDAO dao = new PedidoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }
        request.setAttribute("pedidos", dao.listarTodos());
        request.getRequestDispatcher("pedidos-admin.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        if ("actualizar".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String nuevoEstado = request.getParameter("estado");
            String guia = request.getParameter("guia");
            
            if (dao.actualizarEstado(id, nuevoEstado, guia)) {
                response.sendRedirect("admin-pedidos?success=true");
            } else {
                response.sendRedirect("admin-pedidos?error=true");
            }
        }
    }
}