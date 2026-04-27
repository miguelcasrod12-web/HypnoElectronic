package com.hypnoelectronic.model;

import java.io.Serializable;

/**
 * Registro técnico de los componentes individuales de una orden de venta.
 * Esta entidad vincula el inventario de HypnoElectronic con las transacciones maestras.
 * 
 * Autor: Miguel Castillo - HypnoElectronic
 */
public class DetallePedido implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pkDetalle;
    private int fkeyPedido;
    private int fkeyProducto;
    private String nombreDeProducto;
    private int cantidadVendida;
    private double precioAplicado;

    public DetallePedido() {}

    /**
     * Constructor de inicialización para procesos de venta.
     */
    public DetallePedido(int idProd, int cant, double pUnit) {
        this.fkeyProducto = idProd;
        this.cantidadVendida = cant;
        this.precioAplicado = pUnit;
    }

    public int getIdDetalle() {
        return this.pkDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.pkDetalle = idDetalle;
    }

    public int getProductoId() {
        return this.fkeyProducto;
    }

    public void setProductoId(int productoId) {
        this.fkeyProducto = productoId;
    }

    public int getPedidoId() {
        return this.fkeyPedido;
    }

    public void setPedidoId(int pedidoId) {
        this.fkeyPedido = pedidoId;
    }

    public String getNombreProducto() {
        return this.nombreDeProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreDeProducto = nombreProducto;
    }

    public int getCantidad() {
        return this.cantidadVendida;
    }

    public void setCantidad(int cantidad) {
        this.cantidadVendida = cantidad;
    }

    public double getPrecioUnitario() {
        return this.precioAplicado;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioAplicado = precioUnitario;
    }

    /**
     * Cálculo del subtotal para este renglón.
     */
    public double getSubtotal() {
        return this.precioAplicado * (double) this.cantidadVendida;
    }
}