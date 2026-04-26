<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <%@ include file="includes/navbar.jsp" %>

    <div class="container">
        <div class="row justify-content-center mt-4">
            <div class="col-md-5">
                
                <%
                    String errorMessage = (String) request.getAttribute("errorMessage");
                    String success = request.getParameter("success");
                    
                    if (errorMessage != null) {
                %>
                    <div class="alert alert-danger bg-dark text-danger border-danger text-center">
                        <strong>Error:</strong> <%= errorMessage %>
                    </div>
                <%
                    }
                    
                    if ("true".equals(success)) {
                %>
                    <div class="alert alert-success bg-dark text-success border-success text-center">
                        <strong>¡Éxito!</strong> Usuario registrado correctamente. 
                        <br><a href="login.jsp" class="text-success fw-bold">Iniciar sesión ahora</a>
                    </div>
                <%
                    }
                %>
                
                <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid #333; border-radius: 15px;">
                    <h2 class="text-center mb-4" style="color: var(--neon-blue);">Crear Cuenta</h2>
                    
                    <form action="register" method="post">
                        
                        <input type="hidden" name="userType" value="cliente"> <%-- Siempre registra como cliente --%>

                        <div class="mb-3">
                            <label class="form-label text-secondary small">Nombre completo</label>
                            <input type="text" name="fullName" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label text-secondary small">Correo electrónico</label>
                            <input type="email" name="email" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label text-secondary small">Nombre de usuario</label>
                            <input type="text" name="username" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label text-secondary small">Contraseña</label>
                            <input type="password" name="password" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        
                        <button type="submit" class="btn btn-neon w-100 fw-bold mt-3">REGISTRARSE</button>
                    </form>
                    
                    <p class="mt-4 text-center">
                        <a href="login.jsp" class="text-secondary text-decoration-none small">
                            ¿Ya tienes cuenta? <span style="color: var(--neon-blue);">Inicia sesión</span>
                        </a>
                    </p>
                </div>
            </div>  
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>