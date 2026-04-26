package com.hypnoelectronic.model;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private String imagen_url; // La pieza que faltaba
    private String categoria; // Se mantiene por compatibilidad (se llenará con categoriaNombre)
    private String categoriaNombre;
    private int categoriaId;
    private int proveedorId;
    private String proveedorNombre;
    private double precio;
    private int stock;

    // Constructores
    public Producto() {}
    
    public Producto(String nombre, String descripcion, double precio, int stock) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    public Producto(int id, String nombre, String descripcion, String imagen_url, int categoriaId, String categoriaNombre, int proveedorId, String proveedorNombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen_url = imagen_url;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.proveedorId = proveedorId;
        this.proveedorNombre = proveedorNombre;
        this.precio = precio;
        this.stock = stock;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen_url() { return imagen_url; }
    public void setImagen_url(String imagen_url) { this.imagen_url = imagen_url; }

    public int getCategoriaId() { return categoriaId; }
    public void setCategoriaId(int categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    public int getProveedorId() { return proveedorId; }
    public void setProveedorId(int proveedorId) { this.proveedorId = proveedorId; }

    public String getProveedorNombre() { return proveedorNombre; }
    public void setProveedorNombre(String proveedorNombre) { this.proveedorNombre = proveedorNombre; }

    // Mantener getCategoria para compatibilidad temporal si es necesario, pero usar getCategoriaNombre
    public String getCategoria() { return categoriaNombre; }
    
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    @Override
    public String toString() {
        return "Producto [id=" + id + ", nombre=" + nombre + ", categoria=" + categoriaNombre + ", precio=" + precio + "]";
    }
}