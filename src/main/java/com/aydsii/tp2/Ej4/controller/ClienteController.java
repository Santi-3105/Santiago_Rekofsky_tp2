package com.aydsii.tp2.Ej4.controller;

import com.aydsii.tp2.Ej4.dto.ClienteDTO;
import com.aydsii.tp2.Ej4.dto.ClienteResponseDTO;
import com.aydsii.tp2.Ej4.model.Cliente;
import com.aydsii.tp2.Ej4.service.ClienteService;
import com.aydsii.tp2.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Alta de clientes en la base de datos MySQL")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
            summary = "Alta simple de un cliente",
            description = "Inserta un cliente sin aplicar Bean Validation sobre los datos recibidos."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Cliente creado correctamente",
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> altaSimple(@RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.altaSimple(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Cliente creado con exito", new ClienteResponseDTO(cliente)));
    }

    @Operation(
            summary = "Alta de un cliente con validaciones",
            description = "Valida nombre, apellido, email y telefono con Bean Validation, "
                    + "y ademas verifica que el email no este registrado previamente."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Cliente creado correctamente",
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos o email ya registrado")
    })
    @PostMapping("/validado")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> altaValidada(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.altaValidada(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Cliente creado con exito", new ClienteResponseDTO(cliente)));
    }
}