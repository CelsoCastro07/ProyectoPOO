package com.example.appcontrolfinanzas.AppPages.Controladores;

import java.util.ArrayList;
import java.util.Scanner;

public class CategoriaControl {
    private ArrayList<String> ingresos;
    private ArrayList<String> gastos;

    public CategoriaControl() {
            ingresos = new ArrayList<>();
            gastos = new ArrayList<>();
    }

    public ArrayList<String> getListaIngresos() {
            return ingresos;
    }

    public ArrayList<String> getListaGastos() {
        return gastos;
    }

    public void setListaIngresos(ArrayList<String> lstin){
        ingresos = lstin;
    }

    public void setListaGastos(ArrayList<String> lstgas){
        gastos = lstgas;
    }

    public void mostrarMenu() {
        int cod_mas_1 = 1;
        System.out.println("");
        System.out.println("Categorías disponibles para Ingresos: ");
        System.out.println("");
        for (String in : ingresos) {
            System.out.println(cod_mas_1 + ". " + in);
            cod_mas_1++;
        }
        int cod_mas_1_gas = 1;
        System.out.println("");
        System.out.println("Categorías disponibles para Gastos: ");
        System.out.println("");
        for (String gas : gastos) {
            System.out.println(cod_mas_1_gas + ". " + gas);
            cod_mas_1_gas++;
        }
        System.out.println("");
        System.out.println("1. Agregar Categoría");
        System.out.println("2. Eliminar Categoría");
        System.out.println("3. Regresar al Menú Principal");
        System.out.println("Ingrese una opción: ");
    }


    public boolean buscarCategoriaIn(String c) {
        for(String ing : this.getListaIngresos()){
            if(ing.equalsIgnoreCase(c)){
                return true;
            }
        }
        return false;
    }

    private boolean buscarCategoriaGas(String c) {
        for(String ing : this.getListaGastos()){
            if(ing.equalsIgnoreCase(c)){
                return true;
            }
        }
        return false;
    }

    public String agregarCategoriaIn(String cat) {
        if (!buscarCategoriaIn(cat)) {
            ingresos.add(cat);
            return cat + " ha sido agregada.";
        } else {
            return cat + " ya existe en ingreso.";
        }
    }

    public String agregarCategoriaGas(String cat) {
        if (!buscarCategoriaGas(cat)) {
            gastos.add(cat);
            return cat + " ha sido agregada.";
        } else {
            return cat + " ya existe en ingreso.";
        }
    }

    public String eliminarCategoriaIn(String cat) {
        if (buscarCategoriaIn(cat)) {
            ingresos.remove(cat);
            return cat + " ha sido eliminada";
        } else {
            return cat + " no existe";
        }
    }

    public String eliminarCategoriaGas(String cat) {
        if (buscarCategoriaGas(cat)) {
            gastos.remove(cat);
            return cat + " ha sido eliminada";
        } else {
            return cat + " no existe";
        }
    }

    public void agregarCategoria(Scanner sc) {
        sc.nextLine();
        System.out.println("Ingrese tipo de categoria: ");
        String tipo = sc.nextLine();
        while (!tipo.equalsIgnoreCase("Ingresos") && !tipo.equalsIgnoreCase("Gastos")) {
            System.out.println("Ingrese correctamente: ");
            tipo = sc.nextLine();
        }
        System.out.println("Ingrese nombre de categoria: ");
        String nombreCat = sc.nextLine();

        if (tipo.equalsIgnoreCase("Ingresos")) {
            System.out.println(agregarCategoriaIn(nombreCat));
        } else {
            System.out.println(agregarCategoriaGas(nombreCat));
        }

    }

    public void eliminarCategoria(Scanner sc) {
        sc.nextLine();
        System.out.println("Ingrese tipo de categoria: ");
        String tipo = sc.nextLine();
        while (!tipo.equalsIgnoreCase("Ingresos") && !tipo.equalsIgnoreCase("Gastos")) {
            System.out.println("Ingrese correctamente: ");
            tipo = sc.nextLine();
        }
        System.out.println("Ingrese nombre de categoria: ");
        String nombreCat1 = sc.nextLine();

        if (tipo.equalsIgnoreCase("Ingresos")) {
            System.out.println("Seguro que la quiere eliminar(Si o No): ");
            String opcion = sc.nextLine();
            if (opcion.equalsIgnoreCase("Si")) {
                System.out.println(eliminarCategoriaIn(nombreCat1));
            }
        } else {
            System.out.println("Seguro que la quiere eliminar(Si o No): ");
            String opcion = sc.nextLine();
            if (opcion.equalsIgnoreCase("Si")) {
                System.out.println(eliminarCategoriaGas(nombreCat1));

            }
        }
    }
}
