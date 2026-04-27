<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hypnoelectronic.model.Usuario" %>
<%
    // Recuperamos el usuario de la sesión (usando la misma llave "user" del Servlet)
    Usuario usuarioSesion = (Usuario) session.getAttribute("user");
%>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<style>
    :root {
        --neon-blue: #00d4ff;
        --dark-bg: #121212;
        --card-bg: #1e1e1e;
    }
    
    body { 
        background-color: var(--dark-bg); 
        color: white; 
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        display: flex;
        flex-direction: column;
        min-height: 100vh;
    }

    label.form-label, .text-secondary {
        color: #adb5bd !important;
    }

    .navbar { 
        background-color: #000 !important; 
        border-bottom: 2px solid var(--neon-blue); 
    }
    
    .nav-link { color: white !important; }
    
    .nav-link:hover { 
        color: var(--neon-blue) !important; 
        text-shadow: 0 0 8px var(--neon-blue);
    }

    .btn-neon { 
        border: 1px solid var(--neon-blue); 
        color: var(--neon-blue) !important;
        transition: 0.3s;
        text-transform: uppercase;
        letter-spacing: 1px;
    }

    .btn-neon:hover { 
        background: var(--neon-blue); 
        color: black !important; 
        box-shadow: 0 0 15px var(--neon-blue); 
    }
    
    .user-info {
        color: white;
        margin-right: 15px;
        font-size: 0.9rem;
    }

    /* Ajuste para que el carrito no se mueva */
    .cart-container {
        display: flex;
        align-items: center;
        justify-content: center;
        min-width: 45px;
    }

    .dropdown-menu {
        margin-top: 10px;
        box-shadow: 0 5px 15px rgba(0,0,0,0.5);
    }
</style>

<nav class="navbar navbar-expand-lg navbar-dark mb-4 sticky-top">
  <div class="container">
    <a class="navbar-brand fw-bold" href="home" style="color: var(--neon-blue);">HYPNO <span class="text-white">ELECTRONIC</span></a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">

      <%-- Barra de Búsqueda Global --%>
      <form class="d-flex mx-lg-auto my-2 my-lg-0 w-50" action="home" method="GET">
          <div class="input-group">
              <input class="form-control bg-dark text-white border-secondary" type="search" name="buscar" placeholder="Buscar periféricos..." aria-label="Search" value="${param.buscar}">
              <button class="btn btn-outline-info" type="submit"><i class="bi bi-search"></i></button>
          </div>
      </form>

      <ul class="navbar-nav ms-auto align-items-center">
        
        <%-- Carrito con Badge Dinámico --%>
        <li class="nav-item cart-container">
            <a class="nav-link position-relative px-2" href="carrito.jsp" style="color: var(--neon-blue);">
                <i class="bi bi-cart3" style="font-size: 1.4rem;"></i>
                <c:if test="${not empty sessionScope.listaCarrito and sessionScope.listaCarrito.size() > 0}">
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" style="font-size: 0.6rem;">
                        ${sessionScope.listaCarrito.size()}
                    </span>
                </c:if>
            </a>
        </li>
        
        <% if (usuarioSesion == null) { %>
            <li class="nav-item">
                <a class="nav-link btn btn-neon ms-lg-3" href="login.jsp">Iniciar Sesión</a>
            </li>
        <% } else { %>
            <li class="nav-item dropdown ms-lg-3">
                <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown">
                    Hola, <strong style="color: var(--neon-blue);"><%= usuarioSesion.getFullName() %></strong>
                </a>
                <ul class="dropdown-menu dropdown-menu-end bg-dark border-secondary">
                    <li><a class="dropdown-item text-white" href="configuracion.jsp">Mi Perfil</a></li>
                    <li><a class="dropdown-item text-white" href="mis-compras">Mis Compras</a></li>
                    <% if ("admin".equalsIgnoreCase(usuarioSesion.getUserType())) { %>
                        <li><hr class="dropdown-divider border-secondary"></li>
                        <li><a class="dropdown-item text-warning" href="dashboard.jsp">Panel Admin</a></li>
                    <% } %>
                    <li><hr class="dropdown-divider border-secondary"></li>
                    <li><a class="dropdown-item text-danger" href="logout.jsp">Cerrar Sesión</a></li>
                </ul>
            </li>
        <% } %>
      </ul>
    </div>
  </div>
</nav>