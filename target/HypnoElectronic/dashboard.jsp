<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.hypnoelectronic.model.Usuario" %>
<%@ page import="com.hypnoelectronic.dao.ProductoDAO" %>
<%@ page import="com.hypnoelectronic.model.Producto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%
    Usuario userSession = (Usuario) session.getAttribute("user");
    if (userSession == null || !"admin".equals(userSession.getUserType())) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Control - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

    <%@ include file="includes/navbar.jsp" %>

    <div class="container">
        <div class="row mb-4">
            <div class="col-12">
                <div class="p-4 rounded-3" style="background-color: var(--card-bg); border-left: 5px solid var(--neon-blue);">
                    <h1 class="display-5 fw-bold" style="color: var(--neon-blue);">Bienvenido, <%= userSession.getFullName() %>!</h1>
                    <p class="lead text-secondary">Panel de control de HypnoElectronic</p>
                </div>
            </div>
        </div>

        <%-- ALERTA DE STOCK CRÍTICO (ESTILO ODOO) --%>
        <%
            ProductoDAO pDao = new ProductoDAO();
            List<Producto> todos = pDao.listarTodos();
            List<Producto> stockCritico = new ArrayList<Producto>();
            if (todos != null) {
                for (Producto p : todos) {
                    if (p.getStock() < 3) stockCritico.add(p);
                }
            }
            if (!stockCritico.isEmpty()) {
        %>
            <div class="alert alert-warning border-warning bg-dark text-warning mb-4 shadow-sm">
                <strong>⚠️ Alerta de Suministros:</strong> Tienes <%= stockCritico.size() %> productos con stock crítico (menos de 3 unidades). 
                <a href="admin-inventario" class="alert-link text-decoration-none ms-2">Revisar Almacén →</a>
            </div>
        <% } %>

        <h2 class="mb-4 text-white">Módulos del Sistema</h2>
        <div class="row g-4">
            
            <div class="col-md-3">
                <div class="card h-100 p-3 text-center" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <div class="card-body">
                        <div class="mb-3" style="font-size: 2rem; color: var(--neon-blue);">👥</div>
                        <h5 class="card-title text-white">Usuarios</h5>
                        <p class="card-text text-secondary small">Administrar perfiles y permisos.</p>
                        <a href="usuarios-admin" class="btn btn-outline-info btn-sm w-100">Entrar</a>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card h-100 p-3 text-center" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <div class="card-body">
                        <div class="mb-3" style="font-size: 2rem; color: var(--neon-blue);">📦</div>
                        <h5 class="card-title text-white">Inventario</h5>
                        <p class="card-text text-secondary small">Control de stock de periféricos.</p>
                        <a href="admin-inventario" class="btn btn-outline-info btn-sm w-100">Ver Panel</a>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card h-100 p-3 text-center" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <div class="card-body">
                        <div class="mb-3" style="font-size: 2rem; color: var(--neon-blue);">📊</div>
                        <h5 class="card-title text-white">Reportes</h5>
                        <p class="card-text text-secondary small">Estadísticas de ventas y flujo.</p>
                        <a href="reporte-ventas" class="btn btn-outline-info btn-sm w-100">Generar</a>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card h-100 p-3 text-center" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <div class="card-body">
                        <div class="mb-3" style="font-size: 2rem; color: var(--neon-blue);">🏷️</div>
                        <h5 class="card-title text-white">Categorías</h5>
                        <p class="card-text text-secondary small">Organizar familias de productos.</p>
                        <a href="admin-categorias" class="btn btn-outline-info btn-sm w-100">Gestionar</a>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card h-100 p-3 text-center" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <div class="card-body">
                        <div class="mb-3" style="font-size: 2rem; color: var(--neon-blue);">🚚</div>
                        <h5 class="card-title text-white">Proveedores</h5>
                        <p class="card-text text-secondary small">Alianzas y suministros.</p>
                        <a href="admin-proveedores" class="btn btn-outline-info btn-sm w-100">Gestionar</a>
                    </div>
                </div>
            </div>

            <div class="col-md-3">
                <div class="card h-100 p-3 text-center" style="background-color: var(--card-bg); border: 1px solid #333;">
                    <div class="card-body">
                        <div class="mb-3" style="font-size: 2rem; color: var(--neon-blue);">⚙️</div>
                        <h5 class="card-title text-white">Configuración</h5>
                        <p class="card-text text-secondary small">Ajustes generales del sistema.</p>
                        <a href="configuracion.jsp" class="btn btn-outline-info btn-sm w-100">Ajustar</a>
                    </div>
                </div>
            </div>

        </div>

        <div class="mt-5 pt-3 border-top border-secondary text-center">
             <a href="logout.jsp" class="btn btn-danger btn-sm">Cerrar Sesión</a>
             <a href="usuarios-admin" class="btn btn-link text-secondary text-decoration-none">Gestionar Usuarios</a>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>