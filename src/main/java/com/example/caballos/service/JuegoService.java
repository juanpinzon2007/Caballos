package com.example.caballos.service;

import com.example.caballos.model.Caballo;
import com.example.caballos.model.Carta;
import com.example.caballos.model.Palo;
import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.model.TurnoJuego;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class JuegoService {
    public static final int META = 7;

    public ResultadoJuego jugar() {
        return jugar(new Random());
    }

    public ResultadoJuego jugar(Random random) {
        List<Carta> mazo = crearMazo();
        Collections.shuffle(mazo, random);

        List<Caballo> caballos = crearCaballos();
        List<TurnoJuego> turnos = new ArrayList<>();
        Caballo ganador = null;
        int numeroTurno = 1;

        for (Carta carta : mazo) {
            Caballo caballo = buscarCaballoPorPalo(caballos, carta.getPalo());
            caballo.avanzarHastaMeta(META);

            turnos.add(new TurnoJuego(
                    numeroTurno,
                    carta,
                    caballo.getNombre(),
                    caballo.getPosicion(),
                    capturarPosiciones(caballos)
            ));

            if (caballo.getPosicion() >= META) {
                ganador = caballo;
                break;
            }

            numeroTurno++;
        }

        return new ResultadoJuego(META, caballos, turnos, ganador);
    }

    public List<Carta> crearMazo() {
        String[] valores = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        List<Carta> mazo = new ArrayList<>();

        for (Palo palo : Palo.values()) {
            for (String valor : valores) {
                mazo.add(new Carta(valor, palo));
            }
        }

        return mazo;
    }

    public List<Caballo> crearCaballos() {
        List<Caballo> caballos = new ArrayList<>();
        caballos.add(new Caballo("As de Corazones", Palo.CORAZONES));
        caballos.add(new Caballo("As de Diamantes", Palo.DIAMANTES));
        caballos.add(new Caballo("As de Treboles", Palo.TREBOLES));
        caballos.add(new Caballo("As de Picas", Palo.PICAS));
        return caballos;
    }

    private Caballo buscarCaballoPorPalo(List<Caballo> caballos, Palo palo) {
        for (Caballo caballo : caballos) {
            if (caballo.getPalo() == palo) {
                return caballo;
            }
        }

        throw new IllegalStateException("No existe un caballo para el palo " + palo);
    }

    private Map<String, Integer> capturarPosiciones(List<Caballo> caballos) {
        Map<String, Integer> posiciones = new LinkedHashMap<>();

        for (Caballo caballo : caballos) {
            posiciones.put(caballo.getNombre(), caballo.getPosicion());
        }

        return posiciones;
    }
}
