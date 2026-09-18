package com.aydsii.tp2.service;

import com.aydsii.tp2.dto.PedidoResponseDTO;
import com.aydsii.tp2.dto.ProductoPedidoDTO;
import com.aydsii.tp2.entity.DetallePedido;
import com.aydsii.tp2.entity.Pedido;
import com.aydsii.tp2.exception.BadRequestException;
import com.aydsii.tp2.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private static final Set<String> ESTADOS_VALIDOS =
            Set.of("PENDIENTE", "ENVIADO", "ENTREGADO", "CANCELADO");

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponseDTO> buscar(Integer clienteId, String categoria, String fechaDesdeStr,
                                           String fechaHastaStr, String estado) {

        LocalDate fechaDesde = parsearFecha(fechaDesdeStr, "fechaDesde");
        LocalDate fechaHasta = parsearFecha(fechaHastaStr, "fechaHasta");
        String estadoNormalizado = validarEstado(estado);

        List<Pedido> pedidos = pedidoRepository.buscarConFiltros(
                clienteId, categoria, fechaDesde, fechaHasta, estadoNormalizado);

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