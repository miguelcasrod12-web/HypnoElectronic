<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mis Compras - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <h2 class="mb-4" style="color: var(--neon-blue);">Historial de Compras</h2>

        <c:if test="${empty pedidos}">
            <div class="card p-5 text-center bg-black border-secondary">
                <p class="text-secondary">Aún no has realizado ninguna compra.</p>
                <a href="home" class="btn btn-neon mt-3">Ir al Catálogo</a>
            </div>
        </c:if>

        <c:if test="${not empty pedidos}">
            <div class="card bg-black border-secondary p-3 shadow-lg">
                <table class="table table-dark table-hover align-middle">
                    <thead>
                        <tr class="text-secondary">
                            <th>Orden #</th>
                            <th>Monto Total</th>
                            <th>Estado del Pedido</th>
                            <th class="text-center">Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ped" items="${pedidos}">
                            <tr>
                                <td class="fw-bold text-info">#${ped.id}</td>
                                <td class="text-neon">$${ped.total} COP</td>
                                <td>
                                    <span class="badge ${ped.estado == 'pagado' ? 'bg-success' : 'bg-warning text-dark'}">
                                        ${ped.estado.toUpperCase()}
                                    </span>
                                </td>
                                <td class="text-center">
                                    <button class="btn btn-sm btn-outline-info" onclick="alert('Detalle de orden #${ped.id} en construcción')">Ver Detalles</button>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>

        <div class="mt-4">
            <a href="configuracion.jsp" class="btn btn-secondary btn-sm">Volver a Perfil</a>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>