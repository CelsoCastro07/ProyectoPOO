package com.example.appcontrolfinanzas.AppPages.Administradores;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcontrolfinanzas.AppPages.Transacciones.*;
import com.example.appcontrolfinanzas.AppPages.Controladores.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AdministrarIngresosActivity extends AppCompatActivity {
    private TableLayout tableLayoutIngresos;
    private EditText editTextFechaIn, editTextcategoria, editTextvalorneto, editTextDescrip, editTextFechaFin;
    private Button buttonRegisIng;
    private CategoriaControl catControl;
    private ArrayList<Ingreso> lstIngresos;
    private Spinner SpinnerRepeticion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_ingresos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tableLayoutIngresos = findViewById(R.id.tableLayoutIngresos);
        buttonRegisIng = findViewById(R.id.buttonRegisIng);

        cargarIngresosDesdeArchivo();
        
        mostrarDatosEnTabla(lstIngresos);

        buttonRegisIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarIngresosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_register_income, null);

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarIngresosActivity.this);
                builder.setView(dialogView)
                        .setTitle("Registrar Ingreso")
                        .setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Obtener los datos ingresados por el usuario
                                EditText editTextFechaInDialog = dialogView.findViewById(R.id.editTextFechaInDialog);
                                EditText editTextCategoriaDialog = dialogView.findViewById(R.id.editTextCategoriaDialog);
                                EditText editTextValorNetoDialog = dialogView.findViewById(R.id.editTextValorNetoDialog);
                                EditText editTextDescripcionDialog = dialogView.findViewById(R.id.editTextDescripcionDialog);
                                EditText editTextFechaFinDialog = dialogView.findViewById(R.id.editTextFechaFinDialog);
                                Spinner spinnerRepeticionDialog = dialogView.findViewById(R.id.spinnerRepeticionDialog);

                                String fechaInicio = editTextFechaInDialog.getText().toString();
                                String categoria = editTextCategoriaDialog.getText().toString();
                                String valorNeto = editTextValorNetoDialog.getText().toString();
                                String descripcion = editTextDescripcionDialog.getText().toString();
                                String fechaFin = editTextFechaFinDialog.getText().toString();
                                String repeticionSrt = spinnerRepeticionDialog.getSelectedItem().toString();

                                Repeticion repeticion;
                                switch (repeticionSrt) {
                                    case "por mes":
                                        repeticion = Repeticion.por_mes;
                                        break;
                                    case "por semana":
                                        repeticion = Repeticion.por_semana;
                                        break;
                                    case "por año":
                                        repeticion = Repeticion.por_anio;
                                        break;
                                    case "por dia":
                                        repeticion = Repeticion.por_dia;
                                        break;
                                    // Agregar otros casos si es necesario
                                    default:
                                        repeticion = Repeticion.no_definido;
                                        break;
                                }
                                // Crear un nuevo objeto Ingreso
                                Ingreso nuevoIngreso = new Ingreso(fechaInicio, categoria, Double.parseDouble(valorNeto), descripcion, fechaFin, repeticion);
                                // Agregar el nuevo ingreso a la lista
                                lstIngresos.add(nuevoIngreso);
                                // Mostrar el nuevo ingreso en la tabla
                                mostrarDatosEnTabla(lstIngresos);
                                // Guardar los ingresos en el archivo
                                guardarIngresosEnArchivo();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

    }

    private void mostrarDatosEnTabla(ArrayList<Ingreso> lstaing) {
        // Limpiar la tabla antes de agregar nuevos datos
        tableLayoutIngresos.removeAllViews();

        // Agregar fila de encabezado si no está ya incluida
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("Código"));
        headerRow.addView(createTextView("Fecha Inicio"));
        headerRow.addView(createTextView("Categoría"));
        headerRow.addView(createTextView("Valor Neto"));
        headerRow.addView(createTextView("Descripción"));
        headerRow.addView(createTextView("Fecha Fin"));
        headerRow.addView(createTextView("Repetición"));
        tableLayoutIngresos.addView(headerRow);

        // Agregar datos de ingresos
        for (Ingreso ingreso : lstaing) {
            TableRow row = new TableRow(this);
            row.addView(createTextView(String.valueOf(ingreso.getCodigo())));
            row.addView(createTextView(ingreso.getFechaIn()));
            row.addView(createTextView(ingreso.getCategoria()));
            row.addView(createTextView(String.valueOf(ingreso.getValor())));
            row.addView(createTextView(ingreso.getDescripcion()));
            row.addView(createTextView(ingreso.getFechaFin()));
            row.addView(createTextView(ingreso.getRepeticion().toString()));
            tableLayoutIngresos.addView(row);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(11, 11, 11, 11);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void guardarIngresosEnArchivo() {
        try {
            File file = new File(getFilesDir(), "ingresos.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lstIngresos);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarIngresosDesdeArchivo() {
        try {
            File file = new File(getFilesDir(), "ingresos.txt");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lstIngresos = (ArrayList<Ingreso>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                lstIngresos = new ArrayList<>();
                lstIngresos.add(new Ingreso("01/01/2024", "Salario", 450, "sueldo", "No definido", Repeticion.por_mes));
                lstIngresos.add(new Ingreso("01/07/2024", "Deudas a cobrar", 1000, "prestamo a familiar", "30/06/2025", Repeticion.sin_repeticion));
                // Archivo no encontrado, mostrar un Toast
                Toast.makeText(this, "Archivo no encontrado. Datos inicializados con valores predeterminados.", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Mostrar un Toast en caso de error y inicializar lista con datos predeterminados
            Toast.makeText(this, "Error al cargar archivo. Datos inicializados con valores predeterminados.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar ingresos al reanudar la actividad
        cargarIngresosDesdeArchivo();
        mostrarDatosEnTabla(lstIngresos);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardar ingresos al pausar la actividad
        guardarIngresosEnArchivo();
    }
}