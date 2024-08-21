package com.example.appcontrolfinanzas.AppPages.logIn;

import java.io.Serializable;

public class Credenciales implements Serializable {
    private final String usuario;
    private final String contrasena;

    public Credenciales(String n, String c) {
        usuario = n;
        contrasena = c;
    }

    public String getContrasena (){
        return contrasena;
    }
    public  String getUsuario(){
        return usuario;
    }
}
