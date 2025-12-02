-- ============================================
-- SCRIPT SIMPLIFICADO - HypnoElectronic
-- ============================================

-- Eliminar si existe
DROP DATABASE IF EXISTS HypnoElectronic;

-- Crear base de datos
CREATE DATABASE HypnoElectronic;
USE HypnoElectronic;

-- Tabla usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    fullName VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) DEFAULT 0.00,
    stock INT DEFAULT 0,
    categoria VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de prueba
INSERT INTO usuarios (username, password, fullName, email) VALUES
('admin', 'admin123', 'Administrador Principal', 'admin@hypno.com'),
('usuario1', 'pass123', 'Usuario de Prueba', 'usuario1@hypno.com'),
('miguel', 'mypass123', 'Miguel Castro', 'miguel@example.com');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES
('Laptop HP', 'Laptop de 15 pulgadas, 8GB RAM, 256GB SSD', 899.99, 15, 'Electr?nica'),
('Mouse Logitech', 'Mouse inal?mbrico, 3 botones', 29.99, 50, 'Accesorios'),
('Teclado Mec?nico', 'Teclado RGB mec?nico para gaming', 79.99, 25, 'Accesorios'),
('Monitor 24"', 'Monitor Full HD, 75Hz, IPS', 199.99, 10, 'Monitores'),
('Auriculares', 'Auriculares con cancelaci?n de ruido', 149.99, 30, 'Audio');

-- Verificar inserci?n
SELECT '=== USUARIOS ===' AS '';
SELECT * FROM usuarios;

SELECT '=== PRODUCTOS ===' AS '';
SELECT * FROM productos;

SELECT '? Base de datos creada exitosamente' AS Mensaje_Final;
