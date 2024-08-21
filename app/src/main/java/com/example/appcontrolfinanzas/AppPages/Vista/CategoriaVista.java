package com.example.appcontrolfinanzas.AppPages.Vista;

import com.example.appcontrolfinanzas.AppPages.Controladores.* ;
import java.util.*;

public class CategoriaVista {
    private CategoriaControl control;

    public CategoriaVista(CategoriaControl control){
        this.control = control;

    }

    public void ejecucion(){
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        do{
            control.mostrarMenu();
            opcion = sc.nextInt();
            switch(opcion){
                case 1:
                    control.agregarCategoria(sc);
                    break;
                case 2:
                    control.eliminarCategoria(sc);
                    break;
                case 3:
                    System.out.print("Aqui se regresa al menú principal inicial");
                    break;
                default:
                    System.out.print("Opción no válida");

            }
        }while(opcion != 3);

    }
}
