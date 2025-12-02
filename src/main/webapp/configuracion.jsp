<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registro - HypnoElectronic</title>
    <style>
        body { font-family: Arial; padding: 50px; background: #f5f5f5; }
        .container { max-width: 500px; margin: auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input, select { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ddd; border-radius: 5px; }
        button { background: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; width: 100%; }
        .success { color: green; margin: 10px 0; }
    </style>
</head>
<body>
    <div class="container">
        <h2>Registro de Usuario</h2>
        
        <% if (request.getParameter("success") != null) { %>
            <div class="success">¡Registro exitoso! Ahora puedes iniciar sesión.</div>
        <% } %>
        
        <form action="register" method="post">
            <input type="text" name="fullName" placeholder="Nombre completo" required>
            <input type="email" name="email" placeholder="Correo electrónico" required>
            <input type="text" name="username" placeholder="Usuario" required>
            <input type="password" name="password" placeholder="Contraseña" required>
            
            <select name="userType">
                <option value="patient">Paciente</option>
                <option value="doctor">Doctor</option>
            </select>
            
            <button type="submit">Registrarse</button>
        </form>
        
        <p style="margin-top: 20px; text-align: center;">
            <a href="login.jsp">¿Ya tienes cuenta? Inicia sesión</a>
        </p>
    </div>
</body>
</html>
