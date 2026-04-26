<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // 1. Invalidar la sesión actual para borrar los datos del usuario en memoria
    if (session != null) {
        session.invalidate();
    }
    
    // 2. Redirigir al login enviando el parámetro exacto que espera nuestro login.jsp
    // Usamos "message=logout_success" para que coincida con la validación del archivo anterior.
    response.sendRedirect("login.jsp?message=logout_success");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cerrando sesión - HypnoElectronic</title>
    <style>
        body { background-color: #121212; color: #00d4ff; font-family: Arial; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
    </style>
</head>
<body>
    <p>Saliendo del sistema de forma segura...</p>
</body>
</html>