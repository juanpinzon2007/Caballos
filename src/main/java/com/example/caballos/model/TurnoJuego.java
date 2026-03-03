package com.example.caballos.model;

import java.util.Map;

public class TurnoJuego {
    private final int numero;
    private final Carta carta;
    private final String caballoQueAvanza;
    private final int nuevaPosicion;
    private final Map<String, Integer> posiciones;

    public TurnoJuego(int numero, Carta carta, String caballoQueAvanza, int nuevaPosicion, Map<String, Integer> posiciones) {
        this.numero = numero;
        this.carta = carta;
        this.caballoQueAvanza = caballoQueAvanza;
        this.nuevaPosicion = nuevaPosicion;
        this.posiciones = posiciones;
    }

    public int getNumero() {
        return numero;
    }

    public Carta getCarta() {
        return carta;
    }

    public String getCaballoQueAvanza() {
        return caballoQueAvanza;
    }

    public int getNuevaPosicion() {
        return nuevaPosicion;
    }

    public Map<String, Integer> getPosiciones() {
        return posiciones;
    }
}
