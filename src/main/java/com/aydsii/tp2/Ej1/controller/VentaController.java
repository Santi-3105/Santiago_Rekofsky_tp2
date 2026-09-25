package com.aydsii.tp2.Ej1.controller;

import com.aydsii.tp2.Ej1.dto.AplicarDescuentoResponseDTO;
import com.aydsii.tp2.Ej1.dto.EstadisticasVentasDTO;
import com.aydsii.tp2.Ej1.dto.VentaDTO;
import com.aydsii.tp2.ApiResponse;
import com.aydsii.tp2.Ej1.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//Clase anotada que procesa solicitudes web y devuelve datos directamente (como JSON o XML) en lugar de renderizar una vista HTML
@RestController
//Es el proceso de conectar las peticiones HTTP que llegan al servidor con los métodos específicos de un controlador, es decir que lleguen como nuestro JSON
@RequestMapping("/api/ventas")
@Validated
@Tag(name = "Ventas", description = "Procesamiento en memoria de lotes de ventas recibidos de otro sistema")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }
    //El mensaje que esta como descripcion, indica que operacion se hara
    @Operation(
            summary = "Calcula estadisticas de un lote de ventas",
            description = "Recibe una lista de ventas y devuelve totalFacturado, cantidadVentas, "
                    + "ticketPromedio, ventaMayor, ventaMenor y productoMasVendido. No persiste datos."
    )
    //Los codigos como llegaran, en mi caso tengo una clase parametrizada ApiResponse.java el cual es como llegara mi JSON con los resultados
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Estadisticas calculadas correctamente",
                    content = @Content(schema = @Schema(implementation = EstadisticasVentasDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "La lista viene vacia o alguna venta no cumple las validaciones")
    })
    //Indica que es un post
    @PostMapping("/estadisticas")
    //representa toda la respuesta HTTP, incluyendo el cuerpo, los encabezados y el código de estado
    public ResponseEntity<ApiResponse<EstadisticasVentasDTO>> calcularEstadisticas(
        //Parametro que llegara por el body (ventas en este caso) como una lista y se valida como un tipo VentaDTO
            @Valid @RequestBody List<@Valid VentaDTO> ventas) {

        EstadisticasVentasDTO estadisticas = ventaService.calcularEstadisticas(ventas);
        return ResponseEntity.ok(ApiResponse.of(200, "Estadisticas calculadas con exito", estadisticas));
    }

    @Operation(
            summary = "Aplica un descuento porcentual a un lote de ventas",
            description = "Recibe una lista de ventas y un porcentaje de descuento (0 a 100) y devuelve "
                    + "cada venta con su montoConDescuento, junto con el totalConDescuento general."
    )
    @ApiResponses(value = {
        //Swagger lee estas anotaciones y genera automáticamente una página web interactiva (Swagger UI)
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Descuento aplicado correctamente",
                    //Schema: Comentarios o descripciones
                    content = @Content(schema = @Schema(implementation = AplicarDescuentoResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "La lista viene vacia, alguna venta es invalida o el porcentaje esta fuera de 0-100")
    })
    @PostMapping("/aplicar-descuento")
    public ResponseEntity<ApiResponse<AplicarDescuentoResponseDTO>> aplicarDescuento(
        //Que sea valida el body, o sea la lista
            @Valid @RequestBody List<@Valid VentaDTO> ventas,
            //Parametro
            @Parameter(description = "Porcentaje de descuento a aplicar, entre 0 y 100", example = "10")
            //extraer parámetros de la URL de una petición HTTP
            @RequestParam
            @DecimalMin(value = "0", message = "El porcentaje no puede ser menor a 0")
            @DecimalMax(value = "100", message = "El porcentaje no puede ser mayor a 100")
            double porcentaje) {

        AplicarDescuentoResponseDTO resultado = ventaService.aplicarDescuento(ventas, porcentaje);
        return ResponseEntity.ok(ApiResponse.of(200, "Descuento aplicado con exito", resultado));
    }
}