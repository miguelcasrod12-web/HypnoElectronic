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

    <%-- ... (Cabecera y estilos igual) ... --%>

<div class="container my-5">
    <h2 class="text-center mb-5 fw-bold text-white">Nuestro Catálogo</h2>

    <div class="row mb-4 justify-content-center">
        <div class="col-md-10">
            <form action="home" method="GET" class="card p-3 bg-dark border-secondary shadow">
                <div class="row g-3 align-items-end">
                    <div class="col-md-4">
                        <label class="form-label text-secondary small">Categoría</label>
                        <select name="categoria" class="form-select bg-black text-white border-secondary">
                            <option value="">Todas las categorías</option>
                            <c:forEach var="cat" items="${categorias}">
                                <option value="${cat.id}" ${param.categoria == cat.id ? 'selected' : ''}>${cat.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label text-secondary small">Ordenar por</label>
                        <select name="orden" class="form-select bg-black text-white border-secondary">
                            <option value="precio_asc">Precio: Menor a Mayor</option>
                            <option value="precio_desc">Precio: Mayor a Menor</option>
                            <option value="nombre">Nombre (A-Z)</option>
                            <option value="stock">Disponibilidad (Stock)</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <button type="submit" class="btn btn-neon w-100 fw-bold">
                            <i class="bi bi-filter"></i> APLICAR FILTROS
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="row g-4">
        
        <c:forEach var="prod" items="${productos}">
            <div class="col-md-4" id="prod-${prod.id}"> 
                <div class="card product-card h-100 shadow-sm text-white">
                    
                    <a href="producto-detalle?id=${prod.id}">
                        <img src="${prod.imagen_url}" class="card-img-top" alt="${prod.nombre}" 
                             style="height: 220px; object-fit: cover; filter: brightness(0.8) contrast(1.2);" loading="lazy">
                    </a>

                    <div class="card-body d-flex flex-column">
                        <span class="badge mb-2 w-auto align-self-start" style="background-color: var(--neon-blue); color: black;">
                            ${prod.categoria}
                        </span>
                        
                        <a href="producto-detalle?id=${prod.id}" class="text-decoration-none">
                            <h5 class="card-title fw-bold text-white hover-neon">${prod.nombre}</h5>
                        </a>

                        <p class="card-text text-secondary small">${prod.descripcion}</p>
                        
                        <div class="mt-auto">
                            <h4 class="text-neon fw-bold">$${prod.precio} COP</h4>
                            
                            <p class="small ${prod.stock < 5 ? 'text-warning' : 'text-muted'}">
                                <i class="bi bi-box-seam"></i> Stock: ${prod.stock} unidades
                            </p>

                            <div class="d-grid gap-2">
                                <a href="carrito?accion=agregar&id=${prod.id}#prod-${prod.id}" class="btn btn-neon fw-bold">
                                    <i class="bi bi-cart-plus"></i> AÑADIR
                                </a>
                                <a href="producto-detalle?id=${prod.id}" class="btn btn-outline-secondary btn-sm">
                                    VER DETALLES
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>

        <%-- ... (Mensaje de inventario vacío igual) ... --%>

    </div>
</div>

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