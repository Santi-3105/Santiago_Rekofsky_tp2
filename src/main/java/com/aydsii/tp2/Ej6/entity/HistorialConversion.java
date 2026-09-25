package com.aydsii.tp2.Ej6.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

//Agregue una carpeta entity porque sino luego chocara con los nombres de mis otras clases (Producto)
//Si bien le modifico el nombre lo hice asi para entender mejor
//@Entity identifica esta clase como una tabla de la base de datos, y cada instancia va a corresponder a una fila
@Entity
//El nombre de la tabla, parecido a Tag. Sino se le pone nada por defecto vendra como el nombre de la clase
@Table(name = "historial_conversiones")
public class HistorialConversion {

    //Identifica cual es el campo primario o clave primaria
    @Id
    //Derivamos la logica al sql y que este incremente en uno a partir del ultimo id dado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Identificamos una columna de la tabla de la bdd, nullable es que no puede ser nula y su maximo que es length
    @Column(name = "moneda_origen", nullable = false, length = 3)
    private String monedaOrigen;

    @Column(name = "moneda_destino", nullable = false, length = 3)
    private String monedaDestino;

    @Column(nullable = false, columnDefinition = "DECIMAL(15,2)")
    private double monto;

    //con coclumnDefinition decido yo que tipo de dato usar (tuve que cambiarlo porque me daba un bug)
    @Column(name = "monto_convertido", nullable = false, columnDefinition = "DECIMAL(15,2)")
    private double montoConvertido;

    @Column(nullable = false, columnDefinition = "DECIMAL(15,6)")
    private double tasa;

    @Column(name = "fecha_consulta", nullable = false)
    private LocalDateTime fechaConsulta;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getMontoConvertido() {
        return montoConvertido;
    }

    public void setMontoConvertido(double montoConvertido) {
        this.montoConvertido = montoConvertido;
    }

    public double getTasa() {
        return tasa;
    }

    public void setTasa(double tasa) {
        this.tasa = tasa;
    }

    public LocalDateTime getFechaConsulta() {
        return fechaConsulta;
    }

    public void setFechaConsulta(LocalDateTime fechaConsulta) {
        this.fechaConsulta = fechaConsulta;
    }
}