package com.example.caballos.model;

public class Caballo {
    private final String nombre;
    private final Palo palo;
    private int posicion;

    public Caballo(String nombre, Palo palo) {
        this.nombre = nombre;
        this.palo = palo;
        this.posicion = 0;
    }

    public void avanzarHastaMeta(int meta) {
        if (posicion < meta) {
            posicion++;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public Palo getPalo() {
        return palo;
    }

    public int getPosicion() {
        return posicion;
    }
}
