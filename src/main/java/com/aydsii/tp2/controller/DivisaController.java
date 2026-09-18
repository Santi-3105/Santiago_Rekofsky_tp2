package com.aydsii.tp2.controller;

import com.aydsii.tp2.dto.ApiResponse;
import com.aydsii.tp2.dto.ConversionDivisaDTO;
import com.aydsii.tp2.dto.CotizacionHistorialDTO;
import com.aydsii.tp2.service.DivisaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/divisas")
@Validated
@Tag(name = "Divisas", description = "Conversor de divisas y su historial, usando la API externa Frankfurter")
public class DivisaController {

    private static final String REGEX_CODIGO_MONEDA = "^[A-Za-z]{3}$";

    private final DivisaService divisaService;

    public DivisaController(DivisaService divisaService) {
        this.divisaService = divisaService;
    }

    @Operation(
            summary = "Convierte un monto entre dos monedas",
            description = "Consulta la cotizacion actual en la API externa Frankfurter y devuelve el monto convertido. No persiste nada."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Conversion realizada correctamente",
                    content = @Content(schema = @Schema(implementation = ConversionDivisaDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos: monto <= 0, codigo de moneda mal formado o moneda inexistente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "502",
                    description = "No se pudo obtener la cotizacion desde el servicio externo")
    })
    @GetMapping("/convertir")
    public ResponseEntity<ApiResponse<ConversionDivisaDTO>> convertir(
            @Parameter(description = "Monto a convertir", example = "100")
            @RequestParam @Positive(message = "El monto debe ser mayor que 0") double monto,
            @Parameter(description = "Codigo de moneda de origen (3 letras)", example = "USD")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de origen debe tener 3 letras") String origen,
            @Parameter(description = "Codigo de moneda de destino (3 letras)", example = "ARS")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de destino debe tener 3 letras") String destino) {

        ConversionDivisaDTO resultado = divisaService.convertir(monto, origen, destino);
        return ResponseEntity.ok(ApiResponse.of(200, "Conversion realizada con exito", resultado));
    }

    @Operation(
            summary = "Consulta la cotizacion actual y la guarda en el historial",
            description = "Igual que /convertir, pero ademas registra la consulta en la tabla historial_conversiones."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Consulta realizada y guardada correctamente",
                    content = @Content(schema = @Schema(implementation = ConversionDivisaDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos: monto <= 0, codigo de moneda mal formado o moneda inexistente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "502",
                    description = "No se pudo obtener la cotizacion desde el servicio externo")
    })
    @PostMapping("/consultar")
    public ResponseEntity<ApiResponse<ConversionDivisaDTO>> consultar(
            @Parameter(description = "Monto a convertir", example = "100")
            @RequestParam @Positive(message = "El monto debe ser mayor que 0") double monto,
            @Parameter(description = "Codigo de moneda de origen (3 letras)", example = "USD")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de origen debe tener 3 letras") String origen,
            @Parameter(description = "Codigo de moneda de destino (3 letras)", example = "ARS")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de destino debe tener 3 letras") String destino) {

        ConversionDivisaDTO resultado = divisaService.consultarYGuardar(monto, origen, destino);
        return ResponseEntity.ok(ApiResponse.of(200, "Consulta realizada y guardada con exito", resultado));
    }

    @Operation(
            summary = "Historial de cotizaciones consultadas para un par de monedas",
            description = "Devuelve todas las consultas guardadas para ese par, de la mas reciente a la mas antigua."
    )
    @GetMapping("/historial")
    public ResponseEntity<ApiResponse<List<CotizacionHistorialDTO>>> historial(
            @Parameter(description = "Codigo de moneda de origen (3 letras)", example = "USD")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de origen debe tener 3 letras") String origen,
            @Parameter(description = "Codigo de moneda de destino (3 letras)", example = "ARS")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de destino debe tener 3 letras") String destino) {

        List<CotizacionHistorialDTO> historial = divisaService.obtenerHistorial(origen, destino);
        return ResponseEntity.ok(ApiResponse.of(200, "Historial obtenido con exito", historial));
    }
}