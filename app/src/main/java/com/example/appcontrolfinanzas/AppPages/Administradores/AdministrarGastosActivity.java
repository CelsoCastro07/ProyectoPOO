package com.example.appcontrolfinanzas.AppPages.Administradores;

import static com.example.appcontrolfinanzas.AppPages.Administradores.AdministrarCategoriasActivity.catControl;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.AppPages.Controladores.TransaccionControl;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Gastos;
import com.example.appcontrolfinanzas.AppPages.Transacciones.Repeticion;
import com.example.appcontrolfinanzas.AppPages.Vista.TransaccionVista;
import com.example.appcontrolfinanzas.R;



public class AdministrarGastosActivity extends AppCompatActivity {
    private TableLayout tableLayoutGastos;
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

        TransaccionControl inGascontrol = new TransaccionControl(catControl);
        TransaccionVista principalInGas = new TransaccionVista(inGascontrol);
        inGascontrol.getListaGastos().add(new Gastos("01/01/2024", "Alquiler", 350, "Alquiler casa", "No definido", Repeticion.por_mes));
        inGascontrol.getListaGastos().add(new Gastos("01/04/2024", "Pagos", 1000, "pago a banco", "30/01/2025", Repeticion.por_mes));

        tableLayoutGastos = findViewById(R.id.tableLayoutGastos);
        mostrarDatosEnTabla(inGascontrol);
    }

    private void mostrarDatosEnTabla(TransaccionControl inGascontrol) {
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
        for (Gastos gasto : inGascontrol.getListaGastos()) {
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