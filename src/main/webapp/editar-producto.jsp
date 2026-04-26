<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Producto - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid var(--neon-blue);">
                    <h2 class="mb-4" style="color: var(--neon-blue);">Editar Periférico</h2>
                    
                    <form action="editar-producto" method="post">
                        <input type="hidden" name="id" value="${producto.id}">

                        <div class="mb-3">
                            <label class="form-label text-secondary">Nombre del Producto</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" value="${producto.nombre}" required>
                        </div>

                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label text-secondary">Precio (COP)</label>
                                <input type="number" name="precio" class="form-control bg-dark text-white border-secondary" value="${producto.precio}" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label text-secondary">Stock Actual</label>
                                <input type="number" name="stock" class="form-control bg-dark text-white border-secondary" value="${producto.stock}" required>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-secondary">Categoría</label>
                            <select name="categoriaId" class="form-select bg-dark text-white border-secondary" required>
                                <c:forEach var="cat" items="${categorias}">
                                    <option value="${cat.id}" ${producto.categoriaId == cat.id ? 'selected' : ''}>${cat.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label text-secondary">Proveedor</label>
                            <select name="proveedorId" class="form-select bg-dark text-white border-secondary" required>
                                <c:forEach var="prov" items="${proveedores}">
                                    <option value="${prov.id}" ${producto.proveedorId == prov.id ? 'selected' : ''}>${prov.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-secondary">URL de Imagen</label>
                            <input type="text" name="imagen_url" class="form-control bg-dark text-white border-secondary" value="${producto.imagen_url}">
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-secondary">Descripción</label>
                            <textarea name="descripcion" class="form-control bg-dark text-white border-secondary" rows="3">${producto.descripcion}</textarea>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-neon w-100 fw-bold">ACTUALIZAR DATOS</button>
                            <a href="admin-inventario" class="btn btn-outline-secondary w-100 fw-bold">CANCELAR</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>