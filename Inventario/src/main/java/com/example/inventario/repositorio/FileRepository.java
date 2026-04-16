package com.example.inventario.repositorio;

import com.example.inventario.modelo.Producto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//Esta clase nos ayuda como un archivo para almacenar nuestra información en el csv
//Se encarga de la lógica de negocio(editar,validar y buscar)
public class FileRepository {

    // En esta ruta se guardan los productos
    private static final String ARCHIVO = "datos/productos.csv";

    //Esto se reflejará en la primera línea de csv
    private static final String ENCABEZADO = "codigo,nombre,precio,stock,categoria";


    // Este metodo lee el archivo csv y regresa una lista con todos los productos
    // Si el archivo no existe todavía, devuelve una lista vacía
    public List<Producto> cargar() {
        List<Producto> lista = new ArrayList<>();
        File archivo = new File(ARCHIVO);

        //if que sirve por si aún no existe nada guardado (no hay nada que leer)
        if (!archivo.exists()) return lista;

        //intento de leer línea por línea en el archivo csv
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean esPrimeraLinea = true;

            while ((linea = br.readLine()) != null) {
                //Se salta la primera line porque es el ejemplo que pusimos en el archivo de texto (es nuestro encabezado)
                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }

                // Convertimos la línea de texto en un objeto Producto
                Producto p = lineaAProducto(linea);
                if (p != null) lista.add(p);
            }
        } catch (IOException e) {
            // Si algo sale mal mandamos un aviso sin romper el programa.
            System.err.println("No se pudo leer el archivo: " + e.getMessage());
        }

        return lista;
    }

    // Convierte UNA línea del CSV en un objeto Producto y nos aseguramos de que la línea esté bien escrita
    private Producto lineaAProducto(String linea) {
        // Partimos la línea por las comas: ej. "A1,Leche,25.5,10,Lácteos"
        String[] campos = linea.split(",");

        // Validamos que tenga 5 caracteres de longitud
        if (campos.length != 5) return null;

        try {
            return new Producto(
                    campos[0].trim(),                      // código
                    campos[1].trim(),                      // nombre
                    Double.parseDouble(campos[2].trim()),  // precio
                    Integer.parseInt(campos[3].trim()),    // stock
                    campos[4].trim()                       // categoría
            );
        } catch (NumberFormatException e) {
            // Si el precio o el stock no son números válidos, ignoramos la línea
            return null;
        }
    }


    //Guardamos la lista de productos en el archivo csv
    public void guardar(List<Producto> productos) {
        // Si no existe la carpeta "datos", la creamos
        new File("datos").mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO))) {
            bw.write(ENCABEZADO);
            bw.newLine();
            //escribimos un producto por línea
            for (Producto p : productos) {
                bw.write(p.toString()); // Producto.toString() devuelve los atributos del producto
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("No se pudo guardar el archivo: " + e.getMessage());
        }
    }
}

