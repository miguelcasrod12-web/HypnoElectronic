<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${p.nombre} - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .product-img { width: 100%; max-height: 500px; object-fit: contain; border-radius: 15px; background: #1a1a1a; }
        .text-neon { color: var(--neon-blue); text-shadow: 0 0 10px var(--neon-blue); }
        .card-detalle { background: #121212; border: 1px solid #333; border-radius: 20px; padding: 30px; }
    </style>
</head>
<body class="bg-black text-white">
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row card-detalle">
            <div class="col-md-6 mb-4">
                <img src="${p.imagen_url}" class="product-img shadow-lg" alt="${p.nombre}">
            </div>

            <div class="col-md-6 px-lg-5">
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="home" class="text-secondary">Catálogo</a></li>
                        <li class="breadcrumb-item active text-info">${p.categoria}</li>
                    </ol>
                </nav>
                
                <h1 class="display-4 fw-bold mb-3">${p.nombre}</h1>
                <h2 class="text-neon mb-4">$${p.precio} COP</h2>
                
                <div class="mb-4">
                    <h5 class="text-secondary border-bottom border-secondary pb-2">Especificaciones Técnicas</h5>
                    <p class="lead">${p.descripcion}</p>
                </div>

                <div class="d-flex align-items-center mb-4">
                    <span class="badge ${p.stock > 0 ? 'bg-success' : 'bg-danger'} p-2 px-3">
                        ${p.stock > 0 ? 'Disponible en Cajicá' : 'Agotado'}
                    </span>
                    <span class="ms-3 text-secondary">${p.stock} unidades en bodega</span>
                </div>

                <c:if test="${p.stock > 0}">
                    <a href="carrito?accion=agregar&id=${p.id}" class="btn btn-info btn-lg w-100 fw-bold py-3 shadow-sm">
                        <i class="bi bi-cart-plus"></i> AGREGAR AL CARRITO
                    </a>
                </c:if>
                
                <a href="home" class="btn btn-outline-secondary w-100 mt-3">VOLVER ATRÁS</a>
            </div>
        </div>
    </div>
</body>
</html>