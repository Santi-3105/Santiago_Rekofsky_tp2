package com.aydsii.tp2.service;

import com.aydsii.tp2.dto.AplicarDescuentoResponseDTO;
import com.aydsii.tp2.dto.EstadisticasVentasDTO;
import com.aydsii.tp2.dto.VentaConDescuentoDTO;
import com.aydsii.tp2.dto.VentaConImporteDTO;
import com.aydsii.tp2.dto.VentaDTO;
import com.aydsii.tp2.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VentaService {

    public EstadisticasVentasDTO calcularEstadisticas(List<VentaDTO> ventas) {
        validarListaNoVacia(ventas);

        double totalFacturado = ventas.stream()
                .mapToDouble(VentaDTO::calcularImporte)
                .sum();

        int cantidadVentas = ventas.size();
        double ticketPromedio = totalFacturado / cantidadVentas;

        VentaDTO ventaMayor = ventas.stream()
                .max(Comparator.comparingDouble(VentaDTO::calcularImporte))
                .orElseThrow();

        VentaDTO ventaMenor = ventas.stream()
                .min(Comparator.comparingDouble(VentaDTO::calcularImporte))
                .orElseThrow();

        String productoMasVendido = ventas.stream()
                .collect(Collectors.groupingBy(VentaDTO::getProducto,
                        Collectors.summingInt(VentaDTO::getCantidad)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow();

        EstadisticasVentasDTO estadisticas = new EstadisticasVentasDTO();
        estadisticas.setTotalFacturado(totalFacturado);
        estadisticas.setCantidadVentas(cantidadVentas);
        estadisticas.setTicketPromedio(ticketPromedio);
        estadisticas.setVentaMayor(new VentaConImporteDTO(ventaMayor));
        estadisticas.setVentaMenor(new VentaConImporteDTO(ventaMenor));
        estadisticas.setProductoMasVendido(productoMasVendido);

        return estadisticas;
    }

    public AplicarDescuentoResponseDTO aplicarDescuento(List<VentaDTO> ventas, double porcentaje) {
        validarListaNoVacia(ventas);
        validarPorcentaje(porcentaje);

        List<VentaConDescuentoDTO> ventasConDescuento = ventas.stream()
                .map(venta -> new VentaConDescuentoDTO(venta, porcentaje))
                .collect(Collectors.toList());

        double totalConDescuento = ventasConDescuento.stream()
                .mapToDouble(VentaConDescuentoDTO::getMontoConDescuento)
                .sum();

        return new AplicarDescuentoResponseDTO(ventasConDescuento, totalConDescuento);
    }

    private void validarListaNoVacia(List<VentaDTO> ventas) {
        if (ventas == null || ventas.isEmpty()) {
            throw new BadRequestException("La lista de ventas no puede venir vacia");
        }
    }

    private void validarPorcentaje(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            throw new BadRequestException("El porcentaje de descuento debe estar entre 0 y 100");
        }
    }
}