<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Solo redirigimos a home si entramos directamente a la raíz
    // Si el Servlet ya nos mandó aquí, no hacemos nada para evitar bucles.
    if (request.getAttribute("productos") == null) response.sendRedirect("home");
%>