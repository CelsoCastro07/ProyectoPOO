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
import com.example.appcontrolfinanzas.AppPages.Vista.*;
import com.example.appcontrolfinanzas.AppPages.Controladores.*;
import com.example.appcontrolfinanzas.AppPages.Finanzas.*;
import com.example.appcontrolfinanzas.AppPages.Transacciones.*;
import com.example.appcontrolfinanzas.R;
import java.util.ArrayList;


public class PaginaPrincipalActivity2 extends AppCompatActivity {
Spinner sItems;
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

        CategoriaControl catControl = new CategoriaControl();
        CategoriaVista principalCat = new CategoriaVista(catControl);
        catControl.getListaIngresos().add("Deudas a cobrar");
        catControl.getListaIngresos().add("Salario");
        catControl.getListaGastos().add("Pagos");
        catControl.getListaGastos().add("Alquiler");


        TransaccionControl inGascontrol = new TransaccionControl(catControl);
        TransaccionVista principalInGas = new TransaccionVista(inGascontrol);
        inGascontrol.getListaIngresos().add(new Ingresos("01/01/2024", "Salario", 450, "sueldo", "No definido", Repeticion.por_mes));
        inGascontrol.getListaIngresos().add(new Ingresos("01/07/2024", "Deudas a cobrar", 1000, "prestamo a familiar", "30/06/2025", Repeticion.sin_repeticion));
        inGascontrol.getListaGastos().add(new Gastos("01/01/2024", "Alquiler", 350, "Alquiler casa", "No definido", Repeticion.por_mes));
        inGascontrol.getListaGastos().add(new Gastos("01/04/2024", "Pagos", 1000, "pago a banco", "30/01/2025", Repeticion.por_mes));

        mostrarAct();
        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = (String) parent.getItemAtPosition(position);
                redirigirActividad(selectedOption);
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
        sItems = findViewById(R.id.spActividades);
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

    public void redirigirActividad(String option) {
        Intent intent;
        switch (option) {
            case "1.Administrar categorías":
                intent = new Intent(this, AdministrarCategoriasActivity.class);
                break;
            case "2.Administrar Ingresos":
                intent = new Intent(this,AdministrarIngresosActivity.class);
                break;
            case "3.Administrar Gastos":
                intent = new Intent(this, AdministrarGastosActivity.class);
                break;
             /*
            case "Cuentas por cobrar":
                intent = new Intent(this, CuentasPorCobrarActivity.class);
                break;
            case "Administrar Cuentas por pagar":
                intent = new Intent(this, AdministrarCuentasPorPagarActivity.class);
                break;
            case "Administrar cuentas bancarias":
                intent = new Intent(this, AdministrarCuentasBancariasActivity.class);
                break;
            case "Administrar Inversiones":
                intent = new Intent(this, AdministrarInversionesActivity.class);
                break;
            case "Administrar personas y bancos":
                intent = new Intent(this, AdministrarPersonasYBancosActivity.class);
                break;
            case "Reportes":
                intent = new Intent(this, ReportesActivity.class);
                break;
            case "Proyección de gastos":
                intent = new Intent(this, ProyeccionDeGastosActivity.class);
                break;
              */
            default:
                return; // No hacer nada si la opción no coincide
        }
        startActivity(intent);
    }
}
