package com.hypnoelectronic.controller;

import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.model.Producto;
import com.hypnoelectronic.model.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/crud-test")
public class CrudTestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Prueba CRUD - Evidencia SENA</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println("table { border-collapse: collapse; width: 100%; margin: 20px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".error { color: red; font-weight: bold; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Prueba de Operaciones CRUD - Evidencia GA7-220501096-AA2</h1>");
        
        // Instanciar DAOs
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        
        // ========== PRUEBAS CON USUARIOS ==========
        out.println("<h2>1. Operaciones CRUD con Usuarios</h2>");
        
        // INSERTAR usuario
        Usuario nuevoUsuario = new Usuario("testuser", "test123", "Usuario de Prueba", "test@email.com", "patient");
        boolean insertado = usuarioDAO.insertarUsuario(nuevoUsuario);
        out.println("<p class='" + (insertado ? "success" : "error") + "'>");
        out.println("✓ INSERTAR usuario: " + (insertado ? "ÉXITO" : "FALLO"));
        out.println("</p>");
        
        // LEER todos los usuarios
        List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
        out.println("<h3>Lista de Usuarios (SELECT):</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Usuario</th><th>Nombre</th><th>Email</th><th>Tipo</th></tr>");
        for (Usuario u : usuarios) {
            out.println("<tr>");
            out.println("<td>" + u.getId() + "</td>");
            out.println("<td>" + u.getUsername() + "</td>");
            out.println("<td>" + u.getFullName() + "</td>");
            out.println("<td>" + u.getEmail() + "</td>");
            out.println("<td>" + u.getUserType() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        
        // ========== PRUEBAS CON PRODUCTOS ==========
        out.println("<h2>2. Operaciones CRUD con Productos</h2>");
        
        // INSERTAR producto
        Producto nuevoProducto = new Producto("Nuevo Producto", "Descripción del nuevo producto", 99.99, 100);
        boolean productoInsertado = productoDAO.insertarProducto(nuevoProducto);
        out.println("<p class='" + (productoInsertado ? "success" : "error") + "'>");
        out.println("✓ INSERTAR producto: " + (productoInsertado ? "ÉXITO" : "FALLO"));
        out.println("</p>");
        
        // LEER todos los productos
        List<Producto> productos = productoDAO.obtenerTodosProductos();
        out.println("<h3>Lista de Productos (SELECT):</h3>");
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Nombre</th><th>Descripción</th><th>Precio</th><th>Stock</th></tr>");
        for (Producto p : productos) {
            out.println("<tr>");
            out.println("<td>" + p.getId() + "</td>");
            out.println("<td>" + p.getNombre() + "</td>");
            out.println("<td>" + p.getDescripcion() + "</td>");
            out.println("<td>$" + p.getPrecio() + "</td>");
            out.println("<td>" + p.getStock() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        
        // UPDATE de un producto (si hay productos)
        if (!productos.isEmpty()) {
            Producto primerProducto = productos.get(0);
            primerProducto.setPrecio(primerProducto.getPrecio() + 10);
            boolean actualizado = productoDAO.actualizarProducto(primerProducto);
            out.println("<p class='" + (actualizado ? "success" : "error") + "'>");
            out.println("✓ UPDATE producto ID " + primerProducto.getId() + ": " + (actualizado ? "ÉXITO" : "FALLO"));
            out.println("</p>");
        }
        
        // DELETE de un producto (si hay más de 1)
        if (productos.size() > 1) {
            Producto ultimoProducto = productos.get(productos.size() - 1);
            boolean eliminado = productoDAO.eliminarProducto(ultimoProducto.getId());
            out.println("<p class='" + (eliminado ? "success" : "error") + "'>");
            out.println("✓ DELETE producto ID " + ultimoProducto.getId() + ": " + (eliminado ? "ÉXITO" : "FALLO"));
            out.println("</p>");
        }
        
        out.println("<hr>");
        out.println("<h3>Resumen de operaciones CRUD implementadas:</h3>");
        out.println("<ul>");
        out.println("<li><strong>CREATE</strong>: INSERT INTO tablas</li>");
        out.println("<li><strong>READ</strong>: SELECT con WHERE y SELECT *</li>");
        out.println("<li><strong>UPDATE</strong>: UPDATE con parámetros</li>");
        out.println("<li><strong>DELETE</strong>: DELETE con condición</li>");
        out.println("</ul>");
        
        out.println("<p><strong>Nota para el instructor:</strong> Esta página demuestra el uso completo de JDBC con operaciones CRUD, requerido para la evidencia GA7-220501096-AA2-EV01.</p>");
        
        out.println("</body>");
        out.println("</html>");
    }
}
