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

//Indica que es de tipo Serivce, es algo que se instanciara una sola vez y se utilize durante la ejecucion
@Service
public class DivisaService {

    private final RestClient restClient;
    private final HistorialConversionRepository historialConversionRepository;

    //Inyeccion de dependencias. Crea una instancia de estos sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
    //RestClient se utiliza para hacer llamadas HTTP salientes hacia otras APIs
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
    //En este caso reutilizamos convertir, no hacemos de vuelta la llamada a frankfurter y armamos todo de vuelta
    public ConversionDivisaDTO consultarYGuardar(double monto, String origen, String destino) {
        ConversionDivisaDTO resultado = convertir(monto, origen, destino);

        HistorialConversion historial = new HistorialConversion();
        historial.setMonedaOrigen(resultado.getMonedaOrigen());
        historial.setMonedaDestino(resultado.getMonedaDestino());
        historial.setMonto(monto);
        historial.setMontoConvertido(resultado.getMontoConvertido());
        historial.setTasa(resultado.getTasaCambio());
        historial.setFechaConsulta(LocalDateTime.now());


        //Metodo que extiende de JPA el .save. Esto es lo que hace el insert en mi sql
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
        //Genera automaticamente una cosnulta equivalente a sql de decir =
        //SELECT * FROM historial_conversiones WHERE moneda_origen = ? AND moneda_destino = ? ORDER BY fecha_consulta DESC y cumple con lo dado en el tp
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
            return restClient.get() //arma un request HTTP tipo GET
                    .uri("/rate/{origen}/{destino}", origen, destino) //arma la URL final reemplazando los placeholders {origen}/{destino} por los valores reales
                    .retrieve() //ejecuta el request y prepara la respuesta para ser procesada
                    .body(FrankfurterResponseDTO.class); //Pasa del JSON que obtener a algo tipo Frankfurter
        } catch (HttpClientErrorException e) {
            throw new BadRequestException("La moneda de origen o destino no existe o no es valida");
        } catch (RestClientException e) {
            throw new ExternalServiceException(
                    "No se pudo obtener la cotizacion desde el servicio externo de divisas");
        }
    }
}