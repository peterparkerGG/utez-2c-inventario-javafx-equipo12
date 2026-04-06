module com.example.inventariointegradora {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.inventariointegradora.controller to javafx.fxml;

    opens com.example.inventariointegradora.model to javafx.base;

    exports com.example.inventariointegradora;
    exports com.example.inventariointegradora.controller;
    exports com.example.inventariointegradora.model;
}