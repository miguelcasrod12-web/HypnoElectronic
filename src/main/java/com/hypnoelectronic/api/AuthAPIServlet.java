package com.hypnoelectronic.api;

import java.io.BufferedReader;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * API REST para autenticación de usuarios
 * Endpoint: /api/auth/*
 * Autor: Miguel Castillo - Evidencia GA7-220501096-AA5-EV03
 */
@WebServlet("/api/auth/*")
public class AuthAPIServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Gson gson = new Gson();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Maneja todas las solicitudes GET, POST, PUT, DELETE
     */
    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Configurar respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        // Configurar CORS (para permitir frontend React)
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        String pathInfo = request.getPathInfo();
        String method = request.getMethod();
        
        PrintWriter out = response.getWriter();
        ApiResponse<?> apiResponse;
        
        try {
            // Enrutamiento basado en path y método
            if ("/login".equals(pathInfo) && "POST".equals(method)) {
                apiResponse = handleLogin(request);
            } else if ("/register".equals(pathInfo) && "POST".equals(method)) {
                apiResponse = handleRegister(request);
            } else if ("/logout".equals(pathInfo) && "POST".equals(method)) {
                apiResponse = handleLogout(request);
            } else if ("/check".equals(pathInfo) && "GET".equals(method)) {
                apiResponse = handleCheckAuth(request);
            } else if ("OPTIONS".equals(method)) {
                // Manejar preflight CORS
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            } else {
                apiResponse = ApiResponse.error("Endpoint no encontrado: " + pathInfo, 404);
            }
            
            // Establecer código de estado HTTP
            response.setStatus(apiResponse.getStatusCode());
            
            // Convertir respuesta a JSON y enviar
            String jsonResponse = gson.toJson(apiResponse);
            out.print(jsonResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
            apiResponse = ApiResponse.error("Error interno del servidor: " + e.getMessage(), 500);
            response.setStatus(500);
            out.print(gson.toJson(apiResponse));
        } finally {
            out.flush();
        }
    }

    /**
     * POST /api/auth/login
     * Autentica un usuario y crea sesión
     */

    private ApiResponse<Map<String, Object>> handleLogin(HttpServletRequest request) throws IOException {        
    
        System.out.println("🚀 ENTRÓ A LOGIN");
        
        // Leer JSON del body
        BufferedReader reader = request.getReader();
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

        String username = json.has("username") ? json.get("username").getAsString() : null;
        String password = json.has("password") ? json.get("password").getAsString() : null;
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            return ApiResponse.error("Username y password son requeridos", 400);
        }
        
        try {
            Usuario usuario = usuarioDAO.validarLogin(username, password);
            
            if (usuario != null) {
                // Crear sesión
                HttpSession session = request.getSession(true);
                session.setAttribute("usuario", usuario);
                session.setAttribute("userId", usuario.getId());
                session.setAttribute("userType", usuario.getUserType());
                
                // Preparar datos de respuesta
                Map<String, Object> userData = new HashMap<>();
                userData.put("id", usuario.getId());
                userData.put("username", usuario.getUsername());
                userData.put("fullName", usuario.getFullName());
                userData.put("email", usuario.getEmail());
                userData.put("userType", usuario.getUserType());
                userData.put("sessionId", session.getId());
                
                System.out.println("✅ LOGIN: username=" + username + ", password=" + password);

                return ApiResponse.success("Login exitoso", userData);
            } else {
                return ApiResponse.error("Credenciales incorrectas", 401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error en autenticación: " + e.getMessage(), 500);
        }
    }

    /**
     * POST /api/auth/register
     * Registra un nuevo usuario
     */
    private ApiResponse<Map<String, Object>> handleRegister(HttpServletRequest request) throws IOException {
        // Leer el body completo
        BufferedReader reader = request.getReader();
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

        String username = json.has("username") ? json.get("username").getAsString() : null;
        String password = json.has("password") ? json.get("password").getAsString() : null;
        String fullName = json.has("fullName") ? json.get("fullName").getAsString() : null;
        String email = json.has("email") ? json.get("email").getAsString() : null;
        String userType = json.has("userType") ? json.get("userType").getAsString() : "patient";
        
        // Validaciones básicas
        if (username == null || username.trim().isEmpty()) {
            return ApiResponse.error("Username es requerido", 400);
        }
        if (password == null || password.trim().isEmpty()) {
            return ApiResponse.error("Password es requerido", 400);
        }
        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.error("Email es requerido", 400);
        }
        
        // Verificar si usuario ya existe
        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkSql = "SELECT COUNT(*) FROM usuarios WHERE username = ? OR email = ?";
            PreparedStatement pstmt = conn.prepareStatement(checkSql);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return ApiResponse.error("El username o email ya están registrados", 409);
            }
        } catch (Exception e) {
            return ApiResponse.error("Error verificando usuario: " + e.getMessage(), 500);
        }
        
        // Crear nuevo usuario
        try {
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(password);
            nuevoUsuario.setFullName(fullName != null ? fullName : "");
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setUserType(userType != null ? userType : "patient");
            
            boolean creado = usuarioDAO.insertarUsuario(nuevoUsuario);
            
            if (creado) {
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("username", username);
                responseData.put("email", email);
                responseData.put("message", "Usuario registrado exitosamente");
                
                return ApiResponse.success(responseData);
            } else {
                return ApiResponse.error("No se pudo registrar el usuario", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("Error en registro: " + e.getMessage(), 500);
        }
    }

    /**
     * POST /api/auth/logout
     * Cierra la sesión del usuario
     */
    private ApiResponse<String> handleLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ApiResponse.success("Sesión cerrada exitosamente", null);
    }

    /**
     * GET /api/auth/check
     * Verifica si hay una sesión activa
     */
    private ApiResponse<Map<String, Object>> handleCheckAuth(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        
        if (session != null && session.getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            
            Map<String, Object> userData = new HashMap<>();
            userData.put("authenticated", true);
            userData.put("id", usuario.getId());
            userData.put("username", usuario.getUsername());
            userData.put("fullName", usuario.getFullName());
            userData.put("userType", usuario.getUserType());
            userData.put("sessionId", session.getId());
            
            return ApiResponse.success(userData);
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("authenticated", false);
            return ApiResponse.success(data);
        }
    }
}