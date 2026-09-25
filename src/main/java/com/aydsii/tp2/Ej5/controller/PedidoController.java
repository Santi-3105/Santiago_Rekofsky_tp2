package com.aydsii.tp2.Ej5.controller;

import com.aydsii.tp2.Ej5.dto.PedidoResponseDTO;
import com.aydsii.tp2.Ej5.service.PedidoService;
import com.aydsii.tp2.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
//Clase anotada que procesa solicitudes web y devuelve datos directamente (como JSON o XML) en lugar de renderizar una vista HTML
@RestController
//Es el proceso de conectar las peticiones HTTP que llegan al servidor con los métodos específicos de un controlador, es decir que lleguen como nuestro JSON
//agrupa todos los endpoints de este controller bajo una sección desplegable llamada "Pedidos"
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Consulta del historial de pedidos con filtros combinables")
public class PedidoController {

    private final PedidoService pedidoService;
    //Inyeccion de dependencias. Crea una instancia de este sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }
    //indica que es una operacion con lo que hace y una descripcion
    @Operation(
            summary = "Busca pedidos aplicando filtros opcionales combinables",
            description = "Todos los parametros son opcionales. Si se envia mas de uno, se combinan con AND. " +
                    "Si ninguno se cumple, devuelve HTTP 200 con una lista vacia."
    )
    //Indica que es un get en este caso buscar
    @GetMapping("/buscar")
    //ResponeEntity representa toda la respuesta HTTP, incluyendo el cuerpo, los encabezados y el código de estado
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> buscar(
            //Parametro que le pasaremos, formato
            @Parameter(description = "Id del cliente", example = "5")
            //Parametro solicitado
            //si dice required=false es que no es necesario que llegue un parametro, puede quedar null
            @RequestParam(required = false) Integer clienteId,
            @Parameter(description = "Nombre de categoria de al menos un producto del pedido", example = "Perifericos")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Fecha inicial, formato yyyy-MM-dd", example = "2026-01-01")
            @RequestParam(required = false) String fechaDesde,
            @Parameter(description = "Fecha final, formato yyyy-MM-dd", example = "2026-12-31")
            @RequestParam(required = false) String fechaHasta,
            @Parameter(description = "PENDIENTE, ENVIADO, ENTREGADO o CANCELADO", example = "ENTREGADO")
            @RequestParam(required = false) String estado) {

        //Crea el listado con los pedidos, pidiendole a traves de pedidoService buscar dicho pedido
        List<PedidoResponseDTO> pedidos = pedidoService.buscar(clienteId, categoria, fechaDesde, fechaHasta, estado);
        return ResponseEntity.ok(ApiResponse.of(200, "Consulta realizada correctamente", pedidos));
    }
}