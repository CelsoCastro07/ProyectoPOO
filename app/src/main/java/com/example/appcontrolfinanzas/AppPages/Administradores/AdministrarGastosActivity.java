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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.AppPages.Controladores.CategoriaControl;
import com.example.appcontrolfinanzas.AppPages.Controladores.TransaccionControl;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Gasto;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Ingreso;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Repeticion;
import com.example.appcontrolfinanzas.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class AdministrarGastosActivity extends AppCompatActivity {
    private TableLayout tableLayoutGastos;
    private EditText editTextFechaIn, editTextcategoria, editTextvalorneto, editTextDescrip, editTextFechaFin, editTextRepeticion;
    private Button buttonRegisGas;
    private CategoriaControl catControl;
    private ArrayList<Gasto> lstGasto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_gastos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tableLayoutGastos = findViewById(R.id.tableLayoutGastos);
        buttonRegisGas = findViewById(R.id.buttonRegisGas);

        cargarGastosDesdeArchivo();

        mostrarDatosEnTabla(lstGasto);

        buttonRegisGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarGastosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_register_income, null);

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarGastosActivity.this);
                builder.setView(dialogView)
                        .setTitle("Registrar Gasto")
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
                                Gasto nuevoGasto = new Gasto(fechaInicio, categoria, Double.parseDouble(valorNeto), descripcion, fechaFin, repeticion);
                                // Agregar el nuevo ingreso a la lista
                                lstGasto.add(nuevoGasto);
                                // Mostrar el nuevo ingreso en la tabla
                                mostrarDatosEnTabla(lstGasto);
                                // Guardar los ingresos en el archivo
                                guardarGastosEnArchivo();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });
    }
    private void mostrarDatosEnTabla(ArrayList<Gasto> lstagas) {
        // Limpiar la tabla antes de agregar nuevos datos
        tableLayoutGastos.removeAllViews();
        // Agregar fila de encabezado si no está ya incluida
        TableRow headerRow = new TableRow(this);
        headerRow.addView(createTextView("Código"));
        headerRow.addView(createTextView("Fecha Inicio"));
        headerRow.addView(createTextView("Categoría"));
        headerRow.addView(createTextView("Valor Neto"));
        headerRow.addView(createTextView("Descripción"));
        headerRow.addView(createTextView("Fecha Fin"));
        headerRow.addView(createTextView("Repetición"));
        tableLayoutGastos.addView(headerRow);
        // Agregar datos de gastos
        for (Gasto gasto : lstagas){
            TableRow row = new TableRow(this);
            row.addView(createTextView(String.valueOf(gasto.getCodigo())));
            row.addView(createTextView(gasto.getFechaIn()));
            row.addView(createTextView(gasto.getCategoria()));
            row.addView(createTextView(String.valueOf(gasto.getValor())));
            row.addView(createTextView(gasto.getDescripcion()));
            row.addView(createTextView(gasto.getFechaFin()));
            row.addView(createTextView(gasto.getRepeticion().toString()));
            tableLayoutGastos.addView(row);
        }
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(11, 11, 11, 11);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void guardarGastosEnArchivo() {
        try {
            File file = new File(getFilesDir(), "Gastos.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(lstGasto);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarGastosDesdeArchivo() {
        try {
            File file = new File(getFilesDir(), "ingresos.txt");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                lstGasto = (ArrayList<Gasto>) ois.readObject();
                ois.close();
                fis.close();
            } else {
                lstGasto = new ArrayList<>();
                lstGasto.add(new Gasto("01/01/2024", "Alquiler", 350, "Alquiler casa", "No definido", Repeticion.por_mes));
                lstGasto.add(new Gasto("01/04/2024", "Pagos", 1000, "pago a banco", "30/01/2025", Repeticion.por_mes));
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
        cargarGastosDesdeArchivo();
        mostrarDatosEnTabla(lstGasto);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardar ingresos al pausar la actividad
        guardarGastosEnArchivo();
    }

}