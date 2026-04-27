package com.example.inventario.servicio;


import com.example.inventario.modelo.Producto;
import com.example.inventario.repositorio.FileRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


//Esta clase se encarga de agregar, editar, eliminar, buscar y ordenar productos
//Además revisa que los datos sean correctos antes de guardarlos
public class ProductoService {

    // Este es nuestro almacen que lee y guarda el archivo csv
    private final FileRepository repositorio;


    //Lista observable y se actualiza sola cuando cambia
    private final ObservableList<Producto> productos;

    // Cuando creamos el servicio, cargamos los productos desde el archivo de texto
    public ProductoService() {
        this.repositorio = new FileRepository();
        this.productos = FXCollections.observableArrayList(repositorio.cargar());
    }

    // Devuelve la lista de productos para mostrarla en la tabla
    public ObservableList<Producto> getProductos() {
        return productos;
    }

    // Atajo para pedirle al repositorio que guarde el estado actual
    private void guardar() {
        repositorio.guardar(productos);
    }



    //Estas son nuestras validaciones, cuendo todo sale bien regresa un texto vacio
    // y si hay errores los devuelve en una lista
    public String validar(Producto p, boolean esNuevo) {
        StringBuilder errores = new StringBuilder();

        // Regla para Nombre: Solo letras y espacios (puedes incluir acentos si gustas)
        // El símbolo ^ significa "inicio", [a-zA-ZáéíóúÁÉÍÓÚñÑ ]+ significa "estas letras y espacios", y $ significa "fin"
        String regexNombre = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

        // Regla para Categoría: Solo letras (sin números ni símbolos)
        String regexCategoria = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

        // Validaciones de Nombre
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            errores.append("- El nombre es obligatorio.\n");
        } else if (!p.getNombre().matches(regexNombre)) {
            errores.append("- El nombre no puede contener números ni caracteres especiales.\n");
        }

        // Validaciones de Categoría
        if (p.getCategoria() == null || p.getCategoria().trim().isEmpty()) {
            errores.append("- La categoría es obligatoria.\n");
        } else if (!p.getCategoria().matches(regexCategoria)) {
            errores.append("- La categoría solo debe contener texto (sin números).\n");
        }

        //Mantenemos validaciones de id, precio y stock

        return errores.toString();
    }

    // Validacion de texto vacio o nulo
    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    // Nos avisa si el codigo esta repetido
    public boolean existeCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) return true;
        }
        return false;
    }


    // Agrega un producto nuevo a la lista y guarda el archivo
    public boolean agregar(Producto producto) {
        if (!validar(producto, true).isEmpty()) return false;
        productos.add(producto);
        guardar();
        return true;
    }

    // Reemplaza un producto existente por otro con los datos nuevos
    public boolean actualizar(Producto original, Producto actualizado) {
        if (!validar(actualizado, false).isEmpty()) return false;
        int i = productos.indexOf(original);
        if (i < 0) return false;
        productos.set(i, actualizado);
        guardar();
        return true;
    }

    // Quita un producto de la lista y guarda el archivo
    public boolean eliminar(Producto producto) {
        if (!productos.remove(producto)) return false;
        guardar();
        return true;
    }


    //Buscamos productos mediante su codigo o nombre
    public List<Producto> buscar(String texto) {
        String q = texto.toLowerCase().trim();
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            if (p.getCodigo().toLowerCase().contains(q)
                    || p.getNombre().toLowerCase().contains(q)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // Devuelve la lista ordenada por nombre (A-Z)
    public List<Producto> ordenarPorNombre(List<Producto> lista) {
        List<Producto> copia = new ArrayList<>(lista);
        copia.sort(Comparator.comparing(p -> p.getNombre().toLowerCase()));
        return copia;
    }

    // Devuelve la lista ordenada del precio más barato al más caro
    public List<Producto> ordenarPorPrecioAsc(List<Producto> lista) {
        List<Producto> copia = new ArrayList<>(lista);
        copia.sort(Comparator.comparingDouble(Producto::getPrecio));
        return copia;
    }

    // Devuelve la lista ordenada del precio más caro al más barato
    public List<Producto> ordenarPorPrecioDesc(List<Producto> lista) {
        List<Producto> copia = new ArrayList<>(lista);
        copia.sort(Comparator.comparingDouble(Producto::getPrecio).reversed());
        return copia;
    }
}
