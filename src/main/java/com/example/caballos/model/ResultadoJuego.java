package com.example.caballos.model;

import java.util.List;

public class ResultadoJuego {
    private final int meta;
    private final List<Caballo> caballos;
    private final List<TurnoJuego> turnos;
    private final Caballo ganador;

    public ResultadoJuego(int meta, List<Caballo> caballos, List<TurnoJuego> turnos, Caballo ganador) {
        this.meta = meta;
        this.caballos = caballos;
        this.turnos = turnos;
        this.ganador = ganador;
    }

    public int getMeta() {
        return meta;
    }

    public List<Caballo> getCaballos() {
        return caballos;
    }

    public List<TurnoJuego> getTurnos() {
        return turnos;
    }

    public Caballo getGanador() {
        return ganador;
    }
}
