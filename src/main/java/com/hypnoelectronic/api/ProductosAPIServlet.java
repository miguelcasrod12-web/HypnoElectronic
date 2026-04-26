package com.hypnoelectronic.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypnoelectronic.dao.ProductoDAO;
import com.hypnoelectronic.dto.ApiResponse;
import com.hypnoelectronic.model.Producto;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * API REST para gestión de productos
 * Endpoint: /api/products/*
 * Autor: Miguel Castillo - Evidencia GA7-220501096-AA5-EV03
 */
/*@WebServlet("/api/products/*")*/
public class ProductosAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();
    private ProductoDAO productoDAO = new ProductoDAO();

    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Configurar respuesta JSON y CORS
        configureResponse(response);
        
        String pathInfo = request.getPathInfo();
        String method = request.getMethod();
        
        PrintWriter out = response.getWriter();
        ApiResponse<?> apiResponse;
        
        try {
            // Enrutamiento
            if (pathInfo == null || "/".equals(pathInfo)) {
                // /api/products
                if ("GET".equals(method)) {
                    apiResponse = getAllProducts();
                } else if ("POST".equals(method)) {
                    apiResponse = createProduct(request);
                } else {
                    apiResponse = ApiResponse.error("Método no permitido", 405);
                }
            } else {
                // /api/products/{id}
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    try {
                        int productId = Integer.parseInt(pathParts[1]);
                        
                        if ("GET".equals(method)) {
                            apiResponse = getProductById(productId);
                        } else if ("PUT".equals(method)) {
                            apiResponse = updateProduct(productId, request);
                        } else if ("DELETE".equals(method)) {
                            apiResponse = deleteProduct(productId);
                        } else {
                            apiResponse = ApiResponse.error("Método no permitido", 405);
                        }
                    } catch (NumberFormatException e) {
                        apiResponse = ApiResponse.error("ID de producto inválido", 400);
                    }
                } else {
                    apiResponse = ApiResponse.error("Endpoint no encontrado", 404);
                }
            }
            
            response.setStatus(apiResponse.getStatusCode());
            out.print(gson.toJson(apiResponse));
            
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = ApiResponse.error("Error interno: " + e.getMessage(), 500);
            response.setStatus(500);
            out.print(gson.toJson(apiResponse));
        } finally {
            out.flush();
        }
    }
    
    private void configureResponse(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }
    
    /**
     * GET /api/products
     * Obtiene todos los productos
     */
    private ApiResponse<List<Producto>> getAllProducts() {
        try {
            List<Producto> productos = productoDAO.obtenerTodosProductos();
            if (productos.isEmpty()) {
                return ApiResponse.success("No hay productos registrados", productos);
            }
            return ApiResponse.success(productos);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error obteniendo productos: " + e.getMessage(), 500);
        }
    }
    
    /**
     * GET /api/products/{id}
     * Obtiene un producto por ID
     */
    private ApiResponse<Producto> getProductById(int id) {
        try {
            Producto producto = productoDAO.obtenerProductoPorId(id);
            if (producto != null) {
                return ApiResponse.success(producto);
            } else {
                return ApiResponse.notFound("Producto con ID " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error obteniendo producto: " + e.getMessage(), 500);
        }
    }
    
    /**
     * POST /api/products
     * Crea un nuevo producto
     */
    private ApiResponse<Producto> createProduct(HttpServletRequest request) {
        try {
            // Leer cuerpo JSON si existe, si no usar parámetros
            Producto producto = parseProductFromRequest(request);
            
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                return ApiResponse.error("El nombre del producto es requerido", 400);
            }
            if (producto.getPrecio() <= 0) {
                return ApiResponse.error("El precio debe ser mayor a 0", 400);
            }
            
            boolean creado = productoDAO.insertarProducto(producto);
            if (creado) {
                // Obtener el ID generado (asumiendo que el último insertado es el nuevo)
                List<Producto> productos = productoDAO.obtenerTodosProductos();
                Producto nuevoProducto = productos.get(productos.size() - 1);
                return ApiResponse.success("Producto creado exitosamente", nuevoProducto);
            } else {
                return ApiResponse.error("No se pudo crear el producto", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error creando producto: " + e.getMessage(), 500);
        }
    }
    
    /**
     * PUT /api/products/{id}
     * Actualiza un producto existente
     */
    private ApiResponse<Producto> updateProduct(int id, HttpServletRequest request) {
        try {
            // Verificar que el producto existe
            Producto productoExistente = productoDAO.obtenerProductoPorId(id);
            if (productoExistente == null) {
                return ApiResponse.notFound("Producto con ID " + id);
            }
            
            // Obtener datos actualizados
            Producto productoActualizado = parseProductFromRequest(request);
            
            // Mantener ID original
            productoActualizado.setId(id);
            
            // Si no se enviaron algunos campos, mantener los existentes
            if (productoActualizado.getNombre() == null) {
                productoActualizado.setNombre(productoExistente.getNombre());
            }
            if (productoActualizado.getDescripcion() == null) {
                productoActualizado.setDescripcion(productoExistente.getDescripcion());
            }
            if (productoActualizado.getPrecio() == 0) {
                productoActualizado.setPrecio(productoExistente.getPrecio());
            }
            if (productoActualizado.getStock() == 0) {
                productoActualizado.setStock(productoExistente.getStock());
            }
            // Asegurarse de que los IDs de categoría y proveedor se mantengan si no se actualizan
            if (productoActualizado.getCategoriaId() == 0) {
                productoActualizado.setCategoriaId(productoExistente.getCategoriaId());
                productoActualizado.setProveedorId(productoExistente.getProveedorId());
            }
            
            boolean actualizado = productoDAO.actualizarProducto(productoActualizado);
            if (actualizado) {
                Producto productoFinal = productoDAO.obtenerProductoPorId(id);
                return ApiResponse.success("Producto actualizado exitosamente", productoFinal);
            } else {
                return ApiResponse.error("No se pudo actualizar el producto", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error actualizando producto: " + e.getMessage(), 500);
        }
    }
    
    /**
     * DELETE /api/products/{id}
     * Elimina un producto
     */
    private ApiResponse<Map<String, Object>> deleteProduct(int id) {
        try {
            // Verificar que existe
            Producto producto = productoDAO.obtenerProductoPorId(id);
            if (producto == null) {
                return ApiResponse.notFound("Producto con ID " + id);
            }
            
            boolean eliminado = productoDAO.eliminarProducto(id);
            if (eliminado) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", id);
                responseData.put("message", "Producto eliminado exitosamente");
                responseData.put("productoEliminado", producto.getNombre());
                
                return ApiResponse.success(responseData);
            } else {
                return ApiResponse.error("No se pudo eliminar el producto", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error eliminando producto: " + e.getMessage(), 500);
        }
    }
    
    /**
     * Parsea un producto desde la solicitud (JSON o form-data)
     */
    private Producto parseProductFromRequest(HttpServletRequest request) throws IOException {
        Producto producto = new Producto();
        
        // Intentar leer como JSON primero
        if ("application/json".equals(request.getContentType())) {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            
            if (jsonBuilder.length() > 0) {
                JsonObject jsonObject = gson.fromJson(jsonBuilder.toString(), JsonObject.class);
                
                if (jsonObject.has("nombre")) producto.setNombre(jsonObject.get("nombre").getAsString());
                if (jsonObject.has("descripcion")) producto.setDescripcion(jsonObject.get("descripcion").getAsString());
                if (jsonObject.has("precio")) producto.setPrecio(jsonObject.get("precio").getAsDouble());
                if (jsonObject.has("stock")) producto.setStock(jsonObject.get("stock").getAsInt());
                if (jsonObject.has("categoriaId")) producto.setCategoriaId(jsonObject.get("categoriaId").getAsInt());
                if (jsonObject.has("proveedorId")) producto.setProveedorId(jsonObject.get("proveedorId").getAsInt());
                
                return producto;
            }
        }
        
        // Si no es JSON, usar parámetros normales
        producto.setNombre(request.getParameter("nombre"));
        producto.setDescripcion(request.getParameter("descripcion"));
        
        String precioStr = request.getParameter("precio");
        if (precioStr != null && !precioStr.trim().isEmpty()) {
            producto.setPrecio(Double.parseDouble(precioStr));
        }
        
        String stockStr = request.getParameter("stock");
        if (stockStr != null && !stockStr.trim().isEmpty()) {
            producto.setStock(Integer.parseInt(stockStr));
        }
        
        String catId = request.getParameter("categoriaId");
        if (catId != null) producto.setCategoriaId(Integer.parseInt(catId));
        
        String provId = request.getParameter("proveedorId");
        if (provId != null) producto.setProveedorId(Integer.parseInt(provId));
        
        return producto;
    }
}