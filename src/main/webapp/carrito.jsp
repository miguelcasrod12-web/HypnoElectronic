<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.hypnoelectronic.model.Producto" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrito - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-dark { background-color: var(--card-bg) !important; border: 1px solid #333; }
        .img-thumb { width: 80px; height: 60px; object-fit: cover; border-radius: 5px; }
        .text-neon { color: var(--neon-blue); }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <h2 class="mb-4 text-white">Tu Carrito de Compras</h2>

        <%-- Verificamos si el carrito tiene productos --%>
        <c:choose>
            <c:when test="${not empty sessionScope.carrito}">
                <div class="row">
                    <div class="col-lg-8">
                        <div class="table-responsive">
                            <table class="table table-dark table-hover align-middle">
                                <thead>
                                    <tr>
                                        <th>Producto</th>
                                        <th>Nombre</th>
                                        <th>Precio</th>
                                        <th>Acción</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:set var="total" value="0" />
                                    <c:forEach var="item" items="${sessionScope.listaCarrito}">
                                        <c:set var="total" value="${total + item.subtotal}" />
                                        <tr id="fila-${item.producto.id}">
                                            <td><img src="${item.producto.imagen_url}" class="img-thumb"></td>
                                            <td>${item.producto.nombre}</td>
                                            <td class="text-center">
                                                <div class="d-flex align-items-center justify-content-center">
                                                    <a href="carrito?accion=disminuir&id=${item.producto.id}#fila-${item.producto.id}" class="btn btn-sm btn-outline-secondary px-2">-</a>

                                                    <span class="mx-3 fw-bold">${item.cantidad}</span>

                                                    <a href="carrito?accion=aumentar&id=${item.producto.id}#fila-${item.producto.id}" class="btn btn-sm btn-outline-secondary px-2">+</a>
                                                </div>
                                            </td>
                                            <td class="text-neon">$${item.producto.precio} COP</td>
                                            <td class="fw-bold">$${item.subtotal} COP</td>
                                            <td>
                                                <a href="carrito?accion=eliminar&id=${item.producto.id}" class="btn btn-sm btn-danger">
                                                    Eliminar
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <div class="col-lg-4">
                        <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid #333;">
                            <h4 class="text-white mb-3">Resumen</h4>
                            <div class="d-flex justify-content-between mb-3">
                                <span>Total:</span>
                                <h4 class="text-neon">$${total} COP</h4>
                            </div>
                            <hr class="text-secondary">
                            
                            <%-- LÓGICA DE SEGURIDAD: Solo paga si está logueado --%>
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <a href="checkout" class="btn btn-neon w-100 fw-bold">PROCEDER AL PAGO</a>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info bg-dark text-info border-info small">
                                        Para finalizar la compra, por favor inicia sesión.
                                    </div>
                                    <a href="login.jsp" class="btn btn-outline-info w-100 fw-bold">INICIAR SESIÓN</a>
                                </c:otherwise>
                            </c:choose>
                            
                            <a href="home" class="btn btn-link text-secondary w-100 mt-2 text-decoration-none small">Continuar comprando</a>
                        </div>
                    </div>
                </div>
            </c:when>
            
            <c:otherwise>
                <%-- Mensaje de carrito vacío que ya tenías --%>
                <div class="p-5 rounded-3 text-center" style="background-color: var(--card-bg); border: 2px dashed #333;">
                    <h2 style="color: var(--neon-blue);">Tu carrito está vacío</h2>
                    <p class="text-secondary">Parece que aún no has elegido tu próximo periférico.</p>
                    <a href="home" class="btn btn-neon mt-3">VOLVER AL CATÁLOGO</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <c:if test="${not empty sessionScope.errorStock}">
    <div class="alert alert-warning alert-dismissible fade show bg-dark text-warning border-warning" role="alert">
        <strong>Aviso de Bodega:</strong> ${sessionScope.errorStock}
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("errorStock"); // Se borra al recargar para que no sea molesto %>
</c:if>

<hr class="text-secondary">
<button onclick="window.print()" class="btn btn-outline-light w-100 fw-bold no-print">
    📥 DESCARGAR COTIZACIÓN (PDF)
</button>

<style>
    @media print {
        .no-print, .navbar, .btn, .btn-sm, .alert { display: none !important; }
        body { background-color: white !important; color: black !important; }
        .table-dark { color: black !important; background-color: white !important; border: 1px solid #000; }
        .text-neon { color: black !important; font-weight: bold; }
        .container { margin: 0; width: 100%; }
        h2 { color: black !important; text-align: center; margin-bottom: 30px; }
    }
</style>

<%@ include file="includes/footer.jsp" %>

</body>
</html>