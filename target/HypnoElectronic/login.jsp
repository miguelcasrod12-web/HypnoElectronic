<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <%@ include file="includes/navbar.jsp" %>

    <div class="container">
        <div class="row justify-content-center mt-5">
            <div class="col-md-4">
                
                <% 
                    String message = request.getParameter("message");
                    if ("logout_success".equals(message)) {
                %>
                    <div class="alert alert-info bg-dark text-info border-info text-center mb-4">
                        ✅ Sesión cerrada correctamente
                    </div>
                <% } %>

                <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid #333; border-radius: 15px;">
                    <h2 class="text-center mb-4" style="color: var(--neon-blue);">Iniciar Sesión</h2>
                    
                    <% if(request.getAttribute("errorMessage") != null) { %>
                        <div class="alert alert-danger bg-dark text-danger border-danger small text-center">
                            <%= request.getAttribute("errorMessage") %>
                        </div>
                    <% } %>
                    
                    <form action="login" method="POST">
                        <div class="mb-3 text-start">
                            <label for="username" class="form-label text-secondary small">Usuario</label>
                            <input type="text" id="username" name="username" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        
                        <div class="mb-3 text-start">
                            <label for="password" class="form-label text-secondary small">Contraseña</label>
                            <input type="password" id="password" name="password" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        
                        <button type="submit" class="btn btn-neon w-100 fw-bold mt-3">INGRESAR AL SISTEMA</button>
                    </form>
                    
                    <p class="mt-4 text-center">
                        <a href="register.jsp" class="text-secondary text-decoration-none small">
                            ¿No tienes cuenta? <span style="color: var(--neon-blue);">Regístrate aquí</span>
                        </a>
                    </p>
                </div>

                <div class="text-center mt-4">
                    <a href="home" class="text-muted small text-decoration-none">← Volver al catálogo</a>
                </div>

            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>