package com.hypnoelectronic.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Pedido {
    private int id;
    private int usuarioId;
    private Timestamp fechaPedido;
    private BigDecimal total;
    private String estado; // 'pendiente', 'pagado', 'enviado', 'cancelado'
    private List<DetallePedido> detalles; // Para cargar los ítems del pedido

    public Pedido() {}

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }
}