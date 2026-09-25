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


//Clase anotada que procesa solicitudes web y devuelve datos directamente (como JSON o XML) en lugar de renderizar una vista HTML
@RestController
//Es el proceso de conectar las peticiones HTTP que llegan al servidor con los métodos específicos de un controlador, es decir que lleguen como nuestro JSON
//agrupa todos los endpoints de este controller bajo una sección desplegable llamada "Clientes"
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Alta de clientes en la base de datos MySQL")
public class ClienteController {

    private final ClienteService clienteService;
    //Inyeccion de dependencias. Crea una instancia de este sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    //indica que es una operacion con lo que hace y una descripcion
    @Operation(
            summary = "Alta simple de un cliente",
            description = "Inserta un cliente sin aplicar Bean Validation sobre los datos recibidos."
    )
    //Los codigos como llegaran, en mi caso tengo una clase parametrizada ApiResponse.java el cual es como llegara mi JSON con los resultados
    @ApiResponses(value = {
            //documenta posibles respuestas de un endpoint, con su codigo y un mensaje
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Cliente creado correctamente",
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class)))
    })
    //Indica que es un post
    @PostMapping
    //ResponeEntity representa toda la respuesta HTTP, incluyendo el cuerpo, los encabezados y el código de estado
    //@RequestBody le dice a Spring que tome el JSON completo del body del request y lo convierta a un objeto Java ClienteDTO
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
                    //Content: describe el contenido de esa respuesta, como? Vendra de la forma ProductoClass gracias a @schema, que dice que se implemente como tal
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos o email ya registrado")
    })
    //Indica que es un post en este caso de tipo validado
    @PostMapping("/validado")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> altaValidada(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = clienteService.altaValidada(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
        //Creo un estado en particular (201) porque es lo que pide el ejercicio, lo mismo que ResponseEntity.ok(ApiResponse.of..
                .body(ApiResponse.of(201, "Cliente creado con exito", new ClienteResponseDTO(cliente)));
    }
}