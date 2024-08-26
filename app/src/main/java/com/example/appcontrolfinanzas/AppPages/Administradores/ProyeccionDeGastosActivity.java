package com.example.appcontrolfinanzas.AppPages.Administradores;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.AppPages.Transacciones.Gasto;
import com.example.appcontrolfinanzas.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;

public class ProyeccionDeGastosActivity extends AppCompatActivity {
    public String fileNameGastos = "Gastos.txt";
    public String fileNameCategorias = "categoriasGasto.txt";
    private ArrayList<Gasto> lstGasto;
    private ArrayList<String> lstCat;

    private Button buttonProyectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_proyeccion_de_gastos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonProyectar = findViewById(R.id.buttonProyeccionGasto);

        cargarGastosDeArchivo(fileNameGastos);
        cargarCategoriasGastosDeArchivo(fileNameCategorias);

        buttonProyectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(ProyeccionDeGastosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_proyeccion_gasto, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ProyeccionDeGastosActivity.this);
                builder.setView(dialogView).setTitle("Proyeccion De Gasto").create().show();

                TableLayout tableLayoutProyeccion = dialogView.findViewById(R.id.tableLayoutProyeccionGas);
                TextView mes1 = dialogView.findViewById(R.id.Mes1);
                TextView mes2 = dialogView.findViewById(R.id.Mes2);
                TextView mesActual = dialogView.findViewById(R.id.MesActual);
                TextView mes4 = dialogView.findViewById(R.id.Mes4);


                try{proyeccionGastos(tableLayoutProyeccion, lstGasto, lstCat, mes1, mes2, mesActual, mes4);
                }catch(Exception e){
                    Toast.makeText(ProyeccionDeGastosActivity.this, "Error 🥶", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void proyeccionGastos(TableLayout tabla, ArrayList<Gasto> gastos, ArrayList<String> categorias, TextView mes1, TextView mes2, TextView mesActual, TextView mes4) {
        Date fechaActual = new Date();
        int numMesActual = fechaActual.getMonth() + 1;

        int numMes1 = numMesActual -2;
        int numMes2 = numMesActual - 1;
        mes1.setText(obtenerNombreMes(numMes1));
        mes2.setText(obtenerNombreMes(numMes2));
        mesActual.setText(obtenerNombreMes(numMesActual));
        mes4.setText(obtenerNombreMes(numMesActual +1));

        for(String categoria : categorias){
            double valorMes1 = funcion1(numMes1,categoria, gastos);
            double valorMes2 = funcion1(numMes2,categoria, gastos);
            double valorMesActual = funcion1(numMesActual,categoria, gastos);

            double proyeccionProxMes = funcion2(valorMes1, valorMes2, valorMesActual);
            TableRow row1 = new TableRow(this);
            row1.addView(createTextView(categoria));
            row1.addView(createTextView(String.valueOf(valorMes1)));
            row1.addView(createTextView(String.valueOf(valorMes2)));
            row1.addView(createTextView(String.valueOf(valorMesActual)));
            row1.addView(createTextView(String.valueOf(proyeccionProxMes)));
            tabla.addView(row1);
        }

    }
    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(11, 11, 11, 11);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private double funcion2(double num1, double num2, double num3){
        return (num1 + num2 + num3)/3;
    }

    private double funcion1(int mes, String categoria, ArrayList<Gasto> gastos) {
        Date fechaActual = new Date();
        int numAnioActual = fechaActual.getYear() + 1900;
        double valor1 = 0;

        for(Gasto gasto : gastos){
            String[] listaFecha = gasto.getFechaIn().split("/");
            int numMes = Integer.parseInt(listaFecha[1]);

            if(numMes == mes && Integer.parseInt(listaFecha[2]) == numAnioActual && categoria.equals(gasto.getCategoria())){
                valor1 += gasto.getValor();
            }
        }
        return valor1;
        }



    private String obtenerNombreMes(int mes){
        String[] nombresMeses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return nombresMeses[mes - 1];
    }

    private void cargarCategoriasGastosDeArchivo(String fileNameCategorias) {
        try {
            File file = new File(getFilesDir(), fileNameCategorias);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lstCat = (ArrayList<String>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                // Archivo no encontrado, mostrar un Toast
                lstCat = null;
                Toast.makeText(this, "No se han añadido categorias, vuelvaPronto.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Mostrar un Toast en caso de error y inicializar lista con datos predeterminados
            Toast.makeText(this, "Error al cargar archivo.", Toast.LENGTH_LONG).show();
        }
    }

    private void cargarGastosDeArchivo(String fileNameGastos) {
        try {
            File file = new File(getFilesDir(), fileNameGastos);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lstGasto = (ArrayList<Gasto>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                // Archivo no encontrado, mostrar un Toast
                lstGasto = null;
                Toast.makeText(this, "No se han añadido gastos, vuelva cuando añada pls.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Mostrar un Toast en caso de error y inicializar lista con datos predeterminados
            Toast.makeText(this, "Error al cargar archivo.", Toast.LENGTH_LONG).show();
        }

    }
}