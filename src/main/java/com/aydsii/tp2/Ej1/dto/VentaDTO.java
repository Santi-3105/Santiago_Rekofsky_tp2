package com.aydsii.tp2.Ej1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class VentaDTO {
    //No este vacio
    @NotBlank(message = "El nombre del producto no puede estar vacio")
    private String producto;
    //Sea positivo
    @Positive(message = "La cantidad debe ser un numero entero positivo, mayor a 0")
    private int cantidad;
    //Sea positivo
    @Positive(message = "El precio unitario debe ser positivo, mayor a 0")
    private double precioUnitario;

    public VentaDTO() {
    }

    public VentaDTO(String producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double calcularImporte() {
        return cantidad * precioUnitario;
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
}