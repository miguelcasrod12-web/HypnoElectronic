<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hypnoelectronic.model.Usuario" %>
<%
    Usuario userSession = (Usuario) session.getAttribute("user");
    if (userSession == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<% if (request.getParameter("success") != null) { %>
    <div class="alert alert-success bg-dark text-success border-success py-2 small">
        ✓ Cambios guardados correctamente.
    </div>
<% } %>
<% if (request.getParameter("error") != null) { %>
    <div class="alert alert-danger bg-dark text-danger border-danger py-2 small">
        X Error al actualizar los datos.
    </div>
<% } %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Configuración de Cuenta - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row">
            <div class="col-md-4">
                <div class="list-group shadow-sm">
                    <a href="#" class="list-group-item list-group-item-action active bg-dark border-info">Mi Perfil</a>
                    <a href="mis-compras" class="list-group-item list-group-item-action bg-dark text-white border-secondary">Mis Compras</a>
                    <% if ("admin".equals(userSession.getUserType())) { %>
                        <a href="dashboard.jsp" class="list-group-item list-group-item-action bg-dark text-warning border-secondary">Volver al Panel</a>
                    <% } %>
                </div>
            </div>

            <div class="col-md-8">
                <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <h2 class="mb-4" style="color: var(--neon-blue);">Configuración de la Cuenta</h2>
                    
                    <form action="update-profile" method="post">
                        <div class="mb-3">
                            <label class="text-secondary small">Nombre Completo</label>
                            <input type="text" name="fullName" class="form-control bg-dark text-white border-secondary" value="<%= userSession.getFullName() %>">
                        </div>
                        <div class="mb-3">
                            <label class="text-secondary small">Correo Electrónico</label>
                            <input type="email" name="email" class="form-control bg-dark text-white border-secondary" value="<%= userSession.getEmail() %>">
                        </div>
                        <div class="mb-3">
                            <label class="text-secondary small">Nombre de Usuario</label>
                            <input type="text" class="form-control bg-dark text-white border-secondary" value="<%= userSession.getUsername() %>" disabled>
                            <small class="text-muted">El nombre de usuario no se puede cambiar.</small>
                        </div>
                        
                        <hr class="border-secondary my-4">
                        <h5 class="text-white">Cambiar Contraseña</h5>
                        <div class="mb-3">
                            <input type="password" name="newPassword" class="form-control bg-dark text-white border-secondary" placeholder="Nueva contraseña (dejar en blanco para no cambiar)">
                        </div>
                        
                        <button type="submit" class="btn btn-neon w-100 fw-bold mt-3">GUARDAR CAMBIOS</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>