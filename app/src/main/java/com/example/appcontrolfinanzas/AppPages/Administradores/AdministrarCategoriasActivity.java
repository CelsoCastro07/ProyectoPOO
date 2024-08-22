package com.example.appcontrolfinanzas.AppPages.Administradores;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.AppPages.Controladores.*;
import com.example.appcontrolfinanzas.AppPages.Vista.*;
import com.example.appcontrolfinanzas.R;

import com.example.appcontrolfinanzas.AppPages.Transacciones.*;
import com.google.android.material.chip.Chip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class AdministrarCategoriasActivity extends AppCompatActivity {
    public CategoriaVista principalCat;
    private CategoriaControl catControl;
    private EditText etNombreCategoria;
    private TextView listaIngresos, listaGastos;
    private String tipoCategoria;
    private Button buttonAgCat, buttonEliCat, buttonMenPrin;
    private Chip chipIngreso, chipGasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_administrar_categorias);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicializacion de las categorias
        catControl = (CategoriaControl) getIntent().getSerializableExtra("categoria");
        principalCat = new CategoriaVista(catControl);

        cargarCategoriasDesdeArchivo();

        //Datos por defecto
        if (catControl.getListaIngresos().isEmpty() && catControl.getListaGastos().isEmpty()) {
            catControl.getListaIngresos().add("Deudas a cobrar");
            catControl.getListaIngresos().add("Salario");
            catControl.getListaIngresos().add("Bono solidario");
            catControl.getListaGastos().add("Pagos");
            catControl.getListaGastos().add("Alquiler");
            guardarCategoriasEnArchivo();
        }

        //Inicializacion de los apartados de la interfaz
        etNombreCategoria = findViewById(R.id.etNombreCategoria);
        chipIngreso = findViewById(R.id.chipIngresos);
        chipGasto = findViewById(R.id.chipGastos);
        listaIngresos = findViewById(R.id.listaIngresos);
        listaGastos = findViewById(R.id.listagastos);
        buttonAgCat = findViewById(R.id.buttonAgCat);
        buttonEliCat = findViewById(R.id.buttonEliCat);

        // mostrar las listas de ingresos y gastos
        listaIngresos.setText(mostrList("Ingreso"));
        listaGastos.setText(mostrList("Gasto"));
        //selector de ingreso o gasto
        chipIngreso.setOnClickListener(v -> tipoCategoria = "Ingreso");
        chipGasto.setOnClickListener(v -> tipoCategoria = "Gasto");
        //extraccion de la categoria ingresada

        // Botón para agregar la categoría
        buttonAgCat.setOnClickListener(v ->{
            String cat = etNombreCategoria.getText().toString().trim();
            if (Objects.equals(tipoCategoria, "Ingreso")){
                if(etNombreCategoria != null){
                    agregarCategoriaIn(cat);
                    listaIngresos.setText(mostrList("Ingreso"));
                }
            }else if (Objects.equals(tipoCategoria, "Gasto")){
                if(etNombreCategoria != null){
                    agregarCategoriaGas(cat);
                    listaGastos.setText(mostrList("Gasto"));
                }
            }
        });
        // Botón para eliminar la categoría
        buttonEliCat.setOnClickListener(v ->{
            String cat = etNombreCategoria.getText().toString().trim();
            if (Objects.equals(tipoCategoria, "Ingreso")) {
                if (etNombreCategoria != null) {
                    eliminarCategoriaIn(cat);
                    listaIngresos.setText(mostrList("Ingreso"));
                }
            }else if (Objects.equals(tipoCategoria, "Gasto")){
                if(etNombreCategoria != null){
                    eliminarCategoriaGas(cat);
                    listaGastos.setText(mostrList("Gasto"));
                }
            }
        });
    }

    private String mostrList(String tipo) {
        ArrayList<String> listaCategorias;
        // Determinar la lista de categorías según el tipo utilizando switch
        switch (tipo) {
            case "Ingreso":
                listaCategorias = catControl.getListaIngresos();
                break;
            case "Gasto":
                listaCategorias = catControl.getListaGastos();
                break;
            default:
                return "Tipo de categoría no válido";
        }
        // Concatenar las categorías en forma de listado
        StringBuilder listado = new StringBuilder();
        int i = 0;
        for (String categoria: listaCategorias) {
            listado.append(i + 1).append(". ").append(categoria).append("\n");
            i += 1;
        }
        return listado.toString();
    }

    private boolean buscarCategoriaIn(String c){
        for(String ing : catControl.getListaIngresos()){
            if(ing.equalsIgnoreCase(c)){
                return true;
            }
        }
        return false;
    }

    private boolean buscarCategoriaGas(String c){
        for(String ing : catControl.getListaGastos()){
            if(ing.equalsIgnoreCase(c)){
                return true;
            }
        }
        return false;
    }

    public void agregarCategoriaIn(String cat) {
        if(!buscarCategoriaIn(cat)){
            catControl.getListaIngresos().add(cat);
            guardarCategoriasEnArchivo();
            Toast.makeText(this,cat + " ha sido agregada.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,cat + " ya existe en ingreso.", Toast.LENGTH_SHORT).show();
        }
    }

    public void agregarCategoriaGas(String cat){
        if(!buscarCategoriaGas(cat)){
            catControl.getListaGastos().add(cat);
            guardarCategoriasEnArchivo();
            Toast.makeText(this,cat + " ha sido agregada.",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,cat + " ya existe en gastos.", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarCategoriaIn(String cat){
        if(buscarCategoriaIn(cat)){
            catControl.getListaIngresos().remove(cat);
            guardarCategoriasEnArchivo();
            Toast.makeText(this,cat + " ha sido eliminada", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,cat + " no existe", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarCategoriaGas(String cat){
        if(buscarCategoriaGas(cat)){
            catControl.getListaGastos().remove(cat);
            guardarCategoriasEnArchivo();
            Toast.makeText(this,cat + " ha sido eliminada", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,cat + " no existe", Toast.LENGTH_SHORT).show();
        }
    }

    private void guardarCategoriasEnArchivo() {
        try {
            File file = new File(getFilesDir(), "categorias.txt");
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(catControl.getListaIngresos());
            oos.writeObject(catControl.getListaGastos());
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarCategoriasDesdeArchivo() {
        try {
            File file = new File(getFilesDir(), "categorias.txt");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ArrayList<String> ingresos = (ArrayList<String>) ois.readObject();
                ArrayList<String> gastos = (ArrayList<String>) ois.readObject();
                catControl.setListaIngresos(ingresos);
                catControl.setListaGastos(gastos);
                ois.close();
                fis.close();
            } else {
                // Archivo no encontrado, inicializar listas vacías
                catControl.setListaIngresos(new ArrayList<>());
                catControl.setListaGastos(new ArrayList<>());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Inicializar listas vacías en caso de error
            catControl.setListaIngresos(new ArrayList<>());
            catControl.setListaGastos(new ArrayList<>());
        }
    }

}