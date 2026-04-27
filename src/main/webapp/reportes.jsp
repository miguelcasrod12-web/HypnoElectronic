<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Reportes Analíticos - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .kpi-link { text-decoration: none; display: block; }
        .kpi-card { 
            background: #000; 
            border: 1px solid #333; 
            border-left: 4px solid var(--neon-blue); 
            transition: 0.3s; 
            cursor: pointer; 
            height: 95px; 
            display: flex;
            flex-direction: column;
            justify-content: center;
            container-type: inline-size;
        }
        .kpi-card:hover { transform: translateY(-5px); border-color: var(--neon-blue); }
        .kpi-title { white-space: nowrap; overflow: hidden; text-overflow: ellipsis; font-size: 0.75rem; color: #adb5bd; }
        .kpi-value { 
            white-space: nowrap; 
            overflow: hidden; 
            text-overflow: ellipsis;
            font-size: clamp(0.9rem, 13cqw, 1.6rem); 
            font-weight: 700;
        }
        .chart-container { 
            background: #1a1a1a; 
            border-radius: 15px; 
            padding: 15px; 
            border: 1px solid #333; 
            min-height: 350px; 
            height: auto;
            position: relative;
            overflow-y: auto;
        }
        tr[onclick] { cursor: pointer; }
        tr[onclick]:hover { background-color: rgba(0, 212, 255, 0.1) !important; }
    </style>
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-end mb-5">
            <div>
                <h1 class="display-6 fw-bold" style="color: var(--neon-blue);">Intelligence Center</h1>
                <p class="text-secondary mb-0">
                    Sincronizado con DB: <span class="text-info"><fmt:formatDate value="<%= new Date() %>" pattern="HH:mm:ss" /></span>
                </p>
            </div>
            <a href="reporte-ventas" class="btn btn-sm btn-neon">
                <i class="bi bi-arrow-clockwise"></i> REFRESCAR DATOS
            </a>
        </div>

        <!-- Indicadores Clave (KPIs) -->
        <div class="row g-2 mb-5">
            <!-- 1. Ingresos -->
            <div class="col-xl-2 col-md-4 col-6">
                <a href="javascript:void(0)" onclick="mostrarDetalleIngresos()" class="kpi-link">
                    <div class="card kpi-card p-2 shadow" style="border-left-color: var(--neon-blue);">
                        <div class="kpi-title text-uppercase">Ingresos Totales</div>
                        <div class="kpi-value text-info">$<fmt:formatNumber value="${ventasTotales}" type="number" maxFractionDigits="0" /></div>
                    </div>
                </a>
            </div>
            <!-- 2. Órdenes -->
            <div class="col-xl-2 col-md-4 col-6">
                <a href="reporte-detalle?tipo=pedidos" class="kpi-link">
                    <div class="card kpi-card p-2 shadow" style="border-left-color: #9d00ff;">
                        <div class="kpi-title text-uppercase">Órdenes</div>
                        <div class="kpi-value text-white">${pedidos.size()}</div>
                    </div>
                </a>
            </div>
            <!-- 3. Clientes -->
            <div class="col-xl-2 col-md-4 col-6">
                <a href="reporte-detalle?tipo=clientes" class="kpi-link">
                    <div class="card kpi-card p-2 shadow" style="border-left-color: #ffc107;">
                        <div class="kpi-title text-uppercase">Clientes</div>
                        <div class="kpi-value text-warning">${totalUsers}</div>
                    </div>
                </a>
            </div>
            <!-- 4. Valor Stock -->
            <div class="col-xl-2 col-md-4 col-6">
                <a href="reporte-detalle?tipo=inventario" class="kpi-link">
                    <div class="card kpi-card p-2 shadow" style="border-left-color: #00ff88;">
                        <div class="kpi-title text-uppercase">Valor Almacén</div>
                        <div class="kpi-value text-success">$<fmt:formatNumber value="${valorInv}" type="number" maxFractionDigits="0" /></div>
                    </div>
                </a>
            </div>
            <!-- 5. Alertas -->
            <div class="col-xl-2 col-md-4 col-6">
                <a href="reporte-detalle?tipo=alertas" class="kpi-link">
                    <div class="card kpi-card p-2 shadow" style="border-left-color: #ff4444;">
                        <div class="kpi-title text-uppercase">Alertas Stock</div>
                        <div class="kpi-value text-danger">${stockCriticoCount}</div>
                    </div>
                </a>
            </div>
            <!-- 6. Catálogo -->
            <div class="col-xl-2 col-md-4 col-6">
                <a href="reporte-detalle?tipo=catalogo" class="kpi-link">
                    <div class="card kpi-card p-2 shadow" style="border-left-color: #00d4ff;">
                        <div class="kpi-title text-uppercase">Ítems Catálogo</div>
                        <div class="kpi-value text-info">${productos.size()}</div>
                    </div>
                </a>
            </div>
        </div>

        <div class="row">
            <!-- Tabla: Stock por Proveedor -->
            <div class="col-md-5">
                <div class="chart-container shadow mb-4">
                    <h5 class="text-info mb-3 border-bottom border-secondary pb-2">Existencias por Proveedor</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-hover table-sm mb-0">
                            <thead>
                                <tr class="text-secondary small">
                                    <th>Proveedor</th>
                                    <th class="text-end">Unidades</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${provData}">
                                    <tr onclick="mostrarDetalleProv(this.getAttribute('data-key'))" data-key="<c:out value='${entry.key}'/>">
                                        <td class="py-2">${entry.key}</td>
                                        <td class="text-end fw-bold text-success py-2">${entry.value}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            
            <!-- Tabla: Stock por Categoría -->
            <div class="col-md-4">
                <div class="chart-container shadow mb-4">
                    <h5 class="text-warning mb-3 border-bottom border-secondary pb-2">Stock por Categoría</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-hover table-sm mb-0">
                            <thead>
                                <tr class="text-secondary small">
                                    <th>Categoría</th>
                                    <th class="text-end">Stock</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${catData}">
                                    <tr onclick="mostrarDetalle(this.getAttribute('data-key'))" data-key="<c:out value='${entry.key}'/>">
                                        <td class="py-2">${entry.key}</td>
                                        <td class="text-end fw-bold text-info py-2">${entry.value}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- Tabla: Estados de Pedido -->
            <div class="col-md-3">
                <div class="chart-container shadow mb-4">
                    <h5 class="text-neon mb-3 border-bottom border-secondary pb-2">Estado de Pedidos</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-hover table-sm mb-0">
                            <thead>
                                <tr class="text-secondary small">
                                    <th>Estado</th>
                                    <th class="text-end">Cant.</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${statusData}">
                                    <tr onclick="mostrarDetalleEstado(this.getAttribute('data-key'))" data-key="<c:out value='${entry.key}'/>">
                                        <td class="text-uppercase small py-2">${entry.key}</td>
                                        <td class="text-end fw-bold text-white py-2">${entry.value}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- NUEVA TABLA: ÚLTIMAS TRANSACCIONES -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card bg-black border-secondary p-4 shadow mb-4">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="text-info mb-0">Ventas Recientes</h5>
                        <a href="admin-pedidos" class="btn btn-sm btn-outline-info">Gestionar Despachos</a>
                    </div>
                    <table class="table table-dark table-hover mb-0">
                        <thead class="text-secondary">
                            <tr><th>ID</th><th>Cliente</th><th>Fecha</th><th>Total</th><th>Estado</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="ped" items="${pedidos}" begin="0" end="4">
                                <tr>
                                    <td class="text-info">#${ped.id}</td>
                                    <td>${ped.usuarioNombre}</td>
                                    <td class="small text-secondary"><fmt:formatDate value="${ped.fecha}" pattern="dd/MM/yyyy HH:mm" /></td>
                                    <td class="text-success">$<fmt:formatNumber value="${ped.total}" type="number" /></td>
                                    <td>
                                        <span class="badge ${ped.estado == 'entregado' ? 'bg-success' : 'bg-warning text-dark'}">${(ped.estado != null ? ped.estado : 'pendiente').toUpperCase()}</span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Tabla de Detalle Crítico -->
        <div class="row mt-4">
            <div class="col-12">
                <div class="card bg-black border-secondary p-4 shadow">
                    <h5 class="text-danger mb-3">Auditoría de Reposición (Stock < 5)</h5>
                    <table class="table table-dark table-hover">
                        <thead class="text-secondary">
                            <tr><th>Referencia</th><th>Categoría</th><th>Estado</th><th class="text-end">Existencia</th></tr>
                        </thead>
                        <tbody>
                            <c:forEach var="p" items="${productos}">
                                <c:if test="${p.stock < 5}">
                                    <tr>
                                        <td>${p.nombre}</td>
                                        <td><span class="badge bg-secondary">${p.categoriaNombre}</span></td>
                                        <td><span class="text-danger">● REPOSICIÓN INMEDIATA</span></td>
                                        <td class="text-end fw-bold text-danger">${p.stock}</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para Detalle de Ingresos -->
    <div class="modal fade" id="modalIngresos" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content bg-dark text-white border-success shadow-lg">
                <div class="modal-header border-secondary">
                    <h5 class="modal-title text-success">Desglose de Ingresos por Venta</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="table-responsive">
                        <table class="table table-dark table-hover mb-0">
                            <thead>
                                <tr class="text-secondary">
                                    <th>Orden ID</th>
                                    <th>Cliente</th>
                                    <th>Fecha</th>
                                    <th>Estado</th>
                                    <th class="text-end">Monto (COP)</th>
                                </tr>
                            </thead>
                            <tbody id="tableBodyIngresos">
                                <!-- Se llena vía JS -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para Detalle de Categoría -->
    <div class="modal fade" id="modalDetalleCat" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-lg modal-dialog-centered">
            <div class="modal-content bg-dark text-white border-info shadow-lg">
                <div class="modal-header border-secondary">
                    <h5 class="modal-title" id="modalTitle">Detalle de Categoría</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="table-responsive">
                        <table class="table table-dark table-hover mb-0">
                            <thead>
                                <tr class="text-secondary">
                                    <th>Producto</th>
                                    <th>Precio</th>
                                    <th class="text-center">Stock Disponible</th>
                                </tr>
                            </thead>
                            <tbody id="tableBodyDetalle">
                                <!-- Se llena dinámicamente -->
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Almacén de datos oculto para transferencia segura de JSP a JS (Dataset Pattern) -->
    <div id="jspDataStore" style="display: none;">
        <c:forEach var="p" items="${productos}">
            <div class="raw-product" data-nombre="<c:out value='${p.nombre}'/>" data-categoria="<c:out value='${p.categoriaNombre}' default='Otros'/>" data-proveedor="<c:out value='${p.proveedorNombre}' default='Sin Asignar'/>" data-precio="${p.precio}" data-stock="${p.stock}"></div>
        </c:forEach>
        <c:forEach var="ped" items="${pedidos}">
            <div class="raw-order" data-id="${ped.id}" data-cliente="<c:out value='${ped.usuarioNombre}'/>" data-fecha="<fmt:formatDate value='${ped.fecha}' pattern='dd/MM/yyyy' />" data-estado="<c:out value='${ped.estado}' default='pendiente'/>" data-total="${ped.total}"></div>
        </c:forEach>
        <c:forEach var="entry" items="${catData}">
            <div class="raw-cat" data-key="<c:out value='${entry.key}'/>" data-value="${entry.value}"></div>
        </c:forEach>
        <c:forEach var="entry" items="${provData}">
            <div class="raw-prov" data-key="<c:out value='${entry.key}'/>" data-value="${entry.value}"></div>
        </c:forEach>
        <c:forEach var="entry" items="${statusData}">
            <div class="raw-status" data-key="<c:out value='${entry.key}'/>" data-value="${entry.value}"></div>
        </c:forEach>
    </div>

    <div class="container my-5">
        <a href="dashboard.jsp" class="btn btn-secondary btn-sm">Volver al Panel</a>
    </div>

    <%@ include file="includes/footer.jsp" %>

    <!-- Scripts obligatorios -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            try {
                // 1. Extracción de Datos vía DOM (Dataset) - 100% seguro contra errores de sintaxis y red lines
                const masterProducts = Array.from(document.querySelectorAll('.raw-product')).map(el => ({
                    nombre: el.dataset.nombre, categoria: el.dataset.categoria, proveedor: el.dataset.proveedor, precio: Number(el.dataset.precio || 0), stock: Number(el.dataset.stock || 0)
                }));
                window.masterOrders = Array.from(document.querySelectorAll('.raw-order')).map(el => ({
                    id: el.dataset.id, cliente: el.dataset.cliente, fecha: el.dataset.fecha, estado: el.dataset.estado, total: Number(el.dataset.total || 0)
                }));

                // Función para Categorías (Existente mejorada)
                function mostrarDetalle(categoria) {
                    const filtered = masterProducts.filter(p => p.categoria === categoria);
                    document.getElementById('modalTitle').innerText = "Productos en: " + categoria;
                    const tbody = document.getElementById('tableBodyDetalle');
                    tbody.innerHTML = filtered.map(function(p) {
                        return '<tr>' +
                            '<td>' + p.nombre + '</td>' +
                            '<td class="text-info">$' + p.precio + '</td>' +
                            '<td class="text-center fw-bold ' + (p.stock < 5 ? 'text-danger' : 'text-success') + '">' + p.stock + '</td>' +
                            '</tr>';
                    }).join('');
                    const myModal = new bootstrap.Modal(document.getElementById('modalDetalleCat'));
                    myModal.show();
                }
                window.mostrarDetalle = mostrarDetalle; // Hacerla global

                // Función para Proveedores (Nueva)
                window.mostrarDetalleProv = function(proveedor) {
                    const filtered = masterProducts.filter(p => p.proveedor === proveedor);
                    document.getElementById('modalTitle').innerText = "Inventario de: " + proveedor;
                    const tbody = document.getElementById('tableBodyDetalle');
                    tbody.innerHTML = filtered.map(p => `
                        <tr>
                            <td>\${p.nombre} <br><small class="text-secondary">\${p.categoria}</small></td>
                            <td class="text-info">$\${p.precio}</td>
                            <td class="text-center fw-bold \${p.stock < 5 ? 'text-danger' : 'text-success'}">\${p.stock}</td>
                        </tr>
                    `).join('');
                    new bootstrap.Modal(document.getElementById('modalDetalleCat')).show();
                };

                // Función para Estados de Pedido (Nueva)
                window.mostrarDetalleEstado = function(estado) {
                    if(!window.masterOrders) return;
                    const filtered = window.masterOrders.filter(o => o.estado.toLowerCase() === estado.toLowerCase());
                    
                    // Reutilizamos el Modal de Ingresos cambiando el título dinámicamente
                    const modalEl = document.getElementById('modalIngresos');
                    modalEl.querySelector('.modal-title').innerText = "Pedidos en estado: " + estado.toUpperCase();
                    modalEl.querySelector('.modal-title').className = "modal-title text-info";
                    
                    const tbody = document.getElementById('tableBodyIngresos');
                    tbody.innerHTML = filtered.map(o => `
                        <tr>
                            <td class="text-info">#\${o.id}</td>
                            <td class="small fw-bold text-white">\${o.cliente}</td>
                            <td class="small">\${o.fecha}</td>
                            <td><span class="badge \${o.estado === 'entregado' ? 'bg-success' : 'bg-warning text-dark'} text-uppercase">\${o.estado}</span></td>
                            <td class="text-end fw-bold text-success">$\${o.total.toLocaleString()}</td>
                        </tr>
                    `).join('');
                    
                    new bootstrap.Modal(modalEl).show();
                };

            } catch (err) {
                console.error("Error inicializando el Intelligence Center:", err);
            }
        });

        // Función fuera del DOMContentLoaded para disponibilidad inmediata al click
        function mostrarDetalleIngresos() {
            const tbody = document.getElementById('tableBodyIngresos');
            if(!window.masterOrders) return;
            tbody.innerHTML = window.masterOrders.map(o => `
                <tr>
                    <td class="text-info">#\${o.id}</td>
                    <td class="small fw-bold text-white">\${o.cliente}</td>
                    <td class="small">\${o.fecha}</td>
                    <td><span class="badge \${o.estado === 'entregado' ? 'bg-success' : 'bg-warning text-dark'} text-uppercase">\${o.estado || 'pendiente'}</span></td>
                    <td class="text-end fw-bold text-success">$\${(o.total || 0).toLocaleString()}</td>
                </tr>
            `).join('');
            new bootstrap.Modal(document.getElementById('modalIngresos')).show();
        }
    </script>
</body>
</html>