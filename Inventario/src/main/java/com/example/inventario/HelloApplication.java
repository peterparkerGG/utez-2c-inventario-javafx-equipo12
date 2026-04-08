package com.example.inventario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/inventario/MainView.fxml"));
       Scene scene = new Scene(fxmlLoader.load(), 720, 440);
        stage.setTitle("Sistema de Inventario - Tienda");
        stage.setScene(scene);
        stage.show();
    }
}