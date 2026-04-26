package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin-inventario")
public class ProductoAdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equals(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        ProductoDAO dao = new ProductoDAO();
        request.setAttribute("productos", dao.listarTodos());
        request.getRequestDispatcher("inventario.jsp").forward(request, response);
    }
}