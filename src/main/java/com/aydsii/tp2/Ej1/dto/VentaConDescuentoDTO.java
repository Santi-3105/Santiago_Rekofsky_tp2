package com.aydsii.tp2.Ej1.dto;

public class VentaConDescuentoDTO {

    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double montoConDescuento;

    public VentaConDescuentoDTO() {
    }

    public VentaConDescuentoDTO(VentaDTO venta, double porcentaje) {
        this.producto = venta.getProducto();
        this.cantidad = venta.getCantidad();
        this.precioUnitario = venta.getPrecioUnitario();
        this.montoConDescuento = venta.calcularImporte() * (1 - porcentaje / 100.0);
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getMontoConDescuento() {
        return montoConDescuento;
    }

    public void setMontoConDescuento(double montoConDescuento) {
        this.montoConDescuento = montoConDescuento;
    }
}