package com.pjapp.appmascshop.Model;

public class CarritoModel {
    private String idProducto;
    private String producto;
    private Double precio;
    private Integer cantidad;

    //Constructor
    public CarritoModel(String idProducto, String producto, Double precio, Integer cantidad) {
        this.idProducto = idProducto;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
