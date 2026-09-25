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

@RestController
@RequestMapping("/api/catalogo")
@Tag(name = "Catalogo", description = "Gestion en memoria del catalogo de productos de TechStore")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

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
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<Producto>>> buscar(
            @Parameter(description = "Categoria a filtrar", example = "Perifericos")
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
            @RequestParam String criterio,
            @Parameter(description = "Orden: asc o desc", example = "desc")
            @RequestParam(required = false, defaultValue = "asc") String orden) {

        List<Producto> productos = catalogoService.ordenar(criterio, orden);
        return ResponseEntity.ok(ApiResponse.of(200, "Productos ordenados con exito", productos));
    }

    @Operation(summary = "Agrega un nuevo producto al catalogo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Producto creado correctamente",
                    content = @Content(schema = @Schema(implementation = Producto.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Datos del producto invalidos")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<Producto>> agregar(@Valid @RequestBody ProductoDTO productoDTO) {
        Producto producto = catalogoService.agregar(productoDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Producto creado con exito", producto));
    }

    @Operation(
            summary = "Modifica el stock de un producto",
            description = "cantidad positiva aumenta el stock, negativa lo disminuye. No puede quedar por debajo de 0"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Stock modificado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "El stock resultante seria negativo"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    })
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Producto>> modificarStock(
            @Parameter(description = "Id del producto") @PathVariable Long id,
            @Parameter(description = "Cantidad a sumar (negativa para restar)", example = "5")
            @RequestParam int cantidad) {

        Producto producto = catalogoService.modificarStock(id, cantidad);
        return ResponseEntity.ok(ApiResponse.of(200, "Stock modificado con exito", producto));
    }

    @Operation(summary = "Elimina un producto del catalogo")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "No existe un producto con ese id")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@Parameter(description = "Id del producto") @PathVariable Long id) {
        catalogoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.of(200, "Producto eliminado con exito", null));
    }
}