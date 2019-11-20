package com.example.login;

import java.io.Serializable;

public class productos implements Serializable {
    private int id;
    private String descripcion;
    private String um;
    private double stock;
    private double precio;
    private double cantidad;
    private double subtotal;

    public productos (int id, String descripcion, String um, double stock, double precio, double cantidad, double subtotal){
        this.id=id;
        this.descripcion=descripcion;
        this.um=um;
        this.stock=stock;
        this.precio=precio;
        this.cantidad=cantidad;
        this.subtotal=subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

}
