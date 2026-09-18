package com.aydsii.tp2.service;

import com.aydsii.tp2.dto.ProductoDTO;
import com.aydsii.tp2.exception.BadRequestException;
import com.aydsii.tp2.exception.ResourceNotFoundException;
import com.aydsii.tp2.model.Producto;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class CatalogoService {

    private final List<Producto> productos = new ArrayList<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    @PostConstruct
    public void inicializarCatalogo() {
        agregarProductoInicial("Mouse inalambrico", "Perifericos", 4500.0, 25);
        agregarProductoInicial("Teclado mecanico", "Perifericos", 25000.0, 10);
        agregarProductoInicial("Monitor 24 pulgadas", "Monitores", 85000.0, 8);
        agregarProductoInicial("Notebook Core i5", "Notebooks", 650000.0, 5);
        agregarProductoInicial("Auriculares Bluetooth", "Audio", 18000.0, 15);
        agregarProductoInicial("Webcam Full HD", "Perifericos", 12000.0, 12);
        agregarProductoInicial("Disco SSD 480GB", "Almacenamiento", 30000.0, 20);
        agregarProductoInicial("Memoria RAM 8GB", "Componentes", 15000.0, 30);
    }

    private void agregarProductoInicial(String nombre, String categoria, double precio, int stock) {
        productos.add(new Producto(secuenciaId.incrementAndGet(), nombre, categoria, precio, stock));
    }

    public List<Producto> obtenerTodos() {
        return productos;
    }

    public List<Producto> buscar(String categoria, Double precioMin, Double precioMax) {
        return productos.stream()
                .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
                .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
                .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
                .collect(Collectors.toList());
    }

    public List<Producto> ordenar(String criterio, String orden) {
        Comparator<Producto> comparator = switch (criterio) {
            case "precio" -> Comparator.comparingDouble(Producto::getPrecio);
            case "nombre" -> Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER);
            default -> throw new BadRequestException("El criterio debe ser 'precio' o 'nombre'");
        };

        if ("desc".equalsIgnoreCase(orden)) {
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(orden)) {
            throw new BadRequestException("El orden debe ser 'asc' o 'desc'");
        }

        return productos.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public Producto agregar(ProductoDTO dto) {
        Producto producto = new Producto(
                secuenciaId.incrementAndGet(),
                dto.getNombre(),
                dto.getCategoria(),
                dto.getPrecio(),
                dto.getStock());
        productos.add(producto);
        return producto;
    }

    public Producto modificarStock(Long id, int cantidad) {
        Producto producto = buscarPorId(id);
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new BadRequestException("El stock no puede quedar por debajo de 0");
        }
        producto.setStock(nuevoStock);
        return producto;
    }

    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        productos.remove(producto);
    }

    private Producto buscarPorId(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id " + id));
    }
}