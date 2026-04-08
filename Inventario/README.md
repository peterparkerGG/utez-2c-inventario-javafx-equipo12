# Sistema de Inventario de Productos

## Descripción del sistema
Este es un sistema de escritorio desarrollado en JavaFX para la administración de inventario de una tienda local. Permite realizar operaciones CRUD (Alta, Consulta, Actualización y Eliminación) de productos, con persistencia en un archivo local CSV.

## Cómo ejecutar
1. Asegúrate de tener instalado Java 17 y Maven.
2. Abre una terminal en la carpeta raíz del proyecto.
3. Ejecución con Maven:
   ```bash
   mvn clean javafx:run
   ```

## Explicación del archivo de datos
- **Ubicación:** `datos/productos.csv`
- **Formato:** Archivo de texto plano (CSV) separado por comas con el siguiente encabezado:
  `codigo,nombre,precio,stock,categoria`
- **Persistencia:** Los cambios realizados en la aplicación se guardan automáticamente en este archivo cada vez que se agrega, edita o elimina un producto.
