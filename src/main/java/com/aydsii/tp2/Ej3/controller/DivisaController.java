package com.aydsii.tp2.Ej3.controller;

import com.aydsii.tp2.Ej3.dto.ConversionDivisaDTO;
import com.aydsii.tp2.Ej3.service.DivisaService;
import com.aydsii.tp2.ApiResponse;
import com.aydsii.tp2.Ej6.dto.CotizacionHistorialDTO;

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
//Clase anotada que procesa solicitudes web y devuelve datos directamente (como JSON o XML) en lugar de renderizar una vista HTML
@RestController
//Es el proceso de conectar las peticiones HTTP que llegan al servidor con los métodos específicos de un controlador, es decir que lleguen como nuestro JSON
//agrupa todos los endpoints de este controller bajo una sección desplegable llamada "Divisas"
@RequestMapping("/api/divisas")
//@Validated se utiliza para activar la validación automatica de datos a nivel de clase o de metodo
@Validated
@Tag(name = "Divisas", description = "Conversor de divisas y su historial, usando la API externa Frankfurter")
public class DivisaController {

    //Basicamente parametriza como se controlara la divisa, tienen que ser 3 letras desde la a a la z y en minusculas o mayusculas
    private static final String REGEX_CODIGO_MONEDA = "^[A-Za-z]{3}$";

    
    private final DivisaService divisaService;
    //Inyeccion de dependencias. Crea una instancia de este sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
    public DivisaController(DivisaService divisaService) {
        this.divisaService = divisaService;
    }

    //Idica una operacion, que hace y una descripccion
    @Operation(
            summary = "Convierte un monto entre dos monedas",
            description = "Consulta la cotizacion actual en la API externa Frankfurter y devuelve el monto convertido. No persiste nada."
    )
    //Los codigos como llegaran, en mi caso tengo una clase parametrizada ApiResponse.java el cual es como llegara mi JSON con los resultados
    @ApiResponses(value = {
            //documenta posibles respuestas de un endpoint, con su codigo y un mensaje
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
    //Indica que es un get
    @GetMapping("/convertir")
    public ResponseEntity<ApiResponse<ConversionDivisaDTO>> convertir(
            //Parametro que le pasaremos, formato
            @Parameter(description = "Monto a convertir", example = "100")
            //Parametro solicitado
            //si dice required=false es que no es necesario que llegue un parametro, puede quedar null
            @RequestParam @Positive(message = "El monto debe ser mayor que 0") double monto,
            @Parameter(description = "Codigo de moneda de origen (3 letras)", example = "USD")
            //@Pattern y regexp verifican que el codigo de moneda sea el adecuado
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
    //Indica que es un Post
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
    //Indica que es un GET
    @GetMapping("/historial")
    public ResponseEntity<ApiResponse<List<CotizacionHistorialDTO>>> historial(
            @Parameter(description = "Codigo de moneda de origen (3 letras)", example = "USD")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de origen debe tener 3 letras") String origen,
            @Parameter(description = "Codigo de moneda de destino (3 letras)", example = "ARS")
            @RequestParam @Pattern(regexp = REGEX_CODIGO_MONEDA, message = "El codigo de moneda de destino debe tener 3 letras") String destino) {

                //Crea el listado con las cotizaciones, pidiendole a traves de divisaService obtener el historial
        List<CotizacionHistorialDTO> historial = divisaService.obtenerHistorial(origen, destino);
        return ResponseEntity.ok(ApiResponse.of(200, "Historial obtenido con exito", historial));
    }
}