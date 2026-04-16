
package com.example.inventario.controlador;

import com.example.inventario.modelo.Producto;
import com.example.inventario.servicio.ProductoService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

//Esta clase es el controlador de la ventana del Formulario
// (crea un producto nuevo y edita un producto ya existente)
public class FormController {

    //Conectamos los campos de texto del form con el fxml (Aquí se guarda lo que el usuario escribe)
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtStock;
    @FXML private TextField txtCategoria;
    @FXML private Label lblTitulo;

    // Servicio que guarda los productos
    private ProductoService servicio;

    // Si lo editamos aquí se guarda el producto original
    private Producto productoEditar;

    private boolean esEdicion = false;

    //Guardamos el servicio de MainController
    public void setServicio(ProductoService servicio) {
        this.servicio = servicio;
    }

    //Si editamos, llenamos el formulario con los datos del producto
    public void setProductoEditar(Producto producto) {
        this.productoEditar = producto;
        this.esEdicion = true;

        txtCodigo.setText(producto.getCodigo());
        txtCodigo.setDisable(true); //evita que se pueda cambiar el código al editar
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtStock.setText(String.valueOf(producto.getStock()));
        txtCategoria.setText(producto.getCategoria());
        lblTitulo.setText("Editar Producto");
    }

    //Tomamos los datos escritos por el usuario y los guardamos
    @FXML
    private void onGuardar() {
        //Leemos lo que escribió el usuario
        String codigo    = txtCodigo.getText().trim();
        String nombre    = txtNombre.getText().trim();
        String categoria = txtCategoria.getText().trim();

        //Convertimos precio y stock a número.
        //Si el usuario escribió un valor no numérico, mandamos un mensaje de error.
        double precio;
        int stock;
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El precio debe ser un número (ejemplo: 25.50).");
            return;
        }
        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El stock debe ser un número entero (ejemplo: 100).");
            return;
        }

        // Creamos el producto con los datos
        Producto producto = new Producto(codigo, nombre, precio, stock, categoria);

        //Validamos que los datos están bien
        String errores = servicio.validar(producto, !esEdicion);
        if (!errores.isEmpty()) {
            mostrarAlerta("Error de validación", errores);
            return;
        }

        //Actualizamos si se editó
        //Agregamos si es un registro nuevo
        boolean exito = esEdicion
                ? servicio.actualizar(productoEditar, producto)
                : servicio.agregar(producto);

        if (exito) {
            cerrarVentana();
        } else {
            mostrarAlerta("Error", "No se pudo guardar el producto.");
        }
    }

    //Con el botón Cancelar cerramos la ventana sin guardar
    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    // Cierra la ventana del formulario
    private void cerrarVentana() {
        Stage ventana = (Stage) txtCodigo.getScene().getWindow();
        ventana.close();
    }

    // Mostramos un mensaje de error al usuario
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}