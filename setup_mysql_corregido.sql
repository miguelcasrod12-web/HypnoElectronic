-- ============================================
-- SCRIPT DE BASE DE DATOS - HypnoElectronic
-- ============================================

-- Eliminar base de datos existente si hay conflictos
DROP DATABASE IF EXISTS HypnoElectronic;
DROP DATABASE IF EXISTS hypnoelectronic;

-- Crear nueva base de datos
CREATE DATABASE HypnoElectronic;
USE HypnoElectronic;

-- ============================================
-- TABLA: usuarios
-- ============================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    fullName VARCHAR(100),
    email VARCHAR(100),
    userType VARCHAR(20) DEFAULT 'patient',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLA: productos
-- ============================================
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2),
    stock INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- DATOS DE PRUEBA
-- ============================================

-- Usuario administrador
INSERT INTO usuarios (username, password, fullName, email, userType) 
VALUES ('admin', 'admin123', 'Administrador', 'admin@hypno.com', 'admin');

-- Productos de ejemplo
INSERT INTO productos (nombre, descripcion, precio, stock) VALUES
('Laptop HP', 'Laptop 15 pulgadas, 8GB RAM, 256GB SSD', 1200.00, 10),
('Mouse Inalámbrico', 'Mouse óptico inalámbrico', 25.99, 50),
('Teclado Mecánico', 'Teclado mecánico RGB', 89.99, 20),
('Monitor 24"', 'Monitor Full HD 24 pulgadas', 199.99, 15);

-- ============================================
-- VERIFICACIÓN
-- ============================================
SELECT '=== TABLA USUARIOS ===' AS Verificacion;
SELECT * FROM usuarios;

SELECT '=== TABLA PRODUCTOS ===' AS Verificacion;
SELECT * FROM productos;

SELECT '=== CONTEOS ===' AS Verificacion;
SELECT 'Usuarios:' AS Tabla, COUNT(*) AS Total FROM usuarios
UNION
SELECT 'Productos:' AS Tabla, COUNT(*) AS Total FROM productos;
