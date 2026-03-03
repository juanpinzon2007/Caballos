package com.example.caballos;

import com.example.caballos.model.Caballo;
import com.example.caballos.model.Carta;
import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.service.JuegoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CaballosApplicationTests {
    private final JuegoService juegoService = new JuegoService();

    @Test
    void contextLoads() {
    }

    @Test
    void crearMazoDebeTener52Cartas() {
        List<Carta> mazo = juegoService.crearMazo();

        assertEquals(52, mazo.size());
    }

    @Test
    void crearCaballosDebeIniciarCuatroCaballosEnCero() {
        List<Caballo> caballos = juegoService.crearCaballos();

        assertEquals(4, caballos.size());
        assertTrue(caballos.stream().allMatch(caballo -> caballo.getPosicion() == 0));
    }

    @Test
    void jugarDebeTerminarConGanadorEnLaMeta() {
        ResultadoJuego resultado = juegoService.jugar(new Random(7));

        assertNotNull(resultado.getGanador());
        assertEquals(JuegoService.META, resultado.getGanador().getPosicion());
        assertTrue(resultado.getTurnos().size() >= JuegoService.META);
    }

}
