package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.PedidoDAO;
import com.hypnoelectronic.model.Pedido;
import com.hypnoelectronic.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/mis-compras")
public class MisComprasServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario user = (Usuario) session.getAttribute("user");

        // Seguridad: Si no hay sesión, al login
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        PedidoDAO pedidoDAO = new PedidoDAO();
        List<Pedido> pedidos = pedidoDAO.obtenerPedidosPorUsuario(user.getId());
        request.setAttribute("pedidos", pedidos);
        request.getRequestDispatcher("mis-compras.jsp").forward(request, response);
    }
}