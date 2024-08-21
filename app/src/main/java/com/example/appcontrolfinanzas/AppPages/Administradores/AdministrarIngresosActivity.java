package com.example.appcontrolfinanzas.AppPages.Administradores;

import static com.example.appcontrolfinanzas.AppPages.Administradores.AdministrarCategoriasActivity.catControl;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.appcontrolfinanzas.AppPages.Transacciones.*;
import com.example.appcontrolfinanzas.AppPages.Controladores.*;
import com.example.appcontrolfinanzas.AppPages.Vista.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.R;

import java.util.ArrayList;

public class AdministrarIngresosActivity extends AppCompatActivity {
    private TableLayout tableLayoutIngresos;
    private EditText editTextFechaIn, editTextcategoria, editTextvalorneto, editTextDescrip, editTextFechaFin, editTextRepeticion;
    private Button buttonRegisIng;
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
        // Referenciando los elementos de la interfaz
        tableLayoutIngresos = findViewById(R.id.tableLayoutIngresos);
        editTextFechaIn = findViewById(R.id.editTextFechaIn);
        editTextcategoria = findViewById(R.id.editTextcategoria);
        editTextvalorneto = findViewById(R.id.editTextvalorneto);
        editTextDescrip = findViewById(R.id.editTextDescrip);
        editTextFechaFin = findViewById(R.id.editTextFechaFin);
        editTextRepeticion = findViewById(R.id.editTextRepeticion);
        buttonRegisIng = findViewById(R.id.buttonRegisIng);
        tableLayoutIngresos = findViewById(R.id.tableLayoutIngresos);

        TransaccionControl inGascontrol = new TransaccionControl(catControl);
        ArrayList<Ingresos> lstIngresos = inGascontrol.getListaIngresos();

        lstIngresos.add(new Ingresos("01/01/2024", "Salario", 450, "sueldo", "No definido", Repeticion.por_mes));
        lstIngresos.add(new Ingresos("01/07/2024", "Deudas a cobrar", 1000, "prestamo a familiar", "30/06/2025", Repeticion.sin_repeticion));

        mostrarDatosEnTabla(inGascontrol);

        // Acción del botón Registrar
        buttonRegisIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarIngreso();
            }
        });
    }

    private void mostrarDatosEnTabla(TransaccionControl inGascontrol) {
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
        for (Ingresos ingreso : inGascontrol.getListaIngresos()) {
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

    // Método para agregar un nuevo ingreso a la tabla
    private void agregarIngreso() {
        String fechaInicio = editTextFechaIn.getText().toString();
        String categoria = editTextcategoria.getText().toString();
        String valorNeto = editTextvalorneto.getText().toString();
        String descripcion = editTextDescrip.getText().toString();
        String fechaFin = editTextFechaFin.getText().toString();
        String repeticion = editTextRepeticion.getText().toString();

        // Crear una nueva fila en la tabla
        TableRow nuevaFila = new TableRow(this);
        nuevaFila.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        // Crear y agregar TextViews para cada columna
        TextView textViewCodigo = new TextView(this);
        textViewCodigo.setText(String.valueOf(tableLayoutIngresos.getChildCount())); // Código generado como el número de filas

        TextView textViewFechaInicio = new TextView(this);
        textViewFechaInicio.setText(fechaInicio);

        TextView textViewCategoria = new TextView(this);
        textViewCategoria.setText(categoria);

        TextView textViewValorNeto = new TextView(this);
        textViewValorNeto.setText(valorNeto);

        TextView textViewDescripcion = new TextView(this);
        textViewDescripcion.setText(descripcion);

        TextView textViewFechaFin = new TextView(this);
        textViewFechaFin.setText(fechaFin);

        TextView textViewRepeticion = new TextView(this);
        textViewRepeticion.setText(repeticion);

        // Agregar los TextViews a la fila
        nuevaFila.addView(textViewCodigo);
        nuevaFila.addView(textViewFechaInicio);
        nuevaFila.addView(textViewCategoria);
        nuevaFila.addView(textViewValorNeto);
        nuevaFila.addView(textViewDescripcion);
        nuevaFila.addView(textViewFechaFin);
        nuevaFila.addView(textViewRepeticion);

        // Agregar la fila a la tabla
        tableLayoutIngresos.addView(nuevaFila);
    }
}