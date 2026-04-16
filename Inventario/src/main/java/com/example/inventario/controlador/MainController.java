package com.example.inventario.controlador;

import com.example.inventario.modelo.Producto;
import com.example.inventario.servicio.ProductoService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;


//Esta clase se encarga de mostrar la tabla con productos
//reaccionar cuando el usuario escribe en el buscador
//cambia el orden o presiona los botones
public class MainController {

    // Conexiones con los elementos del archivo FXML tabla, columnas, botones, etc
    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String>  colCodigo;
    @FXML private TableColumn<Producto, String>  colNombre;
    @FXML private TableColumn<Producto, Double>  colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String>  colCategoria;
    @FXML private TextField txtBusqueda;
    @FXML private ComboBox<String> cmbOrdenar;

    //Este servicio maneja los productos de guardar, buscar, ordenar, etc
    private final ProductoService servicio = new ProductoService();


    //Preparamos la tabla y los controles, este metodo se ejecuta automaticamente
    @FXML
    public void initialize() {
        // Decimos a cada columna qué dato del Producto debe mostrar
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        // Llenamos el combo con las opciones para ordenar
        cmbOrdenar.setItems(FXCollections.observableArrayList(
                "Sin ordenar", "Nombre A-Z", "Precio menor a mayor", "Precio mayor a menor"
        ));
        cmbOrdenar.setValue("Sin ordenar");

        // Mostramos los productos en la tabla
        tablaProductos.setItems(servicio.getProductos());

        // Cuando el usuario escribe en el buscador o cambia el orden,
        // volvemos a filtrar y ordenar la tabla.
        txtBusqueda.textProperty().addListener((obs, a, b) -> filtrarYOrdenar());
        cmbOrdenar.valueProperty().addListener((obs, a, b) -> filtrarYOrdenar());
    }

    // Aplica el texto de búsqueda y el orden seleccionado, y actualiza la tabla
    private void filtrarYOrdenar() {
        String texto = txtBusqueda.getText();

        //Filtramos, cuando no hay texto usamos toda la lista
        //si hya texto entonces buscamos
        List<Producto> resultado;
        if (texto == null || texto.trim().isEmpty()) {
            resultado = servicio.getProductos();
        } else {
            resultado = servicio.buscar(texto);
        }

        //Ordenamos según la opción elegida en el combo
        String orden = cmbOrdenar.getValue();
        if ("Nombre A-Z".equals(orden)) {
            resultado = servicio.ordenarPorNombre(resultado);
        } else if ("Precio menor a mayor".equals(orden)) {
            resultado = servicio.ordenarPorPrecioAsc(resultado);
        } else if ("Precio mayor a menor".equals(orden)) {
            resultado = servicio.ordenarPorPrecioDesc(resultado);
        }

        //Mostramos el resultado final en la tabla
        tablaProductos.setItems(FXCollections.observableArrayList(resultado));
    }

    // Este boton abre el formulario vacío para crear un producto
    @FXML
    private void onNuevo() {
        abrirFormulario(null);
    }

    // Este boton abre el formulario con los datos del producto seleccionado
    @FXML
    private void onEditar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida",
                    "Selecciona un producto de la tabla para editar.",
                    Alert.AlertType.WARNING);
            return;
        }
        abrirFormulario(seleccionado);
    }

    // BPor ultimo este otro, pide confirmación y borra el producto seleccionado
    @FXML
    private void onEliminar() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida",
                    "Selecciona un producto de la tabla para eliminar.",
                    Alert.AlertType.WARNING);
            return;
        }

        //Advertimos al usuario para saber si desea eliminar
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que quieres eliminar \"" + seleccionado.getNombre() + "\"?");

        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.OK) {
            servicio.eliminar(seleccionado);
            filtrarYOrdenar();
        }
    }

    // Boton encargado de limpiar el buscador y mostrar todos los productos otra vez
    @FXML
    private void onRecargar() {
        txtBusqueda.clear();
        cmbOrdenar.setValue("Sin ordenar");
        tablaProductos.setItems(servicio.getProductos());
    }


    //Este metodo abre la ventana del form para crear o editar, validamos los nulos
    private void abrirFormulario(Producto producto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/inventario/FormView.fxml"));
            Parent raiz = loader.load();

            // Le mandammos al formulario el servicio y si es edición el producto
            FormController controller = loader.getController();
            controller.setServicio(servicio);
            if (producto != null) {
                controller.setProductoEditar(producto);
            }

            // Creamos una ventana emergente
            Stage ventana = new Stage();
            ventana.setTitle(producto == null ? "Nuevo Producto" : "Editar Producto");
            ventana.setScene(new Scene(raiz));
            ventana.initModality(Modality.APPLICATION_MODAL); // bloquea la ventana principal
            ventana.setResizable(false);
            ventana.showAndWait();

            // Al cerrar, actualizamos la tabla
            filtrarYOrdenar();

        } catch (Exception e) {
            mostrarAlerta("Error",
                    "No se pudo abrir el formulario: " + e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    // Mostramos un mensaje al usuario
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
