package com.aydsii.tp2.Ej1.dto;

public class EstadisticasVentasDTO {

    private double totalFacturado;
    private int cantidadVentas;
    private double ticketPromedio;
    private VentaConImporteDTO ventaMayor;
    private VentaConImporteDTO ventaMenor;
    private String productoMasVendido;

    public double getTotalFacturado() {
        return totalFacturado;
    }

    public void setTotalFacturado(double totalFacturado) {
        this.totalFacturado = totalFacturado;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }

    public void setCantidadVentas(int cantidadVentas) {
        this.cantidadVentas = cantidadVentas;
    }

    public double getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(double ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public VentaConImporteDTO getVentaMayor() {
        return ventaMayor;
    }

    public void setVentaMayor(VentaConImporteDTO ventaMayor) {
        this.ventaMayor = ventaMayor;
    }

    public VentaConImporteDTO getVentaMenor() {
        return ventaMenor;
    }

    public void setVentaMenor(VentaConImporteDTO ventaMenor) {
        this.ventaMenor = ventaMenor;
    }

    public String getProductoMasVendido() {
        return productoMasVendido;
    }

    public void setProductoMasVendido(String productoMasVendido) {
        this.productoMasVendido = productoMasVendido;
    }
}
