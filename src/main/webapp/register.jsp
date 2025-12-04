<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro - HypnoElectronic</title>
    <style>
        body { font-family: Arial; background: #f4f4f4; padding: 20px; }
        .container { max-width: 400px; margin: auto; background: white; padding: 20px; border-radius: 5px; }
        .alert { padding: 10px; margin: 10px 0; border-radius: 3px; }
        .alert-danger { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .alert-success { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        input, select { width: 100%; padding: 8px; margin: 8px 0; }
        button { background: #007bff; color: white; padding: 10px; border: none; width: 100%; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Registro de Usuario</h2>
        
        <%-- SOLAMENTE AQUÍ SE DECLARA errorMessage --%>
        <%
            String errorMessage = (String) request.getAttribute("errorMessage");
            String success = request.getParameter("success");
            
            if (errorMessage != null) {
        %>
            <div class="alert alert-danger">
                <strong>Error:</strong> <%= errorMessage %>
            </div>
        <%
            }
            
            if (success != null && success.equals("true")) {
        %>
            <div class="alert alert-success">
                <strong>¡Éxito!</strong> Usuario registrado correctamente. 
                <a href="login.jsp">Iniciar sesión</a>
            </div>
        <%
            }
        %>
        
        <form action="register" method="post">
            <div>
                <label>Nombre completo:</label>
                <input type="text" name="fullName" required>
            </div>
            
            <div>
                <label>Email:</label>
                <input type="email" name="email" required>
            </div>
            
            <div>
                <label>Nombre de usuario:</label>
                <input type="text" name="username" required>
            </div>
            
            <div>
                <label>Contraseña:</label>
                <input type="password" name="password" required>
            </div>
            
            <div>
                <label>Tipo de usuario:</label>
                <select name="userType">
                    <option value="patient">Cliente</option>
                    <option value="admin">Administrador</option>
                </select>
            </div>
            
            <button type="submit">Registrarse</button>
        </form>
        
        <p style="text-align: center; margin-top: 15px;">
            ¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a>
        </p>
    </div>
</body>
</html>