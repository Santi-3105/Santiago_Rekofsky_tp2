package com.aydsii.tp2.Ej5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aydsii.tp2.Ej5.entity.Pedido;

import java.time.LocalDate;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {


        //Hace la query tal cual pide el enunciado, los fetch se utilizan para recorrer todas las tablas
        //En este caso join fetch lo que hace es traer todo en la misma consulta, todo lo relacionado digamos
        //Distinct para que no se repitan pedidos en un posible caso, por ej un pedido tiene tres productos, solo se da 1 pedido
        //El exist lo que hara es devolverme el producto si al menos existe uno en una categoria, es decir de 5 productos 1 solo entra en una categoria x
        //Lo ultimo conecta los nombres de la query con los parametros de mis clases, basicamente como un casteo para que no haya errores
    @Query("""
            SELECT DISTINCT p FROM Pedido p
            JOIN FETCH p.cliente c
            LEFT JOIN FETCH p.detalles d
            LEFT JOIN FETCH d.producto pr
            LEFT JOIN FETCH pr.categoria cat
            WHERE (:clienteId IS NULL OR c.id = :clienteId)
              AND (:estado IS NULL OR p.estado = :estado)
              AND (:fechaDesde IS NULL OR p.fechaPedido >= :fechaDesde)
              AND (:fechaHasta IS NULL OR p.fechaPedido <= :fechaHasta)
              AND (:categoria IS NULL OR EXISTS (
                    SELECT 1 FROM DetallePedido d2
                    JOIN d2.producto pr2
                    JOIN pr2.categoria cat2
                    WHERE d2.pedido = p AND LOWER(cat2.nombre) = LOWER(:categoria)
              ))
            ORDER BY p.id
            """)
    List<Pedido> buscarConFiltros(
            @Param("clienteId") Integer clienteId,
            @Param("categoria") String categoria,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            @Param("estado") String estado);
}