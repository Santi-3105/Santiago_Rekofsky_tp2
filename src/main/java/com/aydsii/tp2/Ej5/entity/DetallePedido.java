package com.aydsii.tp2.Ej5.entity;

import jakarta.persistence.*;

//Agregue una carpeta entity porque sino luego chocara con los nombres de mis otras clases (Producto)
//Si bien le modifico el nombre lo hice asi para entender mejor
//@Entity identifica esta clase como una tabla de la base de datos, y cada instancia va a corresponder a una fila
@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido {
    
    //Identifica cual es el campo primario o clave primaria
    @Id
    //Derivamos la logica al sql y que este incremente en uno a partir del ultimo id dado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Muchas instancias de esta entidad (DetallePedido) pueden apuntar a una sola instancia de la otra entidad (Pedido, o ProductoEntity)
    @ManyToOne
    //especifica el nombre real de la columna en la tabla que guarda esa foreign key, lo mismo que column name pero para foreign keys
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoEntity producto;

    @Column(nullable = false)
    private int cantidad;

    //con coclumnDefinition decido yo que tipo de dato usar (tuve que cambiarlo porque me daba un bug)
    @Column(name = "precio_unitario", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private double precioUnitario;

    public DetallePedido() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
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