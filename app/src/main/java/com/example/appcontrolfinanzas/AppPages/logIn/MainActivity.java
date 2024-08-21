package com.example.appcontrolfinanzas.AppPages.logIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appcontrolfinanzas.AppPages.PaginaPrincipalActivity2;
import com.example.appcontrolfinanzas.R;

public class MainActivity extends AppCompatActivity {
    ArrayList<Credenciales> lstcredenciales;
    Button btningresar;
    EditText etusuario;
    EditText etcontrasena;
    String fileName = "listaCredenciales.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lstcredenciales = cargarCredenciales(fileName);
        if (lstcredenciales == null) {
            lstcredenciales = new ArrayList<>();
            lstcredenciales.add(new Credenciales("admin", "password"));
            guardarCredenciales(lstcredenciales, fileName);
        }
        btningresar = findViewById(R.id.btningresar);
        etusuario = findViewById(R.id.editusuario);
        etcontrasena = findViewById(R.id.editcontrasena);
        btningresar.setOnClickListener(v -> registrarUsuario());

    }
    // se considera que el usuario siempre ingresa con al menos el usuario correcto
    // si no es asi se creara otro usuario distinto
    public void registrarUsuario() {
        String user = etusuario.getText().toString();
        String password = etcontrasena.getText().toString();
        boolean userencontrado = false;
        boolean claveValida = false;
        for (Credenciales c : lstcredenciales) {
            if (c.getUsuario().equals(user)) {
                userencontrado = true;
                if (c.getContrasena().equals(password)) {
                    claveValida = true;
                    break;
                }
            }
        }
        if (userencontrado && claveValida) {
            // Inicia la nueva actividad
            Log.d("Archivo", "Bienvenido de vuelta");
            Intent pg_principal = new Intent(MainActivity.this, PaginaPrincipalActivity2.class);
            Toast.makeText(MainActivity.this, "Bienvenido de vuelta", Toast.LENGTH_SHORT).show();
            startActivity(pg_principal);
        } else if (userencontrado) {
            // El usuario existe pero la clave es incorrecta
            Log.d("Archivo", "contraseña invalida");
            Toast.makeText(MainActivity.this, "Contraseña inválida", Toast.LENGTH_SHORT).show();
        } else {
            // El usuario no existe, agrega las credenciales y navega a la nueva actividad
            lstcredenciales.add(new Credenciales(user, password));
            guardarCredenciales(lstcredenciales, fileName);
            Toast.makeText(MainActivity.this, "Credenciales guardadas", Toast.LENGTH_SHORT).show();
            Intent pg_principal = new Intent(MainActivity.this, PaginaPrincipalActivity2.class);
            startActivity(pg_principal);
        }
    }
    // escribe en el documento
    private void guardarCredenciales(ArrayList<Credenciales> credenciales, String fileName) {
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(credenciales);
            oos.close();
            fos.close();
            Log.d("Archivo", "credenciales escritas");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // lectrua del archivos
    private ArrayList<Credenciales> cargarCredenciales(String fileName) {
        ArrayList<Credenciales> credencialesIn = null;
        File file = new File(getFilesDir(), fileName);
        if (file.exists()) {
            try {
                FileInputStream datos = openFileInput(fileName);
                ObjectInputStream ois = new ObjectInputStream(datos);
                credencialesIn = (ArrayList<Credenciales>) ois.readObject();
                ois.close();
                datos.close();
                Log.d("Archivo", "Credenciales cargadas.");
                // Mostrar las credenciales en la consola
                for (Credenciales credencial : credencialesIn) {
                    Log.d("Credencialesguardadas", "Usuario: " + credencial.getUsuario() + ", Contraseña: " + credencial.getContrasena());
                }
            } catch (ClassNotFoundException | IOException cl) {
                cl.printStackTrace();
            }
        } else {
            Log.d("Archivo", "El archivo 'listaCredenciales.txt' ya existe.");
        }
        return credencialesIn;
    }
}

