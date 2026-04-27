package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.PedidoDAO;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Pedido;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReporteDetalleServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario user = (Usuario) request.getSession().getAttribute("user");
        if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
            response.sendRedirect("login.jsp");
            return;
        }

        String tipo = request.getParameter("tipo");
        if (tipo == null || tipo.trim().isEmpty()) {
            response.sendRedirect("reporte-ventas");
            return;
        }

        ProductoDAO pDao = new ProductoDAO();
        UsuarioDAO uDao = new UsuarioDAO();
        PedidoDAO pedDao = new PedidoDAO();

        String titulo = "Detalle de Reporte";
        
        switch(tipo) {
            case "pedidos":
                titulo = "Historial Maestro de Órdenes";
                request.setAttribute("lista", pedDao.listarTodos());
                break;
            case "inventario":
                titulo = "Valorización Total de Inventario";
                request.setAttribute("lista", pDao.listarTodos());
                break;
            case "alertas":
                titulo = "Productos con Stock Crítico";
                request.setAttribute("lista", pDao.listarTodos().stream().filter(p -> p.getStock() < 5).collect(Collectors.toList()));
                break;
            case "clientes":
                titulo = "Directorio de Clientes Activos";
                request.setAttribute("lista", uDao.obtenerTodosUsuarios().stream().filter(u -> "cliente".equalsIgnoreCase(u.getUserType())).collect(Collectors.toList()));
                break;
            case "catalogo":
                titulo = "Catálogo Maestro de Productos";
                request.setAttribute("lista", pDao.listarTodos());
                break;
            default:
                response.sendRedirect("reporte-ventas");
                return;
        }

        request.setAttribute("titulo", titulo);
        request.setAttribute("tipo", tipo);
        request.getRequestDispatcher("detalle-reporte.jsp").forward(request, response);
    }
}