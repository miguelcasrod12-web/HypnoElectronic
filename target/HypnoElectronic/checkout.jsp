<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card-checkout { background-color: var(--card-bg); border: 1px solid #333; border-radius: 15px; }
        .text-neon { color: var(--neon-blue); }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card card-checkout p-4 shadow-lg">
                    <h2 class="text-white mb-4">Finalizar Compra</h2>
                    
                    <c:if test="${empty sessionScope.carrito}">
                        <div class="alert alert-warning bg-dark text-warning border-warning">
                            Tu carrito está vacío. No puedes proceder al checkout.
                        </div>
                        <a href="home" class="btn btn-outline-info mt-3">Volver al Catálogo</a>
                    </c:if>

                    <c:if test="${not empty sessionScope.carrito}">
                        <h4 class="text-neon mb-3">Resumen de tu Pedido</h4>
                        <ul class="list-group list-group-flush bg-dark border-secondary mb-4">
                            <c:set var="totalFinal" value="0" />
                            <c:forEach var="item" items="${sessionScope.listaCarrito}">
                                <li class="list-group-item bg-dark text-white border-secondary d-flex justify-content-between align-items-center">
                                    <div>
                                        ${item.producto.nombre} <span class="badge bg-secondary">${item.cantidad} x $${item.producto.precio}</span>
                                    </div>
                                    <span class="fw-bold">$${item.subtotal}</span>
                                </li>
                                <c:set var="totalFinal" value="${totalFinal + item.subtotal}" />
                            </c:forEach>
                            <li class="list-group-item bg-dark text-white border-secondary d-flex justify-content-between align-items-center fw-bold text-neon">
                                Total a Pagar: <span>$${totalFinal} COP</span>
                            </li>
                        </ul>
                        <form action="checkout" method="post">
                            <button type="submit" class="btn btn-neon w-100 fw-bold">CONFIRMAR COMPRA Y PAGAR</button>
                        </form>
                        <a href="carrito.jsp" class="btn btn-outline-secondary w-100 mt-2">Volver al Carrito</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>