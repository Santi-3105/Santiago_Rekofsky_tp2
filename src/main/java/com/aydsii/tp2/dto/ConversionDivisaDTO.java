package com.aydsii.tp2.dto;

public class ConversionDivisaDTO {

    private double montoOriginal;
    private String monedaOrigen;
    private String monedaDestino;
    private double tasaCambio;
    private double montoConvertido;
    private String fecha;

    public double getMontoOriginal() {
        return montoOriginal;
    }

    public void setMontoOriginal(double montoOriginal) {
        this.montoOriginal = montoOriginal;
    }

    public String getMonedaOrigen() {
        return monedaOrigen;
    }

    public void setMonedaOrigen(String monedaOrigen) {
        this.monedaOrigen = monedaOrigen;
    }

    public String getMonedaDestino() {
        return monedaDestino;
    }

    public void setMonedaDestino(String monedaDestino) {
        this.monedaDestino = monedaDestino;
    }

    public double getTasaCambio() {
        return tasaCambio;
    }

    public void setTasaCambio(double tasaCambio) {
        this.tasaCambio = tasaCambio;
    }

    public double getMontoConvertido() {
        return montoConvertido;
    }

    public void setMontoConvertido(double montoConvertido) {
        this.montoConvertido = montoConvertido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}