<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de Categorías - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    <div class="container mt-4">
        <div class="d-flex justify-content-between mb-4">
            <h1>Categorías de Productos</h1>
            <button class="btn btn-neon fw-bold" data-bs-toggle="modal" data-bs-target="#modalCat">+ NUEVA CATEGORÍA</button>
        </div>
        
        <div class="card bg-black border-secondary p-3">
            <table class="table table-dark table-hover">
                <thead><tr><th>Nombre</th><th>Descripción</th><th class="text-center">Acciones</th></tr></thead>
                <tbody>
                    <c:forEach var="c" items="${categorias}">
                        <tr>
                            <td class="text-info fw-bold">${c.nombre}</td>
                            <td class="text-secondary small">${c.descripcion}</td>
                            <td class="text-center">
                                <a href="admin-categorias?accion=editar&id=${c.id}" class="btn btn-sm btn-outline-info me-1">✏️ Editar</a>
                                <a href="admin-categorias?accion=eliminar&id=${c.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Eliminar categoría?')">🗑️</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="modalCat" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-white border-info">
                <form action="admin-categorias" method="post">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title">Registrar Categoría</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Nombre</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Descripción</label>
                            <textarea name="descripcion" class="form-control bg-dark text-white border-secondary"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="submit" class="btn btn-neon">GUARDAR</button>
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