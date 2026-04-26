<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Reportes Analíticos - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .kpi-link { text-decoration: none; display: block; }
        .kpi-card { background: #000; border: 1px solid #333; border-left: 4px solid var(--neon-blue); transition: 0.3s; cursor: pointer; }
        .kpi-card:hover { transform: translateY(-5px); border-color: var(--neon-blue); }
        .chart-container { background: #1a1a1a; border-radius: 15px; padding: 20px; border: 1px solid #333; }
    </style>
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-end mb-5">
            <div>
                <h1 class="display-6 fw-bold" style="color: var(--neon-blue);">Intelligence Center</h1>
                <p class="text-secondary mb-0">Métricas de rendimiento y salud del inventario</p>
            </div>
        </div>

        <!-- Indicadores Clave (KPIs) -->
        <div class="row g-4 mb-5">
            <div class="col-md-3">
                <a href="reporte-detalle?tipo=inventario" class="kpi-link">
                    <div class="card kpi-card p-3 shadow">
                        <div class="text-secondary small">VALOR TOTAL INVENTARIO</div>
                        <div class="h3 fw-bold text-success mt-1">$${valorInv}</div>
                    </div>
                </a>
            </div>
            <div class="col-md-3">
                <a href="reporte-detalle?tipo=alertas" class="kpi-link">
                    <div class="card kpi-card p-3 shadow" style="border-left-color: #ff4444;">
                        <div class="text-secondary small">ALERTAS DE STOCK</div>
                        <div class="h3 fw-bold text-danger mt-1">${stockCriticoCount} <small class="h6">ítems</small></div>
                    </div>
                </a>
            </div>
            <div class="col-md-3">
                <a href="reporte-detalle?tipo=clientes" class="kpi-link">
                    <div class="card kpi-card p-3 shadow" style="border-left-color: #ffc107;">
                        <div class="text-secondary small">CLIENTES ACTIVOS</div>
                        <div class="h3 fw-bold text-warning mt-1">${totalUsers}</div>
                    </div>
                </a>
            </div>
            <div class="col-md-3">
                <a href="reporte-detalle?tipo=catalogo" class="kpi-link">
                    <div class="card kpi-card p-3 shadow">
                        <div class="text-secondary small">CATÁLOGO TOTAL</div>
                        <div class="h3 fw-bold text-info mt-1">${productos.size()}</div>
                    </div>
                </a>
            </div>
        </div>

        <div class="row">
            <!-- Gráfica de Barras: Stock por Producto -->
            <div class="col-md-8">
                <div class="chart-container shadow mb-4">
                    <h5 class="text-info mb-4 border-bottom border-secondary pb-2">Distribución de Existencias</h5>
                    <canvas id="chartStock"></canvas>
                </div>
            </div>
            
            <!-- Gráfica de Pastel: Categorías -->
            <div class="col-md-4">
                <div class="chart-container shadow mb-4">
                    <h5 class="text-warning mb-4 border-bottom border-secondary pb-2">Stock por Categoría</h5>
                    <canvas id="chartCategories"></canvas>
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

    <div class="container my-5">
        <a href="dashboard.jsp" class="btn btn-secondary btn-sm">Volver al Panel</a>
    </div>

    <!-- Scripts obligatorios -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            try {
                // 1. Extracción Segura de Datos desde JSP a JS
                const masterProducts = [];
                const registerItem = (n, c, p, s) => masterProducts.push({ nombre: n, categoria: c, precio: p, stock: s });

                <c:forEach var="p" items="${productos}">
                    registerItem("<c:out value='${p.nombre}'/>", "<c:out value='${p.categoriaNombre}'/>", Number("${p.precio}"), Number("${p.stock}"));
                </c:forEach>

                const catDataLabels = [];
                const catDataValues = [];
                const registerCat = (label, value) => { catDataLabels.push(label); catDataValues.push(value); };

                <c:forEach var="entry" items="${catData}">
                    registerCat("<c:out value='${entry.key}'/>", Number("${entry.value}"));
                </c:forEach>

                // 2. Gráfica de Barras (Existencias)
                const labelsBar = masterProducts.map(p => p.nombre);
                const dataBar = masterProducts.map(p => p.stock);

                new Chart(document.getElementById('chartStock'), {
                    type: 'bar',
                    data: {
                        labels: labelsBar,
                        datasets: [{ 
                            label: 'Stock Actual', 
                            data: dataBar, 
                            backgroundColor: '#00d4ff', 
                            borderRadius: 5 
                        }]
                    },
                    options: { responsive: true, plugins: { legend: { display: false } } }
                });

                // 3. Gráfica de Pastel (Categorías)
                new Chart(document.getElementById('chartCategories'), {
                    type: 'doughnut',
                    data: {
                        labels: catDataLabels,
                        datasets: [{ 
                            data: catDataValues, 
                            backgroundColor: ['#00d4ff', '#ffc107', '#ff4444', '#00ff88', '#9d00ff', '#f39c12', '#e74c3c'],
                            borderWidth: 0
                        }]
                    },
                    options: { 
                        responsive: true, 
                        plugins: { legend: { position: 'bottom', labels: { color: '#fff', padding: 20 } } },
                        onClick: (evt, item) => {
                            if (item.length > 0) {
                                const index = item[0].index;
                                const catName = catDataLabels[index];
                                mostrarDetalle(catName);
                            }
                        }
                    }
                });

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
            } catch (err) {
                console.error("Error inicializando el Intelligence Center:", err);
            }
        });
    </script>
</body>
</html>