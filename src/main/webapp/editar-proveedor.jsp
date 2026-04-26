<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Editar Proveedor - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid var(--neon-blue);">
                    <h2 class="mb-4" style="color: var(--neon-blue);">Editar Proveedor</h2>
                    <form action="admin-proveedores" method="post">
                        <input type="hidden" name="accion" value="actualizar">
                        <input type="hidden" name="id" value="${proveedor.id}">
                        <div class="mb-3">
                            <label class="form-label text-secondary small">Nombre de la Empresa</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" value="${proveedor.nombre}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary small">NIT</label>
                            <input type="text" name="nit" class="form-control bg-dark text-white border-secondary" value="${proveedor.nit}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary small">Persona de Contacto</label>
                            <input type="text" name="contacto" class="form-control bg-dark text-white border-secondary" value="${proveedor.contacto}">
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary small">Email</label>
                            <input type="email" name="email" class="form-control bg-dark text-white border-secondary" value="${proveedor.email}" required>
                        </div>
                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-neon w-100 fw-bold">GUARDAR CAMBIOS</button>
                            <a href="admin-proveedores" class="btn btn-outline-secondary w-100 fw-bold">CANCELAR</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>