package com.hypnoelectronic.model;

import java.io.Serializable;

public class Proveedor implements Serializable {
    private int id;
    private String nombre;
    private String nit;
    private String contacto;
    private String email;

    public Proveedor() {}

    public Proveedor(int id, String nombre, String nit, String contacto, String email) {
        this.id = id;
        this.nombre = nombre;
        this.nit = nit;
        this.contacto = contacto;
        this.email = email;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getContacto() { return contacto; }
    public void setContacto(String contacto) { this.contacto = contacto; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}