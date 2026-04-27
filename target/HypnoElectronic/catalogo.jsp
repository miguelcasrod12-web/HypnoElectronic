<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; }
        .product-card { 
            transition: transform 0.3s; 
            border: 1px solid #333; 
            border-radius: 15px; 
            overflow: hidden;
            background-color: var(--card-bg) !important;
        }
        .product-card:hover { 
            transform: translateY(-10px); 
            box-shadow: 0 0 20px var(--neon-blue); 
            border-color: var(--neon-blue);
        }
        /* Estilos para la Vista de Lista */
        .product-card-list {
            display: flex;
            flex-direction: row;
            background-color: var(--card-bg);
            border: 1px solid #333;
            border-radius: 15px;
            overflow: hidden;
            transition: 0.3s;
        }
        .product-card-list:hover {
            box-shadow: 0 0 20px var(--neon-blue);
            border-color: var(--neon-blue);
        }
        .product-card-list .img-wrapper {
            width: 250px;
            min-width: 250px;
        }
        /* Estilos para la Vista de Lista */
        .product-card-list {
            display: flex;
            flex-direction: row;
            background-color: var(--card-bg);
            border: 1px solid #333;
            border-radius: 15px;
            overflow: hidden;
            transition: 0.3s;
        }
        .product-card-list:hover {
            box-shadow: 0 0 20px var(--neon-blue);
            border-color: var(--neon-blue);
        }
        .product-card-list .img-wrapper {
            width: 250px;
            min-width: 250px;
        }
        .hero-section { 
            background: linear-gradient(rgba(0,0,0,0.7), rgba(0,0,0,0.7)), url('https://images.unsplash.com/photo-1547082299-de196ea013d6?w=1200'); 
            background-size: cover; 
            background-position: center;
            height: 350px; 
            color: white; 
            display: flex; 
            align-items: center; 
            justify-content: center;
            border-bottom: 1px solid #333;
        }
        .text-neon { color: var(--neon-blue); }
    </style>
</head>
<body>
    
    <%@ include file="includes/navbar.jsp" %>

    <header class="hero-section text-center">
        <div>
            <h1 class="display-3 fw-bold">Potencia tu <span class="text-neon">Setup</span></h1>
            <p class="lead">Los mejores periféricos para gamers y profesionales en Cajicá.</p>
        </div>
    </header>

<div class="container my-5">
    <div class="d-flex justify-content-between align-items-center mb-5">
        <h2 class="fw-bold text-white mb-0">Nuestro Catálogo</h2>
        
        <%-- TOOLBAR DE CONTROL --%>
        <div class="d-flex gap-2">
            <button class="btn btn-outline-info" type="button" data-bs-toggle="collapse" data-bs-target="#filterPanel">
                <i class="bi bi-funnel"></i> FILTRAR
            </button>
            <div class="btn-group">
                <a href="?view=grid&categoria=${param.categoria}&orden=${param.orden}&buscar=${param.buscar}" 
                   class="btn btn-outline-light ${empty param.view or param.view == 'grid' ? 'active' : ''}">
                   <i class="bi bi-grid-3x3-gap"></i>
                </a>
                <a href="?view=list&categoria=${param.categoria}&orden=${param.orden}&buscar=${param.buscar}" 
                   class="btn btn-outline-light ${param.view == 'list' ? 'active' : ''}">
                   <i class="bi bi-list-task"></i>
                </a>
            </div>
        </div>
    </div>

    <%-- PANEL DE FILTROS COLAPSABLE --%>
    <div class="collapse mb-4" id="filterPanel">
        <form action="home" method="GET" class="card p-4 bg-dark border-secondary shadow">
            <input type="hidden" name="view" value="${param.view}">
            <div class="row g-3 align-items-end">
                <div class="col-md-5">
                    <label class="form-label text-secondary small">Categoría</label>
                    <select name="categoria" class="form-select bg-black text-white border-secondary">
                        <option value="">Todas las categorías</option>
                        <c:forEach var="cat" items="${categorias}">
                            <option value="${cat.id}" ${param.categoria == cat.id ? 'selected' : ''}>${cat.nombre}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-5">
                    <label class="form-label text-secondary small">Ordenar por</label>
                    <select name="orden" class="form-select bg-black text-white border-secondary">
                        <option value="precio_asc" ${param.orden == 'precio_asc' ? 'selected' : ''}>Precio: Menor a Mayor</option>
                        <option value="precio_desc" ${param.orden == 'precio_desc' ? 'selected' : ''}>Precio: Mayor a Menor</option>
                        <option value="nombre" ${param.orden == 'nombre' ? 'selected' : ''}>Nombre (A-Z)</option>
                        <option value="stock" ${param.orden == 'stock' ? 'selected' : ''}>Disponibilidad (Stock)</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-neon w-100 fw-bold">APLICAR</button>
                </div>
            </div>
        </form>
    </div>

    <div class="row g-4">
        <c:forEach var="prod" items="${productos}">
            <c:choose>
                <%-- VISTA DE LISTA --%>
                <c:when test="${param.view == 'list'}">
                    <div class="col-12" id="prod-${prod.id}">
                        <div class="product-card-list shadow-sm text-white">
                            <div class="img-wrapper">
                                <a href="producto-detalle?id=${prod.id}">
                                    <img src="${prod.imagen_url}" class="img-fluid" alt="${prod.nombre}" 
                                         style="aspect-ratio: 1 / 1; width: 100%; object-fit: contain; background-color: #ffffff; padding: 20px;" loading="lazy">
                                </a>
                            </div>
                            <div class="card-body d-flex flex-column p-4">
                                <div class="d-flex justify-content-between align-items-start">
                                    <div>
                                        <span class="badge mb-2" style="background-color: var(--neon-blue); color: black;">${prod.categoria}</span>
                                        <a href="producto-detalle?id=${prod.id}" class="text-decoration-none">
                                            <h3 class="card-title fw-bold text-white hover-neon">${prod.nombre}</h3>
                                        </a>
                                    </div>
                                    <h3 class="text-neon fw-bold mb-0">$${prod.precio} COP</h3>
                                </div>
                                <p class="card-text text-secondary mt-2">${prod.descripcion}</p>
                                <div class="mt-auto d-flex justify-content-between align-items-center">
                                    <p class="small mb-0 ${prod.stock < 5 ? 'text-warning' : 'text-muted'}">
                                        <i class="bi bi-box-seam"></i> Disponibilidad: ${prod.stock} unidades
                                    </p>
                                    <div class="d-flex gap-2">
                                        <a href="producto-detalle?id=${prod.id}" class="btn btn-outline-secondary">DETALLES</a>
                                        <a href="carrito?accion=agregar&id=${prod.id}" class="btn btn-neon px-4 fw-bold">AÑADIR AL CARRITO</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:when>
                
                <%-- VISTA DE CUADRÍCULA (DEFAULT) --%>
                <c:otherwise>
                    <div class="col-md-4" id="prod-${prod.id}"> 
                        <div class="card product-card h-100 shadow-sm text-white">
                            <a href="producto-detalle?id=${prod.id}">
                                <img src="${prod.imagen_url}" class="card-img-top" alt="${prod.nombre}" 
                                     style="aspect-ratio: 1 / 1; width: 100%; object-fit: contain; background-color: #ffffff; padding: 15px;" loading="lazy">
                            </a>
                            <div class="card-body d-flex flex-column">
                                <span class="badge mb-2 w-auto align-self-start" style="background-color: var(--neon-blue); color: black;">${prod.categoria}</span>
                                <a href="producto-detalle?id=${prod.id}" class="text-decoration-none">
                                    <h5 class="card-title fw-bold text-white hover-neon">${prod.nombre}</h5>
                                </a>
                                <p class="card-text text-secondary small">${prod.descripcion}</p>
                                <div class="mt-auto">
                                    <h4 class="text-neon fw-bold">$${prod.precio} COP</h4>
                                    <p class="small ${prod.stock < 5 ? 'text-warning' : 'text-muted'}"><i class="bi bi-box-seam"></i> Stock: ${prod.stock} unidades</p>
                                    <div class="d-grid gap-2">
                                        <a href="carrito?accion=agregar&id=${prod.id}#prod-${prod.id}" class="btn btn-neon fw-bold"><i class="bi bi-cart-plus"></i> AÑADIR</a>
                                        <a href="producto-detalle?id=${prod.id}" class="btn btn-outline-secondary btn-sm">VER DETALLES</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <%-- ... (Mensaje de inventario vacío igual) ... --%>

    </div>
</div>

<%@ include file="includes/footer.jsp" %>
<style>
    /* Forzamos la variable por si el navbar no la cargó a tiempo */
    :root {
        --neon-blue: #00f3ff;
    }

    .hover-neon:hover {
        color: var(--neon-blue) !important;
        transition: 0.3s;
    }

    /* Ajuste real para el botón */
    .btn-neon { 
        background-color: var(--neon-blue) !important; 
        color: #000 !important; /* Texto negro para que resalte */
        border: none;
        transition: 0.3s;
    }

    .btn-neon:hover { 
        background-color: #ffffff !important; 
        color: #000 !important;
        box-shadow: 0 0 20px #ffffff;
    }

    /* Estilo para el botón de detalles para que no se pierda */
    .btn-outline-secondary {
        border-color: #333;
        color: #aaa;
    }
    
    .btn-outline-secondary:hover {
        background-color: #333;
        color: #fff;
    }
</style>

    <%@ include file="includes/footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>