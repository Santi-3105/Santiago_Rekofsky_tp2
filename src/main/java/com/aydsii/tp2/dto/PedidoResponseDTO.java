package com.aydsii.tp2.dto;

import java.time.LocalDate;
import java.util.List;

public class PedidoResponseDTO {

    private Integer pedidoId;
    private String cliente;
    private LocalDate fecha;
    private String estado;
    private double totalPedido;
    private List<ProductoPedidoDTO> productos;

    public PedidoResponseDTO() {
    }

    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(double totalPedido) {
        this.totalPedido = totalPedido;
    }

    public List<ProductoPedidoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoPedidoDTO> productos) {
        this.productos = productos;
    }
}