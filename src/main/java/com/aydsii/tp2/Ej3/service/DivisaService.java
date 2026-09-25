package com.aydsii.tp2.Ej3.service;

import com.aydsii.tp2.BadRequestException;
import com.aydsii.tp2.Ej3.dto.ConversionDivisaDTO;
import com.aydsii.tp2.Ej3.dto.FrankfurterResponseDTO;
import com.aydsii.tp2.Ej3.exception.ExternalServiceException;
import com.aydsii.tp2.Ej6.dto.CotizacionHistorialDTO;
import com.aydsii.tp2.Ej6.entity.HistorialConversion;
import com.aydsii.tp2.Ej6.repository.HistorialConversionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DivisaService {

    private final RestClient restClient;
    private final HistorialConversionRepository historialConversionRepository;

    public DivisaService(RestClient frankfurterRestClient,
                          HistorialConversionRepository historialConversionRepository) {
        this.restClient = frankfurterRestClient;
        this.historialConversionRepository = historialConversionRepository;
    }

    /**
     * Ejercicio 3: convierte un monto entre dos monedas sin persistir nada.
     */
    public ConversionDivisaDTO convertir(double monto, String origen, String destino) {
        String origenNormalizado = origen.toUpperCase();
        String destinoNormalizado = destino.toUpperCase();

        FrankfurterResponseDTO respuesta = obtenerCotizacion(origenNormalizado, destinoNormalizado);

        double tasaCambio = respuesta.getRate();
        double montoConvertido = monto * tasaCambio;

        ConversionDivisaDTO resultado = new ConversionDivisaDTO();
        resultado.setMontoOriginal(monto);
        resultado.setMonedaOrigen(origenNormalizado);
        resultado.setMonedaDestino(destinoNormalizado);
        resultado.setTasaCambio(tasaCambio);
        resultado.setMontoConvertido(montoConvertido);
        resultado.setFecha(respuesta.getDate());

        return resultado;
    }

    /**
     * Ejercicio 6: hace la misma consulta que convertir(), pero ademas
     * guarda la consulta en historial_conversiones.
     */
    public ConversionDivisaDTO consultarYGuardar(double monto, String origen, String destino) {
        ConversionDivisaDTO resultado = convertir(monto, origen, destino);

        HistorialConversion historial = new HistorialConversion();
        historial.setMonedaOrigen(resultado.getMonedaOrigen());
        historial.setMonedaDestino(resultado.getMonedaDestino());
        historial.setMonto(monto);
        historial.setMontoConvertido(resultado.getMontoConvertido());
        historial.setTasa(resultado.getTasaCambio());
        historial.setFechaConsulta(LocalDateTime.now());

        historialConversionRepository.save(historial);

        return resultado;
    }

    /**
     * Ejercicio 6: recupera el historial de consultas para un par de monedas,
     * de la mas reciente a la mas antigua.
     */
    public List<CotizacionHistorialDTO> obtenerHistorial(String origen, String destino) {
        String origenNormalizado = origen.toUpperCase();
        String destinoNormalizado = destino.toUpperCase();

        List<HistorialConversion> historial = historialConversionRepository
                .findByMonedaOrigenAndMonedaDestinoOrderByFechaConsultaDesc(origenNormalizado, destinoNormalizado);

        return historial.stream()
                .map(h -> {
                    CotizacionHistorialDTO dto = new CotizacionHistorialDTO();
                    dto.setFecha(h.getFechaConsulta());
                    dto.setTasaCambio(h.getTasa());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private FrankfurterResponseDTO obtenerCotizacion(String origen, String destino) {
        try {
            return restClient.get()
                    .uri("/rate/{origen}/{destino}", origen, destino)
                    .retrieve()
                    .body(FrankfurterResponseDTO.class);
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("La moneda de origen o destino no existe o no es valida");
        } catch (RestClientException e) {
            throw new ExternalServiceException(
                    "No se pudo obtener la cotizacion desde el servicio externo de divisas");
        }
    }
}