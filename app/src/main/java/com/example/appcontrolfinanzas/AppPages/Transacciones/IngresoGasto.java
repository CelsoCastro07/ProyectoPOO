package com.example.appcontrolfinanzas.AppPages.Transacciones;

import java.io.Serializable;

public class IngresoGasto implements Serializable {
    protected String fechaIn;
    protected String categoria;
    protected double valorNeto;
    protected String descripcion;
    protected String fechaFin;
    protected Repeticion repeticion;

    public IngresoGasto(String fechaIn, String categoria, double valor, String descripcion, String fechaFin, Repeticion r){
        this.fechaIn = fechaIn;
        this.categoria = categoria;
        valorNeto = valor;
        this.descripcion = descripcion;
        this.fechaFin = fechaFin;
        repeticion = r;
    }


    public void setFechaFin(String fechaFin){
        this.fechaFin = fechaFin;
    }

    public String getFechaIn(){
        return fechaIn;
    }

    public String getCategoria(){
        return categoria;
    }

    public double getValor(){
        return valorNeto;
    }

    public  String getDescripcion(){
        return descripcion;
    }
}
