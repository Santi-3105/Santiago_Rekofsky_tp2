package com.aydsii.tp2.Ej2.controller;

import com.aydsii.tp2.Ej2.dto.ProductoDTO;
import com.aydsii.tp2.Ej2.model.Producto;
import com.aydsii.tp2.Ej2.service.CatalogoService;
import com.aydsii.tp2.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//Clase anotada que procesa solicitudes web y devuelve datos directamente (como JSON o XML) en lugar de renderizar una vista HTML
@RestController
//Es el proceso de conectar las peticiones HTTP que llegan al servidor con los métodos específicos de un controlador, es decir que lleguen como nuestro JSON
//agrupa todos los endpoints de este controller bajo una sección desplegable llamada "Catalogo"
@RequestMapping("/api/catalogo")
@Tag(name = "Catalogo", description = "Gestion en memoria del catalogo de productos de TechStore")
public class CatalogoController {

    private final CatalogoService catalogoService;
    //Inyeccion de dependencias. Crea una instancia de este sin necesidad un New cuando arranca Spring
    //Ventaja de que ya la recirbe armada como parametro
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }
    //El mensaje que esta como descripcion, indica que operacion se hara
    @Operation(summary = "Lista todos los productos del catalogo")
    @GetMapping
    public ResponseEntity<ApiResponse<List<Producto>>> obtenerTodos() {
        List<Producto> productos = catalogoService.obtenerTodos();
        return ResponseEntity.ok(ApiResponse.of(200, "Productos obtenidos con exito", productos));
    }

    @Operation(
            summary = "Busca productos por categoria y/o rango de precio",
            description = "Todos los parametros son opcionales y, si se envia mas de uno, se combinan con AND"
    )
    //Indica que es un get
    @GetMapping("/buscar")
    //representa toda la respuesta HTTP, incluyendo el cuerpo, los encabezados y el código de estado
    public ResponseEntity<ApiResponse<List<Producto>>> buscar(
            //Parametro que le pasaremos, formato
            @Parameter(description = "Categoria a filtrar", example = "Perifericos")
            //Parametro solicitado
            //si dice required=false es que no es necesario que llegue un parametro, puede quedar null
            @RequestParam(required = false) String categoria,
            @Parameter(description = "Precio minimo (inclusive)", example = "1000")
            @RequestParam(required = false) Double precioMin,
            @Parameter(description = "Precio maximo (inclusive)", example = "10000")
            @RequestParam(required = false) Double precioMax) {

        List<Producto> productos = catalogoService.buscar(categoria, precioMin, precioMax);
        return ResponseEntity.ok(ApiResponse.of(200, "Busqueda realizada con exito", productos));
    }

    @Operation(
            summary = "Ordena los productos del catalogo",
            description = "criterio: 'precio' o 'nombre'. orden: 'asc' (por defecto) o 'desc'"
    )
    @GetMapping("/ordenar")
    public ResponseEntity<ApiResponse<List<Producto>>> ordenar(
            @Parameter(description = "Criterio de ordenamiento: precio o nombre", example = "precio")
            //Por default, required=true
            @RequestParam String criterio,
            @Parameter(description = "Orden: asc o desc", example = "desc")
            @RequestParam(required = false, defaultValue = "asc") String orden) {

        List<Producto> productos = catalogoService.ordenar(criterio, orden);
        return ResponseEntity.ok(ApiResponse.of(200, "Productos ordenados con exito", productos));
    }
    //El mensaje que esta como descripcion, indica que operacion se hara
    @Operation(summary = "Agrega un nuevo producto al catalogo")
    //Los codigos como llegaran, en mi caso tengo una clase parametrizada ApiResponse.java el cual es como llegara mi JSON con los resultados
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    //Content: describe el contenido de esa respuesta, como? Vendra de la forma ProductoClass gracias a @schema, que dice que se implemente como tal
                    content = @Content(schema = @Schema(implementation = Producto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos del producto invalidos")
    })
    //Indica que es un post
    @PostMapping
    //@RequestBody le dice a Spring que tome el JSON completo del body del request y lo convierta a un objeto Java ProductoDTO
    //@Valid verifica que la conversion sea correcta y los campos obligatorios esten correctos
    public ResponseEntity<ApiResponse<Producto>> agregar(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto producto = catalogoService.agregar(productoDTO);
        //Creo un estado en particular (201) porque es lo que pide el ejercicio, lo mismo que ResponseEntity.ok(ApiResponse.of..
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Producto creado con exito", producto));
    }

    @Operation(
            summary = "Modifica el stock de un producto",
            description = "cantidad positiva aumenta el stock, negativa lo disminuye. No puede quedar por debajo de 0"
    )
    @ApiResponses(value = {
        //documenta posibles respuestas de un endpoint, con su codigo y un mensaje
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock modificado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El stock resultante seria negativo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    })
    //Indica que es un put
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Producto>> modificarStock(
            //Basicamente si viene algo de catalogo como PUT /api/catalogo/5/stock ese 5 se captura automaticamente y swagger detectara que es el producto con id 5 y aumentara o decrementara
            //Probar?
            @Parameter(description = "Id del producto") @PathVariable Long id,
            @Parameter(description = "Cantidad a sumar (negativa para restar)", example = "5")
            @RequestParam int cantidad) {

        Producto producto = catalogoService.modificarStock(id, cantidad);
        return ResponseEntity.ok(ApiResponse.of(200, "Stock modificado con exito", producto));
    }

    @Operation(summary = "Elimina un producto del catalogo")
    @ApiResponses(value = {
        //documenta posibles respuestas de un endpoint
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    })
    //Indica que es un delete
    @DeleteMapping("/{id}")
    //Como el delete no tiene nada que devolver, se pondra <ApiResponse<Object>, puesto que si fuera Producto si nos podria deolver de algo de ese tipo
    //Ademas data:null es aproposito, para que no devuelva nada y cumpliendo con la consigna de el tp
    public ResponseEntity<ApiResponse<Object>> eliminar(@Parameter(description = "Id del producto") @PathVariable Long id) {
        catalogoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.of(200, "Producto eliminado con exito", null));
    }
}