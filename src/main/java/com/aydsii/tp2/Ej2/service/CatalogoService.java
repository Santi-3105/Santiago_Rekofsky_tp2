package com.aydsii.tp2.Ej2.service;

import com.aydsii.tp2.BadRequestException;
import com.aydsii.tp2.ResourceNotFoundException;
import com.aydsii.tp2.Ej2.dto.ProductoDTO;
import com.aydsii.tp2.Ej2.model.Producto;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


//Indica que es de tipo Serivce, es algo que se instanciara una sola vez y se utilize durante la ejecucion
@Service
public class CatalogoService {


    //Una coleccion dentro del ejercicio, tal y como se pide ya que no utilizaremos bdd por ahora. Solo se elimina si reiniciamos la app
    private final List<Producto> productos = new ArrayList<>();
    //Uso atomic por si dos productos se meten a la vez, no van a compartir ID. Si usara solo long podria pasar
    private final AtomicLong secuenciaId = new AtomicLong(0);

    //Se ejecuta este metodo automaticamente una sola vez, justo despues de que se construya todo, incluido las dependencias. 
    //Cumple el requisito del tp que se cargen 8 productos de ejemplo al iniciar la app.
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
        //El stream lo usaremos para "conectar", es decir es una secuencia de elementos sobre las cuales se hacen operaciones encadenadas
        return productos.stream()
                //Los filtros los cuales pueden ser combinados o directamente no poner ninguno. Por eso esta la opcion de que sea null o contenga algo y de ahi la comparacion
                .filter(p -> categoria == null || p.getCategoria().equalsIgnoreCase(categoria))
                .filter(p -> precioMin == null || p.getPrecio() >= precioMin)
                .filter(p -> precioMax == null || p.getPrecio() <= precioMax)
                .collect(Collectors.toList());
    }

    public List<Producto> ordenar(String criterio, String orden) {
        Comparator<Producto> comparator = switch (criterio) {
            //Se construye un comparador que comparara el "precio" con el precio obteneido del producto
            case "precio" -> Comparator.comparingDouble(Producto::getPrecio);
            case "nombre" -> Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER);
            default -> throw new BadRequestException("El criterio debe ser 'precio' o 'nombre'");
        };

        if ("desc".equalsIgnoreCase(orden)) {
            //Utilizo un reversed para el orden, basicamente reutilizo el comparator hecho arriba y lo doy vuelta
            comparator = comparator.reversed();
        } else if (!"asc".equalsIgnoreCase(orden)) {
            throw new BadRequestException("El orden debe ser 'asc' o 'desc'");
        }

        return productos.stream()
                //Genera una lista nueva, ordenada pero deja la lista orginal intacta dada por su orden de insercion
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    //Alta de un producto, el id se genera apartir de ese secuenciaId.IncrementAndGet
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
        //Busca el id, basicamente obtiene el id del producto y lo compara, si son iguales lo encuentra y lo devuelve
        //En cuyo casi sea null no tira direcamente el null sino que hace una instancia de una Exception, algo mas intuitivo de saber que fallo
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No existe un producto con id " + id));
    }
}