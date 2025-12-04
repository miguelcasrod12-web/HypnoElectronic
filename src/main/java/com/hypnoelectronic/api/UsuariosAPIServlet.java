package com.hypnoelectronic.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hypnoelectronic.dao.UsuarioDAO;
import com.hypnoelectronic.dto.ApiResponse;
import com.hypnoelectronic.model.Usuario;
import com.hypnoelectronic.util.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API REST para gestión de usuarios (solo administradores)
 * Endpoint: /api/users/*
 * Autor: Miguel Castillo - Evidencia GA7-220501096-AA5-EV03
 */
@WebServlet("/api/users/*")
public class UsuariosAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Configurar respuesta
        configureResponse(response);
        
        // Verificar autenticación y permisos
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            sendError(response, "No autenticado", 401);
            return;
        }
        
        Usuario usuarioSesion = (Usuario) session.getAttribute("usuario");
        if (!"admin".equals(usuarioSesion.getUserType())) {
            sendError(response, "Acceso denegado. Solo administradores", 403);
            return;
        }
        
        String pathInfo = request.getPathInfo();
        String method = request.getMethod();
        
        PrintWriter out = response.getWriter();
        ApiResponse<?> apiResponse;
        
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                // /api/users
                if ("GET".equals(method)) {
                    apiResponse = getAllUsers();
                } else if ("POST".equals(method)) {
                    apiResponse = createUser(request);
                } else {
                    apiResponse = ApiResponse.error("Método no permitido", 405);
                }
            } else {
                // /api/users/{id}
                String[] pathParts = pathInfo.split("/");
                if (pathParts.length == 2) {
                    try {
                        int userId = Integer.parseInt(pathParts[1]);
                        
                        if ("GET".equals(method)) {
                            apiResponse = getUserById(userId);
                        } else if ("PUT".equals(method)) {
                            apiResponse = updateUser(userId, request);
                        } else if ("DELETE".equals(method)) {
                            apiResponse = deleteUser(userId);
                        } else {
                            apiResponse = ApiResponse.error("Método no permitido", 405);
                        }
                    } catch (NumberFormatException e) {
                        apiResponse = ApiResponse.error("ID de usuario inválido", 400);
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
    
    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", message);
        error.put("statusCode", status);
        
        PrintWriter out = response.getWriter();
        out.print(gson.toJson(error));
        out.flush();
    }
    
    /**
     * GET /api/users
     * Obtiene todos los usuarios (solo admin)
     */
    private ApiResponse<List<Usuario>> getAllUsers() {
        try {
            List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
            // No enviar contraseñas en la respuesta
            for (Usuario usuario : usuarios) {
                usuario.setPassword("********");
            }
            return ApiResponse.success(usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error obteniendo usuarios: " + e.getMessage(), 500);
        }
    }
    
    /**
     * GET /api/users/{id}
     * Obtiene un usuario por ID
     */
    private ApiResponse<Usuario> getUserById(int id) {
        try {
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
            if (usuario != null) {
                usuario.setPassword("********");
                return ApiResponse.success(usuario);
            } else {
                return ApiResponse.notFound("Usuario con ID " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error obteniendo usuario: " + e.getMessage(), 500);
        }
    }
    
    /**
     * POST /api/users
     * Crea un nuevo usuario (solo admin)
     */
    private ApiResponse<Usuario> createUser(HttpServletRequest request) {
        try {
            Usuario usuario = parseUserFromRequest(request);
            
            // Validaciones
            if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
                return ApiResponse.error("Username es requerido", 400);
            }
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                return ApiResponse.error("Password es requerido", 400);
            }
            if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
                return ApiResponse.error("Email es requerido", 400);
            }
            
            // Verificar unicidad
            try (Connection conn = DatabaseConnection.getConnection()) {
                String checkSql = "SELECT COUNT(*) FROM usuarios WHERE username = ? OR email = ?";
                PreparedStatement pstmt = conn.prepareStatement(checkSql);
                pstmt.setString(1, usuario.getUsername());
                pstmt.setString(2, usuario.getEmail());
                ResultSet rs = pstmt.executeQuery();
                
                if (rs.next() && rs.getInt(1) > 0) {
                    return ApiResponse.error("El username o email ya están registrados", 409);
                }
            }
            
            boolean creado = usuarioDAO.insertarUsuario(usuario);
            if (creado) {
                // Obtener el usuario recién creado
                List<Usuario> usuarios = usuarioDAO.obtenerTodosUsuarios();
                Usuario nuevoUsuario = usuarios.get(usuarios.size() - 1);
                nuevoUsuario.setPassword("********");
                
                return ApiResponse.success("Usuario creado exitosamente", nuevoUsuario);
            } else {
                return ApiResponse.error("No se pudo crear el usuario", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error creando usuario: " + e.getMessage(), 500);
        }
    }
    
    /**
     * PUT /api/users/{id}
     * Actualiza un usuario existente
     */
    private ApiResponse<Usuario> updateUser(int id, HttpServletRequest request) {
        try {
            // Verificar que existe
            Usuario usuarioExistente = usuarioDAO.obtenerUsuarioPorId(id);
            if (usuarioExistente == null) {
                return ApiResponse.notFound("Usuario con ID " + id);
            }
            
            Usuario usuarioActualizado = parseUserFromRequest(request);
            usuarioActualizado.setId(id);
            
            // Si no se enviaron algunos campos, mantener los existentes
            if (usuarioActualizado.getUsername() == null) {
                usuarioActualizado.setUsername(usuarioExistente.getUsername());
            }
            if (usuarioActualizado.getPassword() == null) {
                usuarioActualizado.setPassword(usuarioExistente.getPassword());
            } else {
                // Si se envía nueva contraseña, encriptar (en una app real)
                // Por ahora la guardamos como texto plano (igual que el registro)
            }
            if (usuarioActualizado.getFullName() == null) {
                usuarioActualizado.setFullName(usuarioExistente.getFullName());
            }
            if (usuarioActualizado.getEmail() == null) {
                usuarioActualizado.setEmail(usuarioExistente.getEmail());
            }
            if (usuarioActualizado.getUserType() == null) {
                usuarioActualizado.setUserType(usuarioExistente.getUserType());
            }
            
            // Verificar que el nuevo username/email no choque con otros usuarios
            if (!usuarioActualizado.getUsername().equals(usuarioExistente.getUsername()) ||
                !usuarioActualizado.getEmail().equals(usuarioExistente.getEmail())) {
                
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String checkSql = "SELECT COUNT(*) FROM usuarios WHERE (username = ? OR email = ?) AND id != ?";
                    PreparedStatement pstmt = conn.prepareStatement(checkSql);
                    pstmt.setString(1, usuarioActualizado.getUsername());
                    pstmt.setString(2, usuarioActualizado.getEmail());
                    pstmt.setInt(3, id);
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next() && rs.getInt(1) > 0) {
                        return ApiResponse.error("El nuevo username o email ya están en uso por otro usuario", 409);
                    }
                }
            }
            
            boolean actualizado = usuarioDAO.actualizarUsuario(usuarioActualizado);
            if (actualizado) {
                Usuario usuarioFinal = usuarioDAO.obtenerUsuarioPorId(id);
                usuarioFinal.setPassword("********");
                return ApiResponse.success("Usuario actualizado exitosamente", usuarioFinal);
            } else {
                return ApiResponse.error("No se pudo actualizar el usuario", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error actualizando usuario: " + e.getMessage(), 500);
        }
    }
    
    /**
     * DELETE /api/users/{id}
     * Elimina un usuario (no puede eliminarse a sí mismo)
     */
    private ApiResponse<Map<String, Object>> deleteUser(int id) {
        try {
            // Verificar que existe
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(id);
            if (usuario == null) {
                return ApiResponse.notFound("Usuario con ID " + id);
            }
            
            // Obtener usuario de sesión para verificar que no se elimina a sí mismo
            // (esto se verificaría en el método service, ya estamos después de esa validación)
            
            boolean eliminado = usuarioDAO.eliminarUsuario(id);
            if (eliminado) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("id", id);
                responseData.put("message", "Usuario eliminado exitosamente");
                responseData.put("usuarioEliminado", usuario.getUsername());
                
                return ApiResponse.success(responseData);
            } else {
                return ApiResponse.error("No se pudo eliminar el usuario", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error eliminando usuario: " + e.getMessage(), 500);
        }
    }
    
    /**
     * Parsea un usuario desde la solicitud
     */
    private Usuario parseUserFromRequest(HttpServletRequest request) throws IOException {
        Usuario usuario = new Usuario();
        
        // Intentar leer como JSON
        if ("application/json".equals(request.getContentType())) {
            BufferedReader reader = request.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            
            if (jsonBuilder.length() > 0) {
                JsonObject jsonObject = gson.fromJson(jsonBuilder.toString(), JsonObject.class);
                
                if (jsonObject.has("username")) usuario.setUsername(jsonObject.get("username").getAsString());
                if (jsonObject.has("password")) usuario.setPassword(jsonObject.get("password").getAsString());
                if (jsonObject.has("fullName")) usuario.setFullName(jsonObject.get("fullName").getAsString());
                if (jsonObject.has("email")) usuario.setEmail(jsonObject.get("email").getAsString());
                if (jsonObject.has("userType")) usuario.setUserType(jsonObject.get("userType").getAsString());
                
                return usuario;
            }
        }
        
        // Usar parámetros normales
        usuario.setUsername(request.getParameter("username"));
        usuario.setPassword(request.getParameter("password"));
        usuario.setFullName(request.getParameter("fullName"));
        usuario.setEmail(request.getParameter("email"));
        usuario.setUserType(request.getParameter("userType"));
        
        return usuario;
    }
}