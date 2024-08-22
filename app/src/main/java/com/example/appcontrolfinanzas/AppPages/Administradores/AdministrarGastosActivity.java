package com.example.appcontrolfinanzas.AppPages.Administradores;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
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
        editTextFechaIn = findViewById(R.id.editTextFechaIn);
        editTextcategoria = findViewById(R.id.editTextcategoria);
        editTextvalorneto = findViewById(R.id.editTextvalorneto);
        editTextDescrip = findViewById(R.id.editTextDescrip);
        editTextFechaFin = findViewById(R.id.editTextFechaFin);
        editTextRepeticion = findViewById(R.id.editTextRepeticion);
        buttonRegisGas = findViewById(R.id.buttonRegisGas);
        tableLayoutGastos = findViewById(R.id.tableLayoutGastos);

        lstGasto = (ArrayList<Gasto>) getIntent().getSerializableExtra("ListaGastos");

        if (lstGasto == null) {
            lstGasto = new ArrayList<>();
            lstGasto.add(new Gasto("01/01/2024", "Alquiler", 350, "Alquiler casa", "No definido", Repeticion.por_mes));
            lstGasto.add(new Gasto("01/04/2024", "Pagos", 1000, "pago a banco", "30/01/2025", Repeticion.por_mes));
        }

        mostrarDatosEnTabla(lstGasto);

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

}