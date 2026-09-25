package com.aydsii.tp2.Ej3.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Representa la respuesta cruda de la API externa Frankfurter (endpoint /v2/rate/{origen}/{destino}).
 * No se expone al cliente: se usa solo internamente para parsearla.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrankfurterResponseDTO {

    private String date;
    private String base;
    private String quote;
    private double rate;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}