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
                        <form action="checkout" method="post" class="row g-3">
                            <div class="col-12">
                                <h4 class="text-neon border-bottom border-secondary pb-2 mb-3">Información de Envío</h4>
                            </div>
                            <div class="col-md-12">
                                <label class="form-label text-secondary small">Dirección de Entrega</label>
                                <input type="text" name="direccion" class="form-control bg-dark text-white border-secondary" 
                                       placeholder="Ej: Calle 5 #10-20, Cajicá" value="${sessionScope.user.direccion}" required>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label text-secondary small">Ciudad</label>
                                <input type="text" name="ciudad" class="form-control bg-dark text-white border-secondary" 
                                       placeholder="Cajicá" value="${not empty sessionScope.user.ciudad ? sessionScope.user.ciudad : 'Cajicá'}" required>
                            </div>
                            <div class="col-md-8">
                                <label class="form-label text-secondary small">Teléfono de Contacto</label>
                                <input type="tel" name="telefono" class="form-control bg-dark text-white border-secondary" 
                                       placeholder="Ej: 3001234567" value="${sessionScope.user.telefono}" required>
                            </div>

                            <div class="col-12 mt-4">
                                <h4 class="text-neon border-bottom border-secondary pb-2 mb-3">Resumen de Compra</h4>
                                <div class="table-responsive">
                                    <table class="table table-dark table-borderless align-middle mb-0">
                                        <c:forEach var="item" items="${sessionScope.listaCarrito}">
                                            <tr>
                                                <td style="width: 50px;">
                                                    <img src="${item.producto.imagen_url}" width="40" class="rounded">
                                                </td>
                                                <td>
                                                    <div class="small fw-bold">${item.producto.nombre}</div>
                                                    <div class="text-muted small">${item.cantidad} unidad(es)</div>
                                                </td>
                                                <td class="text-end text-neon small">
                                                    $${item.subtotal}
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>

                            <div class="col-12 mt-4">
                                <h4 class="text-neon border-bottom border-secondary pb-2 mb-3">Resumen de Pago</h4>
                                <div class="p-3 rounded bg-black border border-secondary mb-3">
                                    <div class="d-flex justify-content-between">
                                        <span class="text-secondary">Subtotal (${sessionScope.listaCarrito.size()} periféricos)</span>
                                        <span class="text-white">$${totalFinal}</span>
                                    </div>
                                    <div class="d-flex justify-content-between mt-2 fw-bold h5">
                                        <span class="text-white">TOTAL A PAGAR:</span>
                                        <span class="text-neon">$${totalFinal} COP</span>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="col-12 mt-4">
                                <button type="submit" class="btn btn-neon btn-lg w-100 fw-bold shadow">
                                    <i class="bi bi-credit-card"></i> PASAR A PAGAR
                                </button>
                                <a href="carrito.jsp" class="btn btn-outline-secondary w-100 mt-2">Corregir Carrito</a>
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>