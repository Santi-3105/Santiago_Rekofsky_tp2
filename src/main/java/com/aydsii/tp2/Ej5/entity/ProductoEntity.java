package com.aydsii.tp2.Ej5.entity;

import jakarta.persistence.*;

//Agregue una carpeta entity porque sino luego chocara con los nombres de mis otras clases (Producto)
//Si bien le modifico el nombre lo hice asi para entender mejor
//@Entity identifica esta clase como una tabla de la base de datos, y cada instancia va a corresponder a una fila
@Entity
@Table(name = "productos")
public class ProductoEntity {

    //Identifica cual es el campo primario o clave primaria
    @Id
    //Derivamos la logica al sql y que este incremente en uno a partir del ultimo id dado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    //con coclumnDefinition decido yo que tipo de dato usar (tuve que cambiarlo porque me daba un bug)   
    @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double precio;

    @Column(nullable = false)
    private int stock;

    //Muchas instancias de esta entidad pueden apuntar a una sola instancia de la otra entidad
    @ManyToOne
    //especifica el nombre real de la columna en la tabla que guarda esa foreign key, lo mismo que column name pero para foreign keys
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    public ProductoEntity() {
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}