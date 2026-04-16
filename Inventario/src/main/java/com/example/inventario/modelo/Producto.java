//Esta clase la usamos para guardar los atributos o cualidades del producto
package com.example.inventario.modelo;
public class Producto {

    // Estos son los atributos del producto
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;
    private String categoria;

    // Este es un constructor vacío para poder hacer el primer registro
    public Producto() {
    }

    // Este constructor que recibe todos los datos para el producto
    public Producto(String codigo, String nombre, double precio, int stock, String categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    // Estos son nuestros metodos getters y setters(para leer y cambiar los datos)
    public String getCodigo()    { return codigo; }
    public String getNombre()    { return nombre; }
    public double getPrecio()    { return precio; }
    public int    getStock()     { return stock; }
    public String getCategoria() { return categoria; }


    public void setCodigo(String codigo)       { this.codigo = codigo; }
    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setPrecio(double precio)       { this.precio = precio; }
    public void setStock(int stock)            { this.stock = stock; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    // Este método convierte el producto a una línea de texto y la separa por comas.
    // También sirve para guardar el producto en nuestro archivo CSV.
    @Override
    public String toString() {
        return codigo + "," + nombre + "," + precio + "," + stock + "," + categoria;
    }
}
