package com.example.appcontrolfinanzas.AppPages;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.AppPages.Administradores.*;
import com.example.appcontrolfinanzas.AppPages.Controladores.*;
import com.example.appcontrolfinanzas.AppPages.Transacciones.*;
import com.example.appcontrolfinanzas.R;

import java.util.ArrayList;


public class PaginaPrincipalActivity2 extends AppCompatActivity {
Spinner sItems;
private CategoriaControl catControl;
private TransaccionControl inGascontrol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_principal2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sItems = findViewById(R.id.spActividades);

        catControl = new CategoriaControl();
        catControl.getListaIngresos().add("Deudas a cobrar");
        catControl.getListaIngresos().add("Salario");
        catControl.getListaGastos().add("Pagos");
        catControl.getListaGastos().add("Alquiler");

        mostrarAct();
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                Intent intent;
                switch (selectedOption) {
                    case "1.Administrar categorías":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarCategoriasActivity.class);
                        intent.putExtra("categoria", catControl);
                        break;
                    case "2.Administrar Ingresos":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarIngresosActivity.class);
                        intent.putExtra("categoria", catControl);
                        break;
                    case "3.Administrar Gastos":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarGastosActivity.class);
                        intent.putExtra("categoria", catControl);
                        break;
                    /*
                    case "Cuentas por cobrar":
                        intent = new Intent(PaginaPrincipalActivity2.this, CuentasPorCobrarActivity.class);
                        break;

                    case "Administrar Cuentas por pagar":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarCuentasPorPagarActivity.class);
                        break;
                    case "Administrar cuentas bancarias":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarCuentasBancariasActivity.class);
                        break;
                    case "Administrar Inversiones":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarInversionesActivity.class);
                        break;
                    case "Administrar personas y bancos":
                        intent = new Intent(PaginaPrincipalActivity2.this, AdministrarPersonasYBancosActivity.class);
                        break; */
                    case "9.Reportes":
                        intent = new Intent(PaginaPrincipalActivity2.this, ReporteActivity.class);
                        break;
                    case "10.Proyección de gastos":
                        intent = new Intent(PaginaPrincipalActivity2.this, ProyeccionDeGastosActivity.class);
                        break;
                    default:
                        return; // No hacer nada si la opción no coincide
                }
                startActivity(intent);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sItems.setSelection(0);
    }

    public void mostrarAct() {
        ArrayList<String> listaActividades = new ArrayList<>();
        listaActividades.add("--Seleccione alguna actividad--");
        listaActividades.add("1.Administrar categorías");
        listaActividades.add("2.Administrar Ingresos");
        listaActividades.add("3.Administrar Gastos");
        listaActividades.add("4.Cuentas por cobrar");
        listaActividades.add("5.Administrar Cuentas por pagar");
        listaActividades.add("6.Administrar cuentas bancarias");
        listaActividades.add("7.Administrar Inversiones");
        listaActividades.add("8.Administrar personas y bancos");
        listaActividades.add("9.Reportes");
        listaActividades.add("10.Proyección de gastos");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaActividades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);
    }

}
