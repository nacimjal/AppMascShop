package com.pjapp.appmascshop.Model;

public class DetallePedido {

    private String idDetallePedido;
    private String idPedido;
    private String idProducto;
    private Double precio;
    private Integer cantidad;

    public String getIdDetallePedido() {
        return idDetallePedido;
    }

    public void setIdDetallePedido(String idDetallePedido) {
        this.idDetallePedido = idDetallePedido;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
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
