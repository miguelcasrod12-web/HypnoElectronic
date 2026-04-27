<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Gestión de Despachos - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    <div class="container mt-4">
        <h1 class="mb-4 text-info">Control de Órdenes de Venta</h1>
        
        <c:if test="${empty pedidos}">
            <div class="alert alert-info bg-dark text-info border-secondary text-center p-5">
                <i class="bi bi-inbox-fill display-1 d-block mb-3"></i>
                <h4>No se encontraron órdenes de venta</h4>
                <p class="text-secondary small">Si hay datos en la DB, revisa la consola de Tomcat por errores de columnas.</p>
            </div>
        </c:if>

        <c:if test="${not empty pedidos}">
        <div class="card bg-black border-secondary p-3">
            <table class="table table-dark table-hover align-middle">
                <thead>
                    <tr class="text-secondary">
                        <th>ID</th>
                        <th>Cliente</th>
                        <th>Fecha</th>
                        <th>Total</th>
                        <th>Estado</th>
                        <th>N° Guía</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${pedidos}">
                        <tr>
                            <td>#${p.id}</td>
                            <td class="fw-bold">${p.usuarioNombre}</td>
                            <td class="small text-secondary">${p.fecha}</td>
                            <td class="text-success">$${p.total}</td>
                            <td>
                                <span class="badge ${p.estado == 'entregado' ? 'bg-success' : 'bg-warning text-dark'}">
                                    ${(p.estado != null ? p.estado : 'pendiente').toUpperCase()}
                                </span>
                            </td>
                            <td class="text-info font-monospace">${p.numeroGuia != null ? p.numeroGuia : '---'}</td>
                            <td class="text-center">
                                <button class="btn btn-sm btn-outline-info" data-bs-toggle="modal" data-bs-target="#modal-${p.id}">⚙️ Gestionar</button>
                            </td>
                        </tr>

                        <!-- Modal de Gestión -->
                        <div class="modal fade" id="modal-${p.id}" tabindex="-1" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content bg-dark border-info text-white">
                                    <form action="admin-pedidos" method="post">
                                        <input type="hidden" name="accion" value="actualizar">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <div class="modal-header border-secondary">
                                            <h5 class="modal-title">Gestionar Pedido #${p.id}</h5>
                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="p-2 mb-3 rounded bg-black border border-secondary">
                                                <p class="mb-1 small text-info"><i class="bi bi-geo-alt"></i> Destino:</p>
                                                <p class="mb-2 small">${p.direccionEnvio != null ? p.direccionEnvio : 'No especificada'}</p>
                                                <p class="mb-1 small text-info"><i class="bi bi-telephone"></i> Contacto:</p>
                                                <p class="mb-0 small">${p.telefonoContacto != null ? p.telefonoContacto : 'No especificado'}</p>
                                            </div>

                                            <div class="mb-3">
                                                <label class="form-label small text-secondary">Estado del Proceso</label>
                                                <select name="estado" class="form-select bg-black text-white border-secondary">
                                                    <option value="pendiente" ${p.estado == 'pendiente' ? 'selected' : ''}>Pendiente</option>
                                                    <option value="pagado" ${p.estado == 'pagado' ? 'selected' : ''}>Pagado (Listo para alistar)</option>
                                                    <option value="alistado" ${p.estado == 'alistado' ? 'selected' : ''}>Alistado (En bodega)</option>
                                                    <option value="recogido" ${p.estado == 'recogido' ? 'selected' : ''}>Recogido por Transportadora</option>
                                                    <option value="entregado" ${p.estado == 'entregado' ? 'selected' : ''}>Entregado al Cliente</option>
                                                    <option value="cancelado" ${p.estado == 'cancelado' ? 'selected' : ''}>Cancelado</option>
                                                </select>
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label small text-secondary">Número de Guía / Tracking</label>
                                                <input type="text" name="guia" class="form-control bg-black text-white border-secondary" value="${p.numeroGuia}">
                                            </div>
                                        </div>
                                        <div class="modal-footer border-secondary">
                                            <button type="submit" class="btn btn-neon w-100">ACTUALIZAR PEDIDO</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        </c:if>
        <a href="dashboard.jsp" class="btn btn-secondary mt-4 btn-sm">Volver al Panel</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>