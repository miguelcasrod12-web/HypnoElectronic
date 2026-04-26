<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Control de Accesos - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h1>Gestión de Usuarios</h1>
                <p class="text-secondary">Administración de perfiles y niveles de acceso</p>
            </div>
            <button class="btn btn-neon fw-bold" data-bs-toggle="modal" data-bs-target="#modalUsuario">
                + REGISTRAR COLABORADOR
            </button>
        </div>

        <div class="card bg-dark border-secondary">
            <table class="table table-dark table-hover mb-0">
                <thead>
                    <tr class="text-secondary">
                        <th>Username</th>
                        <th>Nombre Completo</th>
                        <th>Email</th>
                        <th>Rol Actual</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${usuarios}">
                        <tr>
                            <td class="text-info">@${u.username}</td>
                            <td>${u.fullName}</td>
                            <td>${u.email}</td>
                            <td>
                                <span class="badge ${u.userType == 'admin' ? 'bg-warning text-dark' : 'bg-secondary'}">
                                    ${u.userType.toUpperCase()}
                                </span>
                            </td>
                            <td class="text-center">
                                <a href="cambiar-rol?id=${u.id}" class="btn btn-sm btn-outline-warning">🔄 Rol</a>
                                <a href="editar-usuario-form?id=${u.id}" class="btn btn-sm btn-outline-info">✏️</a>
                                <a href="eliminar-usuario?id=${u.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Revocar acceso?')">🗑️</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="modalUsuario" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark border-info">
                <form action="guardar-usuario" method="post">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title">Nuevo Registro de Usuario</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label text-secondary">Nombre Completo</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary">Email</label>
                            <input type="email" name="email" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary">Username</label>
                            <input type="text" name="username" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary">Password</label>
                            <input type="password" name="password" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary">Rol Inicial</label>
                            <select name="role" class="form-select bg-dark text-white border-secondary">
                                <option value="cliente">Cliente</option>
                                <option value="admin">Administrador</option>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="submit" class="btn btn-neon w-100">DAR DE ALTA EN SISTEMA</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

<div class="container mt-4">
    <a href="dashboard.jsp" class="btn btn-secondary btn-sm">Volver al Panel</a>
</div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>