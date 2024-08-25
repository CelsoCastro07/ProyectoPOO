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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AdministrarGastosActivity extends AppCompatActivity {
    private TableLayout tableLayoutGastos;
    private EditText editTextFechaInDialog, editTextCategoriaDialog, editTextValorNetoDialog, editTextDescripDialog, editTextFechaFinDialog;
    private Spinner spinnerRepeticionDialog;
    private Button buttonRegisGas, buttonEliminarGas, buttonFinalizarGas;
    private CategoriaControl catControl;
    private ArrayList<Gasto> lstGasto;

    private ArrayList<String> categorias;
    public String fileNameCat = "categoriasGasto.txt";
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
        buttonEliminarGas = findViewById(R.id.buttonEliminarGas);
        buttonFinalizarGas =findViewById(R.id.buttonFinalizarGas);

        cargarGastosDesdeArchivo();
        cargarCategoriasDesdeArchivo(fileNameCat);

        mostrarDatosEnTabla(lstGasto);

        buttonRegisGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarGastosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_register_income, null);

                Spinner spinnerCategoriaDialog = dialogView.findViewById(R.id.spinner_categorias);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdministrarGastosActivity.this, android.R.layout.simple_spinner_item, categorias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinnerCategoriaDialog.setAdapter(adapter);

                // Crear el AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarGastosActivity.this);
                builder.setView(dialogView)
                        .setTitle("Registrar Gasto")
                        .setPositiveButton("Registrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Obtener los datos ingresados por el usuario
                                editTextFechaInDialog = dialogView.findViewById(R.id.editTextFechaInDialog);
                                editTextValorNetoDialog = dialogView.findViewById(R.id.editTextValorNetoDialog);
                                editTextDescripDialog = dialogView.findViewById(R.id.editTextDescripcionDialog);
                                editTextFechaFinDialog = dialogView.findViewById(R.id.editTextFechaFinDialog);
                                spinnerRepeticionDialog = dialogView.findViewById(R.id.spinnerRepeticionDialog);

                                String fechaInicio = editTextFechaInDialog.getText().toString();
                                String categoria = spinnerCategoriaDialog.getSelectedItem().toString();
                                String valorNeto = editTextValorNetoDialog.getText().toString();
                                String descripcion = editTextDescripDialog.getText().toString();
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
                                    Gasto nuevoGasto = new Gasto(fechaInicio, categoria, Double.parseDouble(valorNeto), descripcion, fechaFin, repeticion);

                                    // Agregar el nuevo ingreso a la lista
                                    boolean Agg = false;
                                    for (Gasto ing : lstGasto) {
                                        if (ing.equals(nuevoGasto)) {
                                            Agg = true;
                                            break;
                                        }
                                    }
                                    if (!Agg) {
                                        lstGasto.add(nuevoGasto);
                                    } else {
                                        Toast.makeText(AdministrarGastosActivity.this, "El gasto ya existe", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    Toast.makeText(AdministrarGastosActivity.this, "Error 😣", Toast.LENGTH_SHORT).show();
                                    return;
                                }
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

        buttonEliminarGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarGastosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_delete_income, null);

                // Crear el AlertDialog para eliminar ingreso
                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarGastosActivity.this);
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
                                    guardarGastosEnArchivo();
                                    mostrarDatosEnTabla(lstGasto);
                                } catch (NumberFormatException e) {
                                    Toast.makeText(AdministrarGastosActivity.this, "Código inválido.", Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });

        buttonFinalizarGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(AdministrarGastosActivity.this);
                View dialogView = inflater.inflate(R.layout.dialog_finalizar, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(AdministrarGastosActivity.this);
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
                                Toast.makeText(AdministrarGastosActivity.this, "Código inválido.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(AdministrarGastosActivity.this, "Por favor \nagregar categorias", Toast.LENGTH_LONG).show();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            categorias = new ArrayList<>();
            Toast.makeText(AdministrarGastosActivity.this, "Por favor \nagregar categorias", Toast.LENGTH_LONG).show();
        }
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
            File file = new File(getFilesDir(), "Gastos.txt");
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

    private void eliminarIngresoPorCodigo(int codigo) {
        boolean encontrado = false;
        for (int i = 0; i < lstGasto.size(); i++) {
            for (Gasto ing: lstGasto) {
                if (ing.getCodigo() == codigo){
                    lstGasto.remove(i);
                    encontrado = true;
                    break;
                }
            }
        }

        if (encontrado) {
            guardarGastosEnArchivo();  // Guardar los cambios
            mostrarDatosEnTabla(lstGasto);  // Actualizar la tabla
            Toast.makeText(this, "Gasto eliminado correctamente.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se encontró un Gasto con ese código.", Toast.LENGTH_SHORT).show();
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
        for (Gasto gasto : lstGasto) {
            if (gasto.getCodigo() == codigo) {
                try {
                    Date fechaInicioDate = sdf.parse(gasto.getFechaIn());
                    if (fechaInicioDate == null) {
                        throw new ParseException("Fecha inválida", 0);
                    }

                    if (nuevaFechaFinDate.before(fechaInicioDate)) {
                        Toast.makeText(this, "La nueva fecha de fin debe ser después de la fecha de inicio.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Actualizar fecha de fin
                    gasto.setFechaFin(nuevaFechaFin);
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
            mostrarDatosEnTabla(lstGasto);
            guardarGastosEnArchivo();
        } else {
            Toast.makeText(this, "Ingreso no encontrado.", Toast.LENGTH_SHORT).show();
        }

    }



}