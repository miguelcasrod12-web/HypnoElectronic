<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Categoría - HypnoElectronic</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-dark text-white">
    <%@ include file="includes/navbar.jsp" %>

    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card p-4 shadow-lg" style="background-color: var(--card-bg); border: 1px solid var(--neon-blue);">
                    <h2 class="mb-4" style="color: var(--neon-blue);">Editar Categoría</h2>
                    
                    <form action="admin-categorias" method="post">
                        <input type="hidden" name="accion" value="actualizar">
                        <input type="hidden" name="id" value="${categoria.id}">

                        <div class="mb-3">
                            <label class="form-label text-secondary small">Nombre de la Categoría</label>
                            <input type="text" name="nombre" class="form-control bg-dark text-white border-secondary" value="${categoria.nombre}" required>
                        </div>

                        <div class="mb-3">
                            <label class="form-label text-secondary small">Descripción</label>
                            <textarea name="descripcion" class="form-control bg-dark text-white border-secondary" rows="4">${categoria.descripcion}</textarea>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-neon w-100 fw-bold">GUARDAR CAMBIOS</button>
                            <a href="admin-categorias" class="btn btn-outline-secondary w-100 fw-bold">CANCELAR</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>