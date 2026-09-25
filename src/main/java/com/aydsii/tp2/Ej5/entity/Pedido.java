package com.aydsii.tp2.Ej5.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.aydsii.tp2.Ej4.model.Cliente;

//Agregue una carpeta entity porque sino luego chocara con los nombres de mis otras clases (Producto)
//Si bien le modifico el nombre lo hice asi para entender mejor
//@Entity identifica esta clase como una tabla de la base de datos, y cada instancia va a corresponder a una fila
@Entity
@Table(name = "pedidos")
public class Pedido {

    //Identifica cual es el campo primario o clave primaria
    @Id
    //Derivamos la logica al sql y que este incremente en uno a partir del ultimo id dado
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Muchas instancias de esta entidad pueden apuntar a una sola instancia de la otra entidad
    @ManyToOne
    //especifica el nombre real de la columna en la tabla que guarda esa foreign key, lo mismo que column name pero para foreign keys
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Column(nullable = false, length = 50)
    private String estado;

    //una instancia de esta entidad (Pedido) pueden apuntar a una o muchas instancias de la otra entidad (DetallePedido)
    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }
}