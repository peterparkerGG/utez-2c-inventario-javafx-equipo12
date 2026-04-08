module com.example.inventario {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.inventario to javafx.fxml;
    opens com.example.inventario.controlador to javafx.fxml;
    opens com.example.inventario.modelo to javafx.base;

    exports com.example.inventario;
    exports com.example.inventario.controlador;
    exports com.example.inventario.modelo;
    exports com.example.inventario.servicio;
    exports com.example.inventario.repositorio;
}
