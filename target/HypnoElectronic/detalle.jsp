<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>${p.nombre} - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .product-img { width: 100%; max-height: 500px; aspect-ratio: 1 / 1; object-fit: contain; border-radius: 15px; background: #ffffff; padding: 20px; }
        .text-neon { color: var(--neon-blue); text-shadow: 0 0 10px var(--neon-blue); }
        .card-detalle { background: #121212; border: 1px solid #333; border-radius: 20px; padding: 30px; }
        
        .recom-card { 
            background-color: var(--card-bg); 
            border: 1px solid #333; 
            transition: 0.3s; 
            border-radius: 12px; 
            overflow: hidden;
        }
        .recom-card:hover { 
            transform: translateY(-5px); 
            border-color: var(--neon-blue); 
            box-shadow: 0 0 15px rgba(0, 212, 255, 0.3); 
        }
        .recom-img { aspect-ratio: 1 / 1; object-fit: contain; background: #ffffff; padding: 10px; width: 100%; }
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

    <!-- PRODUCTOS RECOMENDADOS (Misma Categoría) -->
    <div class="container mt-5 pt-5 border-top border-secondary">
        <h3 class="text-neon mb-4"><i class="bi bi-stars"></i> Periféricos Similares</h3>
        
        <div class="row g-4">
            <c:set var="recCount" value="0" />
            <c:forEach var="rp" items="${productos}">
                <c:if test="${rp.categoriaId == p.categoriaId && rp.id != p.id && recCount < 4}">
                    <div class="col-6 col-md-3">
                        <div class="card recom-card h-100 text-white">
                            <a href="producto-detalle?id=${rp.id}">
                                <img src="${rp.imagen_url}" class="recom-img" alt="${rp.nombre}">
                            </a>
                            <div class="card-body p-3 d-flex flex-column">
                                <h6 class="card-title text-truncate mb-1">${rp.nombre}</h6>
                                <p class="text-neon fw-bold mb-2 small">$${rp.precio}</p>
                                <a href="producto-detalle?id=${rp.id}" class="btn btn-outline-info btn-sm mt-auto">VER</a>
                            </div>
                        </div>
                    </div>
                    <c:set var="recCount" value="${recCount + 1}" />
                </c:if>
            </c:forEach>
            <c:if test="${recCount == 0 && not empty productos}">
                <p class="text-secondary italic ps-3">No hay más productos en esta categoría por ahora.</p>
            </c:if>
        </div>
    </div>

    <!-- MÁS PRODUCTOS SEPARADOS POR CATEGORÍAS -->
    <div class="container mt-5 mb-5 pt-4">
        <h3 class="text-white mb-4">Explora otras categorías</h3>
        <c:forEach var="cat" items="${categorias}">
            <%-- Solo mostramos categorías diferentes a la actual --%>
            <c:if test="${cat.id != p.categoriaId}">
                <div class="mb-5">
                    <h5 class="text-secondary border-bottom border-dark pb-2 mb-3 text-uppercase small letter-spacing-1">
                        ${cat.nombre}
                    </h5>
                    <div class="row g-3">
                        <c:set var="catCount" value="0" />
                        <c:forEach var="cp" items="${productos}">
                            <c:if test="${cp.categoriaId == cat.id && catCount < 4}">
                                <div class="col-6 col-md-3">
                                    <div class="card recom-card h-100 text-white border-dark">
                                        <a href="producto-detalle?id=${cp.id}" class="text-decoration-none">
                                            <img src="${cp.imagen_url}" class="recom-img" alt="${cp.nombre}" style="opacity: 0.85;">
                                        </a>
                                        <div class="card-body p-2 text-center">
                                            <p class="small mb-0 text-truncate text-secondary">${cp.nombre}</p>
                                        </div>
                                    </div>
                                </div>
                                <c:set var="catCount" value="${catCount + 1}" />
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>

    <!-- PRODUCTOS EN GENERAL (Descubrir más) -->
    <div class="container mt-5 mb-5 pt-4">
        <h3 class="text-info mb-4">Descubre otros periféricos</h3>
        <div class="row g-3">
            <c:set var="genCount" value="0" />
            <c:forEach var="gp" items="${productos}">
                <%-- Mostramos productos que no sean el actual y que no sean de su misma categoría para variar --%>
                <c:if test="${gp.id != p.id && gp.categoriaId != p.categoriaId && genCount < 8}">
                    <div class="col-6 col-md-3">
                        <div class="card recom-card h-100 text-white border-dark">
                            <a href="producto-detalle?id=${gp.id}" class="text-decoration-none">
                                <img src="${gp.imagen_url}" class="recom-img" alt="${gp.nombre}" style="opacity: 0.9;">
                            </a>
                            <div class="card-body p-2 text-center">
                                <p class="small mb-0 text-truncate text-secondary">${gp.nombre}</p>
                                <p class="text-neon fw-bold mb-0 small">$${gp.precio}</p>
                            </div>
                        </div>
                    </div>
                    <c:set var="genCount" value="${genCount + 1}" />
                </c:if>
            </c:forEach>
            <c:if test="${recCount == 0}">
                <div class="col-12 text-secondary small italic">No hay otros productos en esta categoría por ahora.</div>
            </c:if>
        </div>
    </div>

    <%@ include file="includes/footer.jsp" %>
</body>
</html>