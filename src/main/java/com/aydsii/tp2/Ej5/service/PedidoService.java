package com.aydsii.tp2.Ej5.service;

import com.aydsii.tp2.BadRequestException;
import com.aydsii.tp2.Ej5.dto.PedidoResponseDTO;
import com.aydsii.tp2.Ej5.dto.ProductoPedidoDTO;
import com.aydsii.tp2.Ej5.entity.DetallePedido;
import com.aydsii.tp2.Ej5.entity.Pedido;
import com.aydsii.tp2.Ej5.repository.PedidoRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


//Indica que es de tipo Serivce, es algo que se instanciara una sola vez y se utilize durante la ejecucion
@Service
public class PedidoService {

    //Setea estados unicos, no es como una lsita ya que no se recorre por indice y es algo mas particular de tipo
    //este valor esta adentro o no? y chequea
    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("PENDIENTE", "ENVIADO", "ENTREGADO", "CANCELADO");

    //Inyeccion de dependencias. Crea una instancia de estos sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponseDTO> buscar(Integer clienteId, String categoria, String fechaDesdeStr,
                                           String fechaHastaStr, String estado) {

        LocalDate fechaDesde = parsearFecha(fechaDesdeStr, "fechaDesde");
        LocalDate fechaHasta = parsearFecha(fechaHastaStr, "fechaHasta");
        String estadoNormalizado = validarEstado(estado);

        //Arma la lista y la agarra del pedidoRepository que hace ese "cast" de la consulta
        List<Pedido> pedidos = pedidoRepository.buscarConFiltros(
                clienteId, categoria, fechaDesde, fechaHasta, estadoNormalizado);

        //Retorna los pedidos a traves de la funcion mapearApedido.., el cual arma una lista con los productos pedidos (ver funcion)
        return pedidos.stream()
                .map(this::mapearAPedidoResponseDTO)
                .collect(Collectors.toList());
    }

    private LocalDate parsearFecha(String fecha, String nombreCampo) {
        if (fecha == null || fecha.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(fecha);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("El parametro " + nombreCampo + " debe tener formato yyyy-MM-dd");
        }
    }

    private String validarEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        String estadoUpper = estado.toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoUpper)) {
            throw new BadRequestException(
                    "El estado debe ser uno de: PENDIENTE, ENVIADO, ENTREGADO, CANCELADO");
        }
        return estadoUpper;
    }

    //Arma la respuesta completa de un pedido
    private PedidoResponseDTO mapearAPedidoResponseDTO(Pedido pedido) {
        List<ProductoPedidoDTO> productos = pedido.getDetalles().stream()
                .map(this::mapearADetalleDTO)
                .collect(Collectors.toList());

        double total = productos.stream()
                .mapToDouble(ProductoPedidoDTO::getSubtotal)
                .sum();

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setPedidoId(pedido.getId());
        dto.setCliente(pedido.getCliente().getNombre() + " " + pedido.getCliente().getApellido());
        dto.setFecha(pedido.getFechaPedido());
        dto.setEstado(pedido.getEstado());
        dto.setProductos(productos);
        dto.setTotalPedido(total);

        return dto;
    }

    private ProductoPedidoDTO mapearADetalleDTO(DetallePedido detalle) {
        ProductoPedidoDTO dto = new ProductoPedidoDTO();
        dto.setNombre(detalle.getProducto().getNombre());
        dto.setCategoria(detalle.getProducto().getCategoria() != null
                ? detalle.getProducto().getCategoria().getNombre() : null);
        dto.setCantidad(detalle.getCantidad());
        dto.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
        return dto;
    }
}