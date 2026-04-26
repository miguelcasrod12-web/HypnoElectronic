<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirmación de Compra - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .card-confirm { background-color: var(--card-bg); border: 1px solid #333; border-radius: 15px; }
        .text-neon { color: var(--neon-blue); }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card card-confirm p-4 shadow-lg text-center">
                    <h2 class="text-neon mb-3">¡Compra Realizada con Éxito!</h2>
                    <p class="text-white lead">Tu pedido <span class="fw-bold">#${param.pedidoId}</span> ha sido procesado.</p>
                    <p class="text-secondary">Recibirás un correo electrónico con los detalles de tu compra.</p>
                    <a href="home" class="btn btn-neon mt-4">Volver al Catálogo</a>
                    <a href="configuracion.jsp" class="btn btn-outline-info mt-2">Ver Mis Pedidos</a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>