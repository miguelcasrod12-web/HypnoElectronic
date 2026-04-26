<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de Proveedores - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    <div class="container mt-4">
        <div class="d-flex justify-content-between mb-4">
            <h1>Aliados de Suministros</h1>
            <button class="btn btn-neon fw-bold" data-bs-toggle="modal" data-bs-target="#modalProv">+ NUEVO PROVEEDOR</button>
        </div>
        
        <div class="card bg-black border-secondary p-3">
            <table class="table table-dark table-hover align-middle">
                <thead>
                    <tr class="text-secondary">
                        <th>Empresa</th>
                        <th>NIT</th>
                        <th>Contacto</th>
                        <th>Email</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${proveedores}">
                        <tr>
                            <td class="text-info fw-bold">${p.nombre}</td>
                            <td>${p.nit}</td>
                            <td>${p.contacto}</td>
                            <td class="text-secondary small">${p.email}</td>
                            <td class="text-center">
                                <a href="admin-proveedores?accion=editar&id=${p.id}" class="btn btn-sm btn-outline-info me-1">✏️ Editar</a>
                                <a href="admin-proveedores?accion=eliminar&id=${p.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Eliminar proveedor?')">🗑️</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <div class="modal fade" id="modalProv" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-white border-info">
                <form action="admin-proveedores" method="post">
                    <div class="modal-header border-secondary">
                        <h5 class="modal-title">Registrar Proveedor</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Nombre de la Empresa</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">NIT</label>
                            <input type="text" name="nit" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Persona de Contacto</label>
                            <input type="text" name="contacto" class="form-control bg-dark text-white border-secondary">
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Email</label>
                            <input type="email" name="email" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="submit" class="btn btn-neon w-100">GUARDAR PROVEEDOR</button>
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