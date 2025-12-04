@'<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cerrando sesión...</title>
</head>
<body>
    <%
        // Invalidar sesión
        if (session != null) {
            session.invalidate();
        }
        
        // Redirigir al login
        response.sendRedirect("login.jsp?msg=logout_ok");
    %>
</body>
</html>
'@