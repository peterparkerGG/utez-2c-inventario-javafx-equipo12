package com.example.inventario.servicio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Esta es la clase que ARRANCA el programa.
// Carga la ventana principal (MainView.fxml) y la muestra.
public class App extends Application {

    // Método que JavaFX llama para mostrar la ventana principal
    @Override
    public void start(Stage ventanaPrincipal) throws Exception {
        // Cargamos el diseño de la ventana desde el archivo FXML
        Parent raiz = FXMLLoader.load(getClass().getResource("/com/example/inventario/MainView.fxml"));

        // Ponemos título y mostramos la ventana
        ventanaPrincipal.setTitle("Inventario de Productos - Tienda");
        ventanaPrincipal.setScene(new Scene(raiz));
        ventanaPrincipal.show();
    }

    // Punto de entrada del programa (Java empieza aquí)
    public static void main(String[] args) {
        launch(args);
    }
}
