package com.aydsii.tp2.Ej6.dto;

import java.time.LocalDateTime;

public class CotizacionHistorialDTO {

    private LocalDateTime fecha;
    private double tasaCambio;

    public CotizacionHistorialDTO() {
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTasaCambio() {
        return tasaCambio;
    }

    public void setTasaCambio(double tasaCambio) {
        this.tasaCambio = tasaCambio;
    }
}