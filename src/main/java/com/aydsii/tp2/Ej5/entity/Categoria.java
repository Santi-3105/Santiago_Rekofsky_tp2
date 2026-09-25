package com.aydsii.tp2.Ej5.entity;

import jakarta.persistence.*;

//Agregue una carpeta entity porque sino luego chocara con los nombres de mis otras clases (Producto)
//Si bien le modifico el nombre lo hice asi para entender mejor
//@Entity identifica esta clase como una tabla de la base de datos, y cada instancia va a corresponder a una fila
@Entity
//El nombre de la tabla, parecido a Tag. Sino se le pone nada por defecto vendra como el nombre de la clase
@Table(name = "categorias")
public class Categoria {
    
    //Identifica cual es el campo primario o clave primaria
    @Id
    //Derivamos la logica al sql y que este incremente en uno a partir del ultimo id dado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Identificamos una columna de la tabla de la bdd, nullable es que no puede ser nula y su maximo que es length
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    public Categoria() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}