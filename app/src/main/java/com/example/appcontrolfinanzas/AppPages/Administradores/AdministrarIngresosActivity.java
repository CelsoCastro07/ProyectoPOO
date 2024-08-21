package com.example.appcontrolfinanzas.AppPages.Administradores;

import static com.example.appcontrolfinanzas.AppPages.Administradores.AdministrarCategoriasActivity.catControl;

import android.os.Bundle;
import android.view.Gravity;
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

public class AdministrarIngresosActivity extends AppCompatActivity {
    private TableLayout tableLayoutIngresos;
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

        TransaccionControl inGascontrol = new TransaccionControl(catControl);
        TransaccionVista principalInGas = new TransaccionVista(inGascontrol);
        inGascontrol.getListaIngresos().add(new Ingresos("01/01/2024", "Salario", 450, "sueldo", "No definido", Repeticion.por_mes));
        inGascontrol.getListaIngresos().add(new Ingresos("01/07/2024", "Deudas a cobrar", 1000, "prestamo a familiar", "30/06/2025", Repeticion.sin_repeticion));


        tableLayoutIngresos = findViewById(R.id.tableLayoutIngresos);
        mostrarDatosEnTabla(inGascontrol);
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
}