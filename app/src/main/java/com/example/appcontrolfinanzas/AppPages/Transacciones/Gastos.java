package com.example.appcontrolfinanzas.AppPages.Transacciones;

public class Gastos extends IngresoGasto{
    private int codigo;
    public static int ultimoCodigo;
    private String fechaFinal;

    public Gastos (String fechaIn, String cat, double v, String d, String fechaFin, Repeticion r){
        super(fechaIn, cat, v, d, fechaFin, r);
        this.codigo = ultimoCodigo +1;
        ultimoCodigo = this.codigo;
    }

    public int getCodigo(){
        return codigo;
    }

    public String getFechaFin(){
        return fechaFin;
    }
    public Repeticion getRepeticion(){
        return repeticion;
    }

    public String toString(){
        return String.format("%-8s %-15s %-15s %-15s %-15s %-15s %-15s", codigo, fechaIn, categoria, valorNeto, descripcion, fechaFin, repeticion );
    }
}
