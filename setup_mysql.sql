-- RESET DE BASE DE DATOS HYPNOELECTRONIC
CREATE DATABASE IF NOT EXISTS hypnoelectronic;
USE hypnoelectronic;

-- 1. Tablas de Configuración y Roles
CREATE TABLE IF NOT EXISTS roles (
    idrole INT PRIMARY KEY,
    nombre_role VARCHAR(45) NOT NULL
);
INSERT IGNORE INTO roles (idrole, nombre_role) VALUES (1, 'admin'), (2, 'cliente');

CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS proveedores (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20) UNIQUE,
    contacto VARCHAR(50),
    email VARCHAR(100)
);

-- 2. Gestión de Usuarios y Seguridad
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255), -- Espacio suficiente para el hash BCrypt
    role_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(idrole)
);

-- 3. Catálogo y Control de Inventario
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(12,2) NOT NULL,
    stock_actual INT DEFAULT 0,
    imagen_url VARCHAR(255),
    categoria_id INT,
    proveedor_id INT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria),
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id_proveedor)
);

-- Historial de entradas (Ingreso de material)
CREATE TABLE entradas_almacen (
    id_entrada INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT,
    cantidad INT NOT NULL,
    precio_compra DECIMAL(12,2),
    fecha_ingreso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES productos(id_producto)
);

-- 4. Ventas y Experiencia de Usuario
CREATE TABLE pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(12,2) NOT NULL,
    estado ENUM('pendiente', 'pagado', 'enviado', 'cancelado') DEFAULT 'pendiente',
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);

CREATE TABLE detalle_pedidos (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id_pedido),
    FOREIGN KEY (producto_id) REFERENCES productos(id_producto)
);

-- 5. Datos Iniciales Profesionales
INSERT INTO usuarios (nombre_completo, email, username, password, role_id) VALUES 
('Administrador General', 'admin@hypno.com', 'admin', '$2a$10$8.UnS3YxK8P4S75eQ/j/u.eK7H1vD2.fI/D1l9u3K3W/Z6gX.vS6q', 1),
('Cliente de Prueba', 'cliente@gmail.com', 'cliente', '$2a$10$9.lYkM7zM6W/B5u9/C.m.3YyZ0lFm6R3nB.r6g1HhG7YvO6S9C6e', 2);

INSERT INTO categorias (nombre, descripcion) VALUES ('Teclados', 'Mecánicos y Membrana'), ('Mouses', 'Gaming de alta precisión');