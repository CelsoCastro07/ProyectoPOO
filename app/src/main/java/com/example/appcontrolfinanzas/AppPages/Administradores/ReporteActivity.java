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
import com.example.appcontrolfinanzas.AppPages.Transacciones.Ingreso;
import com.example.appcontrolfinanzas.AppPages.Transacciones.IngresoGasto;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Repeticion;
import com.example.appcontrolfinanzas.R;
import com.google.android.material.chip.Chip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReporteActivity extends AppCompatActivity {
    private Button buttonreporteMensual, buttonreporteAnual;
    private Chip chipIngreso, chipGasto;

        private String tipoTransaccion;
        public String fileNameIngreso = "ingresos.txt";
        public String fileNameGasto = "Gastos.txt";
        private ArrayList<Ingreso> lstIngreso;
        private ArrayList<Gasto> lstGasto;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_reporte);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
            buttonreporteMensual = findViewById(R.id.buttonReporteMensual);
            buttonreporteAnual = findViewById(R.id.buttonReporteAnual);
            chipIngreso = findViewById(R.id.chipIngresos);
            chipGasto = findViewById(R.id.chipGastos);

            cargarIngresos(fileNameIngreso);
            cargarGastos(fileNameGasto);


            chipIngreso.setOnClickListener(v -> {tipoTransaccion = "Ingreso";
            chipGasto.setChecked(false);
        });
        chipGasto.setOnClickListener(v-> {tipoTransaccion = "Gasto";
            chipIngreso.setChecked(false);
        });

        buttonreporteMensual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (tipoTransaccion == null || tipoTransaccion.isEmpty()) {
                    Toast.makeText(ReporteActivity.this, "Tipo no válido", Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflater = LayoutInflater.from(ReporteActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_reporte_mensual, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReporteActivity.this);
                    builder.setView(dialogView).setTitle("Reportes Mensuales").create().show();

                    TextView mesActual = dialogView.findViewById(R.id.mesActual);
                    TableLayout tableLayoutReporteMensual = dialogView.findViewById(R.id.tableLayoutReporteMensual);

                    if(tipoTransaccion.equalsIgnoreCase("Ingreso")){
                        mostrarDatosDeReporteMesIn(lstIngreso, tableLayoutReporteMensual, mesActual);
                    }

                    else if(tipoTransaccion.equalsIgnoreCase("Gasto")){
                        mostrarDatosDeReporteMesGasto(lstGasto, tableLayoutReporteMensual, mesActual);
                    }
            }
        });

        buttonreporteAnual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipoTransaccion == null || tipoTransaccion.isEmpty()) {
                    Toast.makeText(ReporteActivity.this, "Tipo no válido", Toast.LENGTH_SHORT).show();
                    return;
                }
                LayoutInflater inflador = LayoutInflater.from(ReporteActivity.this);
                View dialogView = inflador.inflate(R.layout.dialog_reporte_anual, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ReporteActivity.this);
                builder.setView(dialogView).setTitle("Reportes Anuales").create().show();

                TableLayout tableLayoutReporteAnual = dialogView.findViewById(R.id.tableLayoutReporteAnio);

                if(tipoTransaccion.equalsIgnoreCase("Ingreso")){
                    mostrarDatosDeReporteAnioIn(lstIngreso, tableLayoutReporteAnual);
                }

                else if(tipoTransaccion.equalsIgnoreCase("Gasto")){
                    mostrarDatosDeReporteAnioGasto(lstGasto, tableLayoutReporteAnual);
                }
            }
        });


    }

    private void mostrarDatosDeReporteAnioGasto(ArrayList<Gasto> lista, TableLayout tabla) {
        double valorNetoFinAnio = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar fecha = Calendar.getInstance();
        fecha.set(2024, Calendar.JANUARY, 1);

        Calendar fechaLimite = new GregorianCalendar(2500, Calendar.DECEMBER, 25);

        while (fecha.before(new GregorianCalendar(2024, Calendar.DECEMBER, 30))) {
            for(Gasto gasto:lista) {
                try {
                    Date fechaIngreso = format.parse(gasto.getFechaIn());
                    Calendar calendarFechaIng = Calendar.getInstance();
                    calendarFechaIng.setTime(fechaIngreso);

                    if ((calendarFechaIng.get(Calendar.MONTH)+1) == (fecha.get(Calendar.MONTH) + 1) && calendarFechaIng.get(Calendar.YEAR) == fecha.get(Calendar.YEAR) && gasto.getRepeticion().equals(Repeticion.sin_repeticion)) {
                        TableRow row1 = new TableRow(this);
                        row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row1.addView(createTextView2(obtenerNombreMes(fecha.get(Calendar.MONTH) + 1)));
                        row1.addView(createTextView2(gasto.getCategoria()));
                        row1.addView(createTextView2(String.valueOf(gasto.getValor())));
                        tabla.addView(row1);
                        valorNetoFinAnio += gasto.getValor();
                    }
                    if(gasto.getRepeticion().equals(Repeticion.por_mes)){
                        if(!gasto.getFechaFin().equalsIgnoreCase("No definido")){
                            try {
                                Date fechaFinIng = format.parse(gasto.getFechaFin());
                                Calendar calendarFehcaFinIng = Calendar.getInstance();
                                calendarFehcaFinIng.setTime(fechaFinIng);

                                if(calendarFechaIng.before(fecha) && fecha.before(calendarFehcaFinIng)){
                                    TableRow row2 = new TableRow(this);
                                    row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    row2.addView(createTextView2(obtenerNombreMes(fecha.get(Calendar.MONTH) + 1)));
                                    row2.addView(createTextView2(gasto.getCategoria()));
                                    row2.addView(createTextView2(String.valueOf(gasto.getValor())));
                                    tabla.addView(row2);
                                    valorNetoFinAnio += gasto.getValor();
                                }
                            }catch(Exception e){
                                Toast.makeText(ReporteActivity.this, "Fecha Fin no valida", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(fecha.before(fechaLimite)){
                                TableRow row3 = new TableRow(this);
                                row3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                row3.addView(createTextView2(obtenerNombreMes(fecha.get(Calendar.MONTH) + 1)));
                                row3.addView(createTextView2(gasto.getCategoria()));
                                row3.addView(createTextView2(String.valueOf(gasto.getValor())));
                                tabla.addView(row3);
                                valorNetoFinAnio += gasto.getValor();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ReporteActivity.this, "Fecha inicio no valida", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
            fecha.add(Calendar.MONTH, 1);
        }
        TableRow rowFinal = new TableRow(this);
        rowFinal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        rowFinal.addView(createTextView2("Total"));
        rowFinal.addView(createTextView2("    "));
        rowFinal.addView(createTextView2(String.valueOf(valorNetoFinAnio)));
        tabla.addView(rowFinal);
    }

    private void mostrarDatosDeReporteAnioIn(ArrayList<Ingreso> lista, TableLayout tabla) {
        double valorNetoFinAnio = 0;

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Calendar fecha = Calendar.getInstance();
        fecha.set(2024, Calendar.JANUARY, 1);

        Calendar fechaLimite = new GregorianCalendar(2500, Calendar.DECEMBER, 25);

        while (fecha.before(new GregorianCalendar(2024, Calendar.DECEMBER, 30))) {
            for(Ingreso ingreso:lista) {
                try {
                    Date fechaIngreso = format.parse(ingreso.getFechaIn());
                    Calendar calendarFechaIng = Calendar.getInstance();
                    calendarFechaIng.setTime(fechaIngreso);

                    if ((calendarFechaIng.get(Calendar.MONTH)+1) == (fecha.get(Calendar.MONTH) + 1) && calendarFechaIng.get(Calendar.YEAR) == fecha.get(Calendar.YEAR) && ingreso.getRepeticion().equals(Repeticion.sin_repeticion)) {
                        TableRow row1 = new TableRow(this);
                        row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        row1.addView(createTextView2(obtenerNombreMes(fecha.get(Calendar.MONTH) + 1)));
                        row1.addView(createTextView2(ingreso.getCategoria()));
                        row1.addView(createTextView2(String.valueOf(ingreso.getValor())));
                        tabla.addView(row1);
                        valorNetoFinAnio += ingreso.getValor();
                    }
                    if(ingreso.getRepeticion().equals(Repeticion.por_mes)){
                        if(!ingreso.getFechaFin().equalsIgnoreCase("No definido")){
                            try {
                                Date fechaFinIng = format.parse(ingreso.getFechaFin());
                                Calendar calendarFehcaFinIng = Calendar.getInstance();
                                calendarFehcaFinIng.setTime(fechaFinIng);

                                if(calendarFechaIng.before(fecha) && fecha.before(calendarFehcaFinIng)){
                                    TableRow row2 = new TableRow(this);
                                    row2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                    row2.addView(createTextView2(obtenerNombreMes(fecha.get(Calendar.MONTH) + 1)));
                                    row2.addView(createTextView2(ingreso.getCategoria()));
                                    row2.addView(createTextView2(String.valueOf(ingreso.getValor())));
                                    tabla.addView(row2);
                                    valorNetoFinAnio += ingreso.getValor();
                                }
                            }catch(Exception e){
                                Toast.makeText(ReporteActivity.this, "Fecha Fin no valida", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(fecha.before(fechaLimite)){
                                TableRow row3 = new TableRow(this);
                                row3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                                row3.addView(createTextView2(obtenerNombreMes(fecha.get(Calendar.MONTH) + 1)));
                                row3.addView(createTextView2(ingreso.getCategoria()));
                                row3.addView(createTextView2(String.valueOf(ingreso.getValor())));
                                tabla.addView(row3);
                                valorNetoFinAnio += ingreso.getValor();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ReporteActivity.this, "Fecha inicio no valida", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
            fecha.add(Calendar.MONTH, 1);
        }
        TableRow rowFinal = new TableRow(this);
        rowFinal.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        rowFinal.addView(createTextView2("Total"));
        rowFinal.addView(createTextView2("    "));
        rowFinal.addView(createTextView2(String.valueOf(valorNetoFinAnio)));
        tabla.addView(rowFinal);
    }

    private void mostrarDatosDeReporteMesGasto(ArrayList<Gasto> lista, TableLayout tabla, TextView mesActual) {
        Date fechaActual = new Date();

        int numMesActual = fechaActual.getMonth() + 1;
        String nombreMesActual = obtenerNombreMes(numMesActual);
        int numAnioActual = fechaActual.getYear() + 1900;
        mesActual.setText(nombreMesActual);

        double valorNetoFin = 0;

        for(IngresoGasto gasto : lista){
            String[] listaFecha = gasto.getFechaIn().split("/");
            int numMes = Integer.parseInt(listaFecha[1]);
            if(numMes == numMesActual && Integer.parseInt(listaFecha[2]) == numAnioActual){
                TableRow row = new TableRow(this);
                row.addView(createTextView(gasto.getCategoria()));
                row.addView(createTextView( String.valueOf(gasto.getValor())));
                tabla.addView(row);
                valorNetoFin += gasto.getValor();
            }

        }
        TableRow row2 = new TableRow(this);
        row2.addView(createTextView( "Total"));
        row2.addView(createTextView(String.valueOf(valorNetoFin)));
        tabla.addView(row2);
    }

    private void mostrarDatosDeReporteMesIn(ArrayList<Ingreso> lista, TableLayout tabla, TextView mesActual){
        Date fechaActual = new Date();

        int numMesActual = fechaActual.getMonth() + 1;
        String nombreMesActual = obtenerNombreMes(numMesActual);
        int numAnioActual = fechaActual.getYear() + 1900;
        mesActual.setText(nombreMesActual);

        double valorNetoFin = 0;

        for(IngresoGasto ingreso : lista){
            String[] listaFecha = ingreso.getFechaIn().split("/");
            int numMes = Integer.parseInt(listaFecha[1]);
            if(numMes == numMesActual && Integer.parseInt(listaFecha[2]) == numAnioActual){
                TableRow row = new TableRow(this);
                row.addView(createTextView(ingreso.getCategoria()));
                row.addView(createTextView( String.valueOf(ingreso.getValor())));
                tabla.addView(row);
                valorNetoFin += ingreso.getValor();
            }

        }
        TableRow row2 = new TableRow(this);
        row2.addView(createTextView( "Total"));
        row2.addView(createTextView(String.valueOf(valorNetoFin)));
        tabla.addView(row2);
    }

    private String obtenerNombreMes(int mes){
        String[] nombresMeses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return nombresMeses[mes - 1];
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(11, 11, 11, 11);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
    private TextView createTextView2(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(11, 11, 11, 11);
        textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textView.setGravity(Gravity.CENTER);
        return textView;
    }


    private void cargarGastos(String fileName) {
        try {
            File file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lstGasto = (ArrayList<Gasto>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                lstGasto =null;
                // Archivo no encontrado, mostrar un Toast
                Toast.makeText(this, "No se han añadido gastos.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Mostrar un Toast en caso de error y inicializar lista con datos predeterminados
            Toast.makeText(this, "Error al cargar archivo.", Toast.LENGTH_LONG).show();
        }
    }

    private void cargarIngresos(String fileName) {
        try {
            File file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lstIngreso = (ArrayList<Ingreso>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                // Archivo no encontrado, mostrar un Toast
                lstIngreso = null;
                Toast.makeText(this, "No se han añadido ingresos.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Mostrar un Toast en caso de error y inicializar lista con datos predeterminados
            Toast.makeText(this, "Error al cargar archivo.", Toast.LENGTH_LONG).show();
        }
    }


}