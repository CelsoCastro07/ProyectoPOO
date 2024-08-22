package com.example.appcontrolfinanzas.AppPages.Transacciones;

import java.io.Serializable;

public class Ingreso extends IngresoGasto implements Serializable {
    private int codigo;
    public static int ultimoCodigo = 1;

    public Ingreso(String fechaIn, String cat, double v, String d, String fechaFin, Repeticion r){
        super(fechaIn, cat, v, d, fechaFin, r);
        ultimoCodigo ++;
        this.codigo = ultimoCodigo;

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
        return String.format("%-8s %-15s %-15s %-15s %-15s %-15s %-15s", codigo, fechaIn, categoria, valorNeto, descripcion, fechaFin , repeticion );
    }
}
