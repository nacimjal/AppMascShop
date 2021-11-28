package com.pjapp.appmascshop.Model;

public class Pedido {
    private String idPedido;
    private String numPedido;
    private String cliente;
    private String fechEmision;
    private Double subtotal;
    private Double igv;
    private Double total;
    private String evidenciaPago;
    private String nombreCliente;

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFechEmision() {
        return fechEmision;
    }

    public void setFechEmision(String fechEmision) {
        this.fechEmision = fechEmision;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEvidenciaPago() {
        return evidenciaPago;
    }

    public void setEvidenciaPago(String evidenciaPago) {
        this.evidenciaPago = evidenciaPago;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
