package com.example.appcontrolfinanzas.AppPages.Administradores;

import android.content.DialogInterface;
import java.text.SimpleDateFormat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdministrarIngresosActivity extends AppCompatActivity {
    private TableLayout tableLayoutIngresos;
    private Button buttonRegisIng,buttonEliminarIng,buttonFinalizarIng;
    private CategoriaControl catControl;
    private ArrayList<Ingreso> lstIngresos;
    private ArrayList<String> categorias;
    public String fileNameCat = "categoriasIngreso.txt";

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
        buttonRegisIng = findViewById(R.id.buttonRegistrar);
        buttonEliminarIng = findViewById(R.id.buttonEliminar);
        buttonFinalizarIng = findViewById(R.id.buttonFinalizarIng);

        cargarIngresosDesdeArchivo();
        cargarCategoriasDesdeArchivo(fileNameCat);

        mostrarDatosEnTabla(lstIngresos);


        buttonRegisIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarIngresosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_register_income, null);

                Spinner spinnerCategoriaDialog = dialogView.findViewById(R.id.spinner_categorias);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdministrarIngresosActivity.this, android.R.layout.simple_spinner_item, categorias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerCategoriaDialog.setAdapter(adapter);

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarIngresosActivity.this);
                builder.setView(dialogView)
                        .setTitle("Registrar Ingreso")
                        .setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                catControl = (CategoriaControl) getIntent().getSerializableExtra("categoria");

                                // Obtener los datos ingresados por el usuario
                                EditText editTextFechaInDialog = dialogView.findViewById(R.id.editTextFechaInDialog);
                                EditText editTextValorNetoDialog = dialogView.findViewById(R.id.editTextValorNetoDialog);
                                EditText editTextDescripcionDialog = dialogView.findViewById(R.id.editTextDescripcionDialog);
                                EditText editTextFechaFinDialog = dialogView.findViewById(R.id.editTextFechaFinDialog);
                                Spinner spinnerRepeticionDialog = dialogView.findViewById(R.id.spinnerRepeticionDialog);

                                String fechaInicio = editTextFechaInDialog.getText().toString();
                                String categoria = spinnerCategoriaDialog.getSelectedItem().toString();
                                String valorNeto = editTextValorNetoDialog.getText().toString();
                                String descripcion = editTextDescripcionDialog.getText().toString();
                                String fechaFin = editTextFechaFinDialog.getText().toString();
                                String repeticionSrt = spinnerRepeticionDialog.getSelectedItem().toString();

                                Repeticion repeticion;
                                switch (repeticionSrt) {
                                    case "por mes":
                                        repeticion = Repeticion.por_mes;
                                        break;
                                    // Agregar otros casos si es necesario
                                    default:
                                        repeticion = Repeticion.sin_repeticion;
                                        break;
                                }
                                // Crear un nuevo objeto Ingreso
                                try {
                                    Ingreso nuevoIngreso = new Ingreso(fechaInicio, categoria, Double.parseDouble(valorNeto), descripcion, fechaFin, repeticion);
                                    boolean Agg = false;
                                    for (Ingreso ing:lstIngresos) {
                                        if (ing.equals(nuevoIngreso)) {
                                            Agg = true;
                                            break;
                                        }
                                    }
                                    if (!Agg){
                                        lstIngresos.add(nuevoIngreso);
                                    }else{
                                        Toast.makeText(AdministrarIngresosActivity.this, "El ingreso ya existe", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(AdministrarIngresosActivity.this, "Error 😣", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // Agregar el nuevo ingreso a la lista

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

        buttonEliminarIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarIngresosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_delete_income, null);

                // Crear el AlertDialog para eliminar ingreso
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarIngresosActivity.this);
                builder.setView(dialogView)
                        .setTitle("Eliminar Ingreso")
                        .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Obtener el código ingresado por el usuario
                                EditText editTextCodigo = dialogView.findViewById(R.id.editTextCodigoDialog);
                                String codigoStr = editTextCodigo.getText().toString();
                                try {
                                    int codigo = Integer.parseInt(codigoStr);
                                    eliminarIngresoPorCodigo(codigo);
                                    guardarIngresosEnArchivo();
                                    mostrarDatosEnTabla(lstIngresos);
                                } catch (NumberFormatException e) {
                                    Toast.makeText(AdministrarIngresosActivity.this, "Código inválido.", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        buttonFinalizarIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarIngresosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_finalizar, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarIngresosActivity.this);
                builder.setView(dialogView)
                        .setTitle("Finalizar Ingreso")
                        .setPositiveButton("Finalizar", (dialog, which) -> {
                            EditText editTextCodigo = dialogView.findViewById(R.id.editTextCodigoFinalizar);
                            EditText editTextNuevaFechaFin = dialogView.findViewById(R.id.editTextNuevaFechaFin);

                            String codigoStr = editTextCodigo.getText().toString();
                            String nuevaFechaFin = editTextNuevaFechaFin.getText().toString();

                            try {
                                int codigo = Integer.parseInt(codigoStr);
                                actualizarFechaFin(codigo, nuevaFechaFin);
                            } catch (NumberFormatException e) {
                                Toast.makeText(AdministrarIngresosActivity.this, "Código inválido.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

    }

    private void cargarCategoriasDesdeArchivo(String fileNameCat) {
        try {
            File fileIngreso = new File(getFilesDir(), fileNameCat);
            if (fileIngreso.exists()) {
                FileInputStream fis = new FileInputStream(fileIngreso);
                ObjectInputStream oisIng = new ObjectInputStream(fis);

                categorias = (ArrayList<String>) oisIng.readObject();
                oisIng.close();
                fis.close();

            } else {
                // Archivo no encontrado, inicializar lista vacía
                categorias = new ArrayList<>();
                    Toast.makeText(AdministrarIngresosActivity.this, "Por favor \nagregar categorias", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            categorias = new ArrayList<>();
            Toast.makeText(AdministrarIngresosActivity.this, "Por favor \nagregar categorias", Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarSpinnerCategorias(Spinner spinner) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        adapter.notifyDataSetChanged();
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

    private void eliminarIngresoPorCodigo(int codigo) {
        boolean encontrado = false;
        for (int i = 0; i < lstIngresos.size(); i++) {
            for (Ingreso ing: lstIngresos) {
                if (ing.getCodigo() == codigo){
                    lstIngresos.remove(i);
                    encontrado = true;
                    break;
                }
            }
        }

        if (encontrado) {
            guardarIngresosEnArchivo();  // Guardar los cambios
            mostrarDatosEnTabla(lstIngresos);  // Actualizar la tabla
            Toast.makeText(this, "Ingreso eliminado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró un ingreso con ese código.", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarFechaFin(int codigo, String nuevaFechaFin) {
        boolean actualizado = false;

        // Definir formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date nuevaFechaFinDate;
        try {
            nuevaFechaFinDate = sdf.parse(nuevaFechaFin);
            if (nuevaFechaFinDate == null) {
                throw new ParseException("Fecha inválida", 0);
            }
        } catch (ParseException e) {
            Toast.makeText(this, "Formato de fecha inválido. Debe ser dd/MM/yyyy.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar el ingreso y actualizar la fecha de fin
        for (Ingreso ingreso : lstIngresos) {
            if(ingreso.getCodigo()==codigo){
                try {
                    Date fechaInicioDate = sdf.parse(ingreso.getFechaIn());
                    if (fechaInicioDate == null) {
                        throw new ParseException("Fecha inválida", 0);
                    }
                if (nuevaFechaFinDate.before(fechaInicioDate)) {
                    Toast.makeText(this, "La nueva fecha de fin debe ser después de la fecha de inicio.", Toast.LENGTH_SHORT).show();
                    return;
                }

                    // Actualizar fecha de fin
                    ingreso.setFechaFin(nuevaFechaFin);
                    actualizado = true;
                    break;
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al procesar las fechas.", Toast.LENGTH_SHORT).show();
                }
            }
            }

        // Mostrar mensaje de éxito o error
        if (actualizado) {
            Toast.makeText(this, "Fecha de fin actualizada.", Toast.LENGTH_SHORT).show();
            mostrarDatosEnTabla(lstIngresos);
            guardarIngresosEnArchivo();
        } else {
            Toast.makeText(this, "Ingreso no encontrado.", Toast.LENGTH_SHORT).show();
        }

    }
}