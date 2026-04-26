<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Perfil - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card bg-dark border-info p-4 shadow-lg">
                    <h2 class="mb-4 text-info">Actualizar Colaborador</h2>
                    
                    <form action="actualizar-usuario-ejecutar" method="post">
                        <input type="hidden" name="id" value="${usuarioEdit.id}">

                        <div class="mb-3">
                            <label class="form-label text-secondary">Nombre Completo</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" value="${usuarioEdit.fullName}" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-secondary">Email</label>
                            <input type="email" name="email" class="form-control bg-dark text-white border-secondary" value="${usuarioEdit.email}" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-secondary">Username</label>
                            <input type="text" name="username" class="form-control bg-dark text-white border-secondary" value="${usuarioEdit.username}" required>
                        </div>

                        <div class="mb-4">
                            <label class="form-label text-secondary">Contraseña (Dejar igual si no se cambia)</label>
                            <input type="password" name="password" class="form-control bg-dark text-white border-secondary" value="${usuarioEdit.password}" required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-info w-100 fw-bold">GUARDAR CAMBIOS</button>
                            <a href="usuarios-admin" class="btn btn-outline-secondary w-100">VOLVER</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>