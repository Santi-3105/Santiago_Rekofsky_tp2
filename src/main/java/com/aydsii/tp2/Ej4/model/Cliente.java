package com.aydsii.tp2.Ej4.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;


//@Entity identifica esta clase como una tabla de la base de datos, y cada instancia va a corresponder a una fila
@Entity
//El nombre de la tabla, parecido a Tag. Sino se le pone nada por defecto vendra como el nombre de la clase
@Table(name = "clientes")
public class Cliente {

    //Identifica cual es el campo primario o clave primaria
    @Id
    //Derivamos la logica al sql y que este incremente en uno a partir del ultimo id dado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Identificamos una columna de la tabla de la bdd, nullable es que no puede ser nula y su maximo que es length
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;
    //Decimos que el mail es unico, no hace que nuestra logica de verificar el mail si existe o no, no sirva. Sino que es otra capa mas de proteccion
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(length = 30)
    private String telefono;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String email, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}