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

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Consulta del historial de pedidos con filtros combinables")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(
            summary = "Busca pedidos aplicando filtros opcionales combinables",
            description = "Todos los parametros son opcionales. Si se envia mas de uno, se combinan con AND. " +
                    "Si ninguno se cumple, devuelve HTTP 200 con una lista vacia."
    )
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<PedidoResponseDTO>>> buscar(
            @Parameter(description = "Id del cliente", example = "5")
            @RequestParam(required = false) Integer clienteId,
            @Parameter(description = "Nombre de categoria de al menos un producto del pedido", example = "Perifericos")
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Fecha inicial, formato yyyy-MM-dd", example = "2026-01-01")
            @RequestParam(required = false) String fechaDesde,
            @Parameter(description = "Fecha final, formato yyyy-MM-dd", example = "2026-12-31")
            @RequestParam(required = false) String fechaHasta,
            @Parameter(description = "PENDIENTE, ENVIADO, ENTREGADO o CANCELADO", example = "ENTREGADO")
            @RequestParam(required = false) String estado) {

        List<PedidoResponseDTO> pedidos = pedidoService.buscar(clienteId, categoria, fechaDesde, fechaHasta, estado);
        return ResponseEntity.ok(ApiResponse.of(200, "Consulta realizada correctamente", pedidos));
    }
}