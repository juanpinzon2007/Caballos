package com.example.caballos.model;

public class Carta {
    private final String valor;
    private final Palo palo;

    public Carta(String valor, Palo palo) {
        this.valor = valor;
        this.palo = palo;
    }

    public String getValor() {
        return valor;
    }

    public Palo getPalo() {
        return palo;
    }

    public String getEtiqueta() {
        return valor + " " + palo.getSimbolo();
    }
}
