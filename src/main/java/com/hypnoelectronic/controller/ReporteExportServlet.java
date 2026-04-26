package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.model.Usuario;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ReporteExportServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo");
        
        // 1. Configurar Headers para evitar "archivos dañados"
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_" + tipo + "_" + System.currentTimeMillis() + ".csv");
        
        PrintWriter writer = response.getWriter();
        
        // 2. ESCRIBIR EL BOM (Byte Order Mark) - VITAL PARA EXCEL UTF-8
        writer.write('\ufeff');

        if ("clientes".equalsIgnoreCase(tipo)) {
            writer.println("Nombre Completo,Username,Email,Rol,Fecha Registro");
            List<Usuario> lista = new UsuarioDAO().obtenerTodosUsuarios();
            for (Usuario u : lista) {
                if("cliente".equalsIgnoreCase(u.getUserType())) {
                    writer.println(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", 
                        u.getFullName(), u.getUsername(), u.getEmail(), u.getUserType(), u.getCreatedAt()));
                }
            }
        } else {
            writer.println("Producto,Categoría,Precio Unitario,Stock Actual,Valorización Stock");
            List<Producto> lista = new ProductoDAO().listarTodos();
            
            for (Producto p : lista) {
                boolean incluir = true;
                if ("alertas".equals(tipo) && p.getStock() >= 5) incluir = false;
                
                if (incluir) {
                    writer.println(String.format("\"%s\",\"%s\",%.2f,%d,%.2f", 
                        p.getNombre(), p.getCategoriaNombre(), p.getPrecio(), p.getStock(), (p.getPrecio() * p.getStock())));
                }
            }
        }
        
        writer.flush();
        writer.close();
    }
}