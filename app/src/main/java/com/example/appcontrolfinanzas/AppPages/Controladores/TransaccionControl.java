package com.example.appcontrolfinanzas.AppPages.Controladores;

import com.example.appcontrolfinanzas.AppPages.Transacciones.Gasto;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Ingreso;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Repeticion;

import java.io.Serializable;
import java.util.*;
import java.time.format.*;
import java.time.*;

public class TransaccionControl implements Serializable {
    private ArrayList<Ingreso> listaIngresos;
    private ArrayList<Gasto> listaGastos;
    private CategoriaControl controlcat;

    public TransaccionControl(CategoriaControl controlcat){
        this.controlcat = controlcat;
        listaIngresos = new ArrayList<>();
        listaGastos = new ArrayList<>();
    }

    public ArrayList<Ingreso> getListaIngresos(){
        return listaIngresos;
    }
    public ArrayList<Gasto> getListaGastos(){
        return listaGastos;
    }

    public void mostrarMenuGas(){
        System.out.printf("%-8s %-15s %-15s %-15s %-15s %-15s %-15s\n", "Codigo", "Fecha inicio", "Categoria", "Valor Neto", "Descripcion", "Fecha fin", "Repeticion");
        System.out.println(new String(new char[110]).replace("\0", "-"));
        for(Gasto gasto :listaGastos){
            System.out.println(gasto);
            System.out.println(new String(new char[110]).replace("\0", "-"));
        }
        System.out.println("");
        System.out.println("1. Agregar Gasto");
        System.out.println("2. Eliminar Gasto");
        System.out.println("3. Finalizar Gasto");
        System.out.println("4. Regresar al menú principal");
        System.out.println("Ingrese una opción: "   );
    }

    /* REGISTRAR CATEGORIAS */
    public void registrar(Scanner sc){
        sc.nextLine();
        System.out.println("Ingrese una categoría: ");
        String categoria = sc.nextLine();
        if(controlcat.getListaIngresos().contains(categoria)){
            registrarIn(categoria);
        }
        if(controlcat.getListaGastos().contains(categoria)){
            registrarGasto(categoria);
        }
        else{
            System.out.println("Categoria no se encuentra.");
        }
    }

    /* REGISTRAR INGRESO */
    private void registrarIn(String cat){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor: ");
        double valor = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Ingrese la descripción: ");
        String descripcion = scanner.nextLine();
        System.out.println("Ingrese la fecha de inicio(dd/MM/yyyy): ");
        String fechaIn = scanner.nextLine();
        while(fechaIn.length() != 10){
            System.out.println("Ingrese con el formato por favor(dd/MM/yyyy): ");
            fechaIn = scanner.nextLine();
        }
        System.out.println("ingrese la repetición (sin repetición, una vez por día, por semana, por mes)");
        String repeticion = scanner.nextLine();

        while(!repeticion.equals("por mes") && !repeticion.equals("por semana") && !repeticion.equals("sin repeticion") && !repeticion.equals("una vez por dia")){
            System.out.println("ingrese correctamente la repetición (sin repetición, una vez por día, por semana, por mes): ");
            repeticion = scanner.nextLine();
        }
        Repeticion r = Repeticion.valueOf(repeticion.replace(" ", "_"));
        System.out.println("Ingrese fecha fin(dd/MM/yyyy)(si no tiene, presione Enter): ");
        String fechaFin = scanner.nextLine();
        if(fechaFin.isEmpty()){
            Ingreso ingreso1 = new Ingreso(fechaIn, cat, valor, descripcion,"No definido", r);
            AggIngreso(ingreso1);
        }
        else{
            while(fechaFin.length() != 10){
                System.out.println("Ingrese con el formato por favor(dd/MM/yyyy): ");
                fechaFin = scanner.nextLine();
            }
            Ingreso ingreso1 = new Ingreso(fechaIn, cat, valor, descripcion, fechaFin, r);
            AggIngreso(ingreso1);
        }
    }

    private void AggIngreso(Ingreso ingreso1){
        if(verificacionInYaHaSidoAgg(ingreso1)){
            System.out.println("Ya se encuentra");
            System.out.println("");
        }
        else{
            listaIngresos.add(ingreso1);
            System.out.println("Ingreso ha sido agregado exitosamente.");
            System.out.println(" ");
        }
    }

    private boolean verificacionInYaHaSidoAgg(Ingreso ingreso1){
        for(Ingreso ingreso: listaIngresos){
            if(ingreso.equals(ingreso1)){
                return true;
            }
        }
        return false;

    }


    /* REGISTRAR GASTO */

    private void registrarGasto(String cat){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el valor: ");
        double valor = scanner.nextDouble();

        scanner.nextLine();

        System.out.println("Ingrese la descripción: ");
        String descripcion = scanner.nextLine();

        System.out.println("Ingrese la fecha de inicio(dd/MM/yyyy): ");
        String fechaIn = scanner.nextLine();
        while(fechaIn.length() != 10){
            System.out.println("Ingrese con el formato por favor(dd/MM/yyyy): ");
            fechaIn = scanner.nextLine();
        }

        System.out.println("ingrese la repetición (sin repetición, una vez por día, por semana, por mes)");
        String repeticion = scanner.nextLine();

        while(!repeticion.equals("por mes") && !repeticion.equals("por semana") && !repeticion.equals("sin repeticion") && !repeticion.equals("una vez por dia")){
            System.out.println("ingrese correctamente la repetición (sin repetición, una vez por día, por semana, por mes): ");
            repeticion = scanner.nextLine();
        }

        Repeticion r = Repeticion.valueOf(repeticion.replace(" ", "_"));

        System.out.println("Ingrese fecha fin (si no tiene, presione Enter): ");
        String fechaFin = scanner.nextLine();

        if(fechaFin.isEmpty()){
            Gasto gasto = new Gasto(fechaIn, cat, valor, descripcion, "No definido", r);
            AggGasto(gasto);
        }
        else{
            Gasto gasto = new Gasto(fechaIn, cat, valor, descripcion, fechaFin, r);
            AggGasto(gasto);
        }
    }

    private void AggGasto(Gasto gasto1){
        if(verificacionGastoYaHaSidoAgg(gasto1)){
            System.out.println("Ya se encuentra");
            System.out.println("");
        }
        else{
            listaGastos.add(gasto1);
            System.out.println("Gasto ha sido agregado exitosamente.");
            System.out.println(" ");
        }
    }

    private boolean verificacionGastoYaHaSidoAgg(Gasto gasto){
        for(Gasto un_gasto: listaGastos){
            if(un_gasto.equals(gasto)){
                return true;
            }
        }
        return false;

    }



    /* ELIMINAR */

    public void eliminarIn(Scanner sc){
        sc.nextLine();
        System.out.println("Ingrese el codigo: ");
        int codigo = sc.nextInt();
        sc.nextLine();
        System.out.println("Si elimina no se tomara en cuenta para el análisis de reportes");
        System.out.println("¿Seguro de eliminarla?(Si o No)");
        String siono = sc.nextLine();
        if(siono.equalsIgnoreCase("si")){
            Iterator<Ingreso> it = listaIngresos.iterator();
            while(it.hasNext()){
                Ingreso ingreso = it.next();
                if(ingreso.getCodigo() == codigo){
                    it.remove();
                    System.out.println("Ingreso removido. ");
                    break;
                }
                else{
                    System.out.println("No se encontro");
                }
            }

        }
        else{
            System.out.println("No se elimina");
            System.out.println(" ");
        }
    }

    public void eliminarGas(Scanner sc){
        sc.nextLine();
        System.out.println("Ingrese el codigo: ");
        int codigo = sc.nextInt();
        sc.nextLine();
        System.out.println("Si elimina no se tomara en cuenta para el análisis de reportes");
        System.out.println("¿Seguro de eliminarla?(Si o No)");
        String siono = sc.nextLine();
        if(siono.equalsIgnoreCase("si")){
            Iterator<Gasto> it = listaGastos.iterator();
            while(it.hasNext()){
                Gasto gasto = it.next();
                if(gasto.getCodigo() == codigo){
                    it.remove();
                    System.out.println("Gasto removido. ");
                    break;
                }
                else{
                    System.out.println("No se encontro");
                }
            }

        }
        else{
            System.out.println("No se elimina");
            System.out.println(" ");
        }
    }


    /* FINALIZAR */

    public boolean validarFecha(LocalDate fechaF, LocalDate fechaIn){
        if(fechaIn.isBefore(fechaF)){
            return true;
        }
        if(fechaIn.isAfter(fechaF)){
            return false;
        }
        else{
            return false;
        }
    }

    /* FINALIZAR INGRESO*/

    public void finalizarIn(Scanner sc){
        sc.nextLine();
        System.out.println("Ingrese el codigo: ");
        int codigo = sc.nextInt();
        sc.nextLine();
        for (Ingreso ingreso : listaIngresos) {
            if(ingreso.getCodigo() == codigo){
                System.out.println("Ingrese fecha de finalizacion(dd/MM/yyyy): ");
                String fechaFin = sc.nextLine();
                while(fechaFin.length() != 10){
                    System.out.println("Ingrese con el formato por favor(dd/MM/yyyy): ");
                    fechaFin = sc.nextLine();
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaNueva = LocalDate.parse(fechaFin, formatter);

                LocalDate fechaIn = LocalDate.parse(ingreso.getFechaIn(),formatter);
                if(validarFecha(fechaNueva, fechaIn)){
                    ingreso.setFechaFin(fechaFin);
                }
                else{
                    System.out.println("La nueva fecha no es valida.");
                }

                break;
            }
            else{
                System.out.println("Codigo no encontrado, intente de nuevo");
            }
        }
    }


    /* FINALIZAR GASTO */
    public void finalizarGasto(Scanner sc){
        sc.nextLine();
        System.out.println("Ingrese el codigo: ");
        int codigo = sc.nextInt();
        sc.nextLine();
        for (Gasto gasto : listaGastos) {
            if(gasto.getCodigo() == codigo){
                System.out.println("Ingrese fecha de finalizacion(dd/MM/yyyy): ");
                String fechaFin = sc.nextLine();
                while(fechaFin.length() != 10){
                    System.out.println("Ingrese con el formato por favor(dd/MM/yyyy): ");
                    fechaFin = sc.nextLine();
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaNueva = LocalDate.parse(fechaFin, formatter);

                LocalDate fechaIn = LocalDate.parse(gasto.getFechaIn(),formatter);
                if(validarFecha(fechaNueva, fechaIn)){
                    gasto.setFechaFin(fechaFin);
                }
                else{
                    System.out.println("La nueva fecha no es valida.");
                }

                break;
            }
            else{
                System.out.println("Codigo no encontrado, intente de nuevo");
            }
        }
    }
}
