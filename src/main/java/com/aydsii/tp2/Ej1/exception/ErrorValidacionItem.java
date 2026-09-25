package com.aydsii.tp2.Ej1.exception;

public class ErrorValidacionItem {

    private int posicion;
    private String campo;
    private String motivo;

    public ErrorValidacionItem() {
    }

    public ErrorValidacionItem(int posicion, String campo, String motivo) {
        this.posicion = posicion;
        this.campo = campo;
        this.motivo = motivo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}