module com.example.inventario {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inventario to javafx.fxml;
    exports com.example.inventario;
}