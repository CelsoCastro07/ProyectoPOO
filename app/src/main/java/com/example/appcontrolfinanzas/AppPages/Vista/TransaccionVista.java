package com.example.appcontrolfinanzas.AppPages.Vista;
import com.example.appcontrolfinanzas.AppPages.Controladores.TransaccionControl;
import java.util.Scanner;

public class TransaccionVista {
    private TransaccionControl inGascontrol;

    public TransaccionVista(TransaccionControl inGascontrol){
        this.inGascontrol = inGascontrol;
    }

    public void ejecucionIn(){
        Scanner sc = new Scanner(System.in);
        int opcion1 = 0;
        do{
            opcion1 = sc.nextInt();
            switch(opcion1){
                case 1:
                    inGascontrol.registrar(sc);
                    break;
                case 2:
                    inGascontrol.eliminarIn(sc);
                    break;
                case 3:
                    inGascontrol.finalizarIn(sc);
                    break;
                case 4:
                    System.out.println("Regresar al menú. ");
                    break;
                default:
                    System.out.print("Opción no válida");
                    break;
            }
        }while(opcion1 != 4);

    }


    public void ejecucionGasto(){
        Scanner sc = new Scanner(System.in);
        int opcion1 = 0;
        do{
            inGascontrol.mostrarMenuGas();
            opcion1 = sc.nextInt();
            switch(opcion1){
                case 1:
                    inGascontrol.registrar(sc);
                    break;
                case 2:
                    inGascontrol.eliminarGas(sc);
                    break;
                case 3:
                    inGascontrol.finalizarGasto(sc);
                    break;
                case 4:
                    System.out.println("Regresar al menú. ");
                    break;
                default:
                    System.out.print("Opción no válida");
                    break;
            }
        }while(opcion1 != 4);

    }
}
