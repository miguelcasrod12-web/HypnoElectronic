<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%@ page import="com.hypnoelectronic.dao.CategoriaDAO" %>
<%@ page import="com.hypnoelectronic.dao.ProveedorDAO" %>
<%@ page import="com.hypnoelectronic.dao.ProductoDAO" %>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Gestión de Inventario - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-container { background-color: var(--card-bg); border-radius: 15px; padding: 20px; border: 1px solid #333; }
        .img-admin { width: 50px; height: 50px; object-fit: cover; border-radius: 5px; border: 1px solid var(--neon-blue); }
        .stock-critico { color: #ff4444; font-weight: bold; animation: pulse 2s infinite; }
        @keyframes pulse { 0% { opacity: 1; } 50% { opacity: 0.5; } 100% { opacity: 1; } }
    </style>
</head>
<body>

    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h1 class="text-white">Panel de Inventario</h1>
                <p class="text-secondary">Control de existencias y catálogo de productos</p>
            </div>
            <button class="btn btn-neon fw-bold" data-bs-toggle="modal" data-bs-target="#modalProducto">
                + AGREGAR NUEVO PRODUCTO
            </button>
        </div>

        <div class="table-container shadow-lg">
            <table class="table table-dark table-hover align-middle">
                <thead>
                    <tr class="text-secondary border-bottom border-secondary">
                        <th>Imagen</th>
                        <th>Producto</th>
                        <th>Categoría</th>
                        <th>Proveedor</th>
                        <th>Precio (COP)</th>
                        <th class="text-center">Stock</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${productos}">
                        <tr>
                            <td><img src="${p.imagen_url}" class="img-admin"></td>
                            <td>
                                <div class="fw-bold text-white">${p.nombre}</div>
                                <small class="text-muted">ID: ${p.id}</small>
                            </td>
                            <td><span class="badge bg-secondary">${p.categoriaNombre}</span></td>
                            <td><span class="badge bg-info">${p.proveedorNombre}</span></td>
                            <td class="text-neon fw-bold">$${p.precio}</td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${p.stock <= 5}">
                                        <span class="stock-critico">⚠️ ${p.stock}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-success">${p.stock}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <a href="editar-producto?id=${p.id}" class="btn btn-sm btn-outline-info me-1">✏️</a>
                                <a href="eliminar-producto?id=${p.id}" class="btn btn-sm btn-outline-danger" onclick="return confirm('¿Seguro de eliminar este producto?')">🗑️</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    
    <%-- Cargar listas para los selects --%>
    <% request.setAttribute("categorias", new CategoriaDAO().listarTodas()); %>
    <% request.setAttribute("proveedores", new ProveedorDAO().listarTodos()); %>
    <% request.setAttribute("productosExistentes", new ProductoDAO().listarTodos()); %>

    <div class="modal fade" id="modalProducto" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-white border-info">
                <div class="modal-header border-secondary">
                    <h5 class="modal-title">Registrar Nuevo Periférico</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form action="guardar-producto" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Nombre del Producto</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" required>
                        </div>
                        <div class="row mb-3">
                            <div class="col">
                                <label class="form-label small text-secondary">Precio (COP)</label>
                                <input type="number" name="precio" class="form-control bg-dark text-white border-secondary" required>
                            </div>
                            <div class="col">
                                <label class="form-label small text-secondary">Stock Inicial</label>
                                <input type="number" name="stock" class="form-control bg-dark text-white border-secondary" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Categoría</label>
                            <select name="categoriaId" class="form-select bg-dark text-white border-secondary" required>
                                <c:forEach var="cat" items="${categorias}">
                                    <option value="${cat.id}">${cat.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">URL de la Imagen</label>
                            <input type="text" name="imagen_url" class="form-control bg-dark text-white border-secondary" placeholder="https://...">
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Descripción</label>
                            <textarea name="descripcion" class="form-control bg-dark text-white border-secondary" rows="2"></textarea>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Proveedor</label>
                            <select name="proveedorId" class="form-select bg-dark text-white border-secondary" required>
                                <c:forEach var="prov" items="${proveedores}">
                                    <option value="${prov.id}">${prov.nombre}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-neon">GUARDAR EN INVENTARIO</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <%-- Modal para Ingresar Mercancía --%>
    <div class="modal fade" id="modalEntradaAlmacen" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content bg-dark text-white border-info">
                <div class="modal-header border-secondary">
                    <h5 class="modal-title">Registrar Entrada de Mercancía</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form action="entrada-almacen" method="post">
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Producto</label>
                            <select name="productoId" class="form-select bg-dark text-white border-secondary" required>
                                <c:forEach var="prod" items="${productosExistentes}">
                                    <option value="${prod.id}">${prod.nombre} (Stock: ${prod.stock})</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Cantidad a Ingresar</label>
                            <input type="number" name="cantidad" class="form-control bg-dark text-white border-secondary" min="1" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label small text-secondary">Precio de Compra Unitario</label>
                            <input type="number" name="precioCompra" class="form-control bg-dark text-white border-secondary" step="0.01" min="0" required>
                        </div>
                    </div>
                    <div class="modal-footer border-secondary">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-neon">REGISTRAR ENTRADA</button>
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