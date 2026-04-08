module com.example.inventario {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inventario.controlador to javafx.fxml;


    exports com.example.inventario.controlador;
    exports com.example.inventario.modelo;
    exports com.example.inventario.servicio;
    exports com.example.inventario.repositorio;
}