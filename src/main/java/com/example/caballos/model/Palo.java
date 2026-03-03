package com.example.caballos.model;

public enum Palo {
    CORAZONES("Corazones", "\u2665"),
    DIAMANTES("Diamantes", "\u2666"),
    TREBOLES("Treboles", "\u2663"),
    PICAS("Picas", "\u2660");

    private final String nombre;
    private final String simbolo;

    Palo(String nombre, String simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
