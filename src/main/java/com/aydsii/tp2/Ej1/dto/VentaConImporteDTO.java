package com.aydsii.tp2.Ej1.dto;

public class VentaConImporteDTO {

    private String producto;
    private int cantidad;
    private double precioUnitario;
    private double importe;

    public VentaConImporteDTO() {
    }

    public VentaConImporteDTO(VentaDTO venta) {
        this.producto = venta.getProducto();
        this.cantidad = venta.getCantidad();
        this.precioUnitario = venta.getPrecioUnitario();
        this.importe = venta.calcularImporte();
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

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
}