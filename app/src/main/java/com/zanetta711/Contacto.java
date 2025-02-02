package com.zanetta711;

import java.io.Serializable;

public class Contacto implements Serializable {
    private String nombre;
    private String apellido;
    private String telefono;
    private String domicilio;
    private String genero;

    // Constructor
    public Contacto(String nombre, String apellido, String telefono, String domicilio, String genero) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.domicilio = domicilio;
        this.genero = genero;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getGenero() {
        return genero;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}