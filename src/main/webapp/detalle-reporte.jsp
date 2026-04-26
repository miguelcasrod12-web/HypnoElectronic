<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>${titulo} - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container mt-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="text-info">${titulo}</h2>
            <a href="exportar-reporte?tipo=${tipo}" class="btn btn-success fw-bold">📥 EXPORTAR A EXCEL (CSV)</a>
        </div>

        <div class="card bg-black border-secondary p-4 shadow-lg">
            <table class="table table-dark table-hover">
                <thead>
                    <c:choose>
                        <c:when test="${tipo == 'clientes'}">
                            <tr><th>Nombre</th><th>Username</th><th>Email</th><th>Fecha Registro</th></tr>
                        </c:when>
                        <c:otherwise>
                            <tr><th>Producto</th><th>Categoría</th><th>Precio</th><th>Existencia</th><th>Valor en Stock</th></tr>
                        </c:otherwise>
                    </c:choose>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${lista}">
                        <tr>
                            <c:choose>
                                <c:when test="${tipo == 'clientes'}">
                                    <td>${item.fullName}</td>
                                    <td class="text-info">@${item.username}</td>
                                    <td>${item.email}</td>
                                    <td class="text-secondary small">${item.createdAt}</td>
                                </c:when>
                                <c:otherwise>
                                    <td>${item.nombre}</td>
                                    <td><span class="badge bg-secondary">${item.categoriaNombre}</span></td>
                                    <td>$${item.precio}</td>
                                    <td class="${item.stock < 5 ? 'text-danger fw-bold' : ''}">${item.stock}</td>
                                    <td class="text-success">$${item.precio * item.stock}</td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <div class="mt-4">
            <a href="reporte-ventas" class="btn btn-outline-secondary">← Volver al Intelligence Center</a>
        </div>
    </div>
</body>
</html>