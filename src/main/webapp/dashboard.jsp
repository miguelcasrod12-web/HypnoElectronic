<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Verificar si el usuario está logueado
    if(session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - HypnoElectronic</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f4f4f4; }
        .header { background: white; padding: 20px; border-radius: 5px; margin-bottom: 20px; }
        .welcome { color: #333; }
        .logout-btn { background: #dc3545; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
        .logout-btn:hover { background: #c82333; }
    </style>
    <style>
        .logout-btn {
            display: inline-block;
            padding: 8px 16px;
            background: #dc3545;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            font-size: 14px;
            margin-top: 10px;
        }
        
        .logout-btn:hover {
            background: #c82333;
        }
    </style>
</head>
<body>
    <div class="header">
        <h1 class="welcome">Bienvenido, <%= session.getAttribute("username") %>!</h1>
        <p>Sistema de Gestión HypnoElectronic</p>
        <a href="logout.jsp" class="logout-btn">Cerrar Sesión</a>
    </div>
    
    <div class="content">
        <h2>Módulos del Sistema</h2>
        <ul>
            <li>Gestión de Usuarios</li>
            <li>Control de Inventario</li>
            <li>Reportes y Estadísticas</li>
            <li>Configuración del Sistema</li>
        </ul>
    </div>
<div class='enlace-navegacion'><a href='logout.jsp'>Cerrar sesión</a> | <a href='register.jsp'>Registrar usuario</a></div>
</body>
</html>

