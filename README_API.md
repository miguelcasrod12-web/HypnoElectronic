$docContent = @'
# API REST - HypnoElectronic System

## Información General
- **Base URL:** `http://localhost:8080/HypnoElectronic_API_AA5/api/`
- **Formato de Respuesta:** JSON
- **Autenticación:** Token en sesión

## Endpoints Disponibles

### 🔐 Autenticación
| Método | Endpoint | Descripción | Body (JSON) |
|--------|----------|-------------|-------------|
| POST | `/auth/login` | Iniciar sesión | `{"email":"usuario@ejemplo.com","password":"123456"}` |
| POST | `/auth/register` | Registrar usuario | `{"nombre":"Juan","email":"juan@ejemplo.com","password":"123456"}` |
| POST | `/auth/logout` | Cerrar sesión | - |
| GET | `/auth/check` | Verificar sesión | - |

### 📦 Productos
| Método | Endpoint | Descripción | Body (JSON) |
|--------|----------|-------------|-------------|
| GET | `/products` | Listar todos los productos | - |
| GET | `/products/{id}` | Obtener producto por ID | - |
| POST | `/products` | Crear nuevo producto | `{"nombre":"Producto","descripcion":"Descripción","precio":100.0,"stock":50}` |
| PUT | `/products/{id}` | Actualizar producto | `{"nombre":"Producto Actualizado","precio":120.0}` |
| DELETE | `/products/{id}` | Eliminar producto | - |

### 👥 Usuarios
| Método | Endpoint | Descripción | Body (JSON) |
|--------|----------|-------------|-------------|
| GET | `/users` | Listar todos los usuarios | - |
| GET | `/users/{id}` | Obtener usuario por ID | - |
| POST | `/users` | Crear nuevo usuario | `{"nombre":"Maria","email":"maria@ejemplo.com","password":"123456"}` |
| PUT | `/users/{id}` | Actualizar usuario | `{"nombre":"Maria Actualizada","email":"maria.nueva@ejemplo.com"}` |
| DELETE | `/users/{id}` | Eliminar usuario | - |

## Modelos de Datos

### Producto
```json
{
  "id": 1,
  "nombre": "Laptop Gamer",
  "descripcion": "Laptop para gaming de alta gama",
  "precio": 1500.00,
  "stock": 10,
  "fecha_creacion": "2024-12-04"
}