package com.hypnoelectronic.filtro;

import com.hypnoelectronic.model.Usuario;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    // Rutas que requieren que el usuario sea ADMIN
    private static final List<String> ADMIN_ROUTES = Arrays.asList(
        "/dashboard.jsp", "/usuarios-admin", "/admin-inventario", "/reporte-ventas", "/usuarios.jsp", "/inventario.jsp", "/api/users"
    );

    // Rutas que requieren estar LOGUEADO (cualquier rol)
    private static final List<String> PRIVATE_ROUTES = Arrays.asList(
        "/configuracion.jsp", "/update-profile", "/checkout", "/confirmar-pedido"
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String path = request.getServletPath();

        Usuario user = (session != null) ? (Usuario) session.getAttribute("user") : null;

        // 1. Validar rutas de Administrador
        if (ADMIN_ROUTES.contains(path)) {
            if (user == null || !"admin".equalsIgnoreCase(user.getUserType())) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=unauthorized");
                return;
            }
        }

        // 2. Validar rutas privadas generales
        if (PRIVATE_ROUTES.contains(path)) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=login_required");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig filterConfig) throws ServletException {}
    @Override public void destroy() {}
}