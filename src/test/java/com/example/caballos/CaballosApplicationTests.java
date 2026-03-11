package com.example.caballos;

import com.example.caballos.dto.CompraPuntosResponse;
import com.example.caballos.dto.PlataformaResponse;
import com.example.caballos.dto.UsuarioResponse;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.exception.ReglaNegocioException;
import com.example.caballos.model.Caballo;
import com.example.caballos.model.Carta;
import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.service.JuegoService;
import com.example.caballos.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class CaballosApplicationTests {
    @Autowired
    private JuegoService juegoService;

    @Autowired
    private UsuarioService usuarioService;

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

    @Test
    void registrarDebeAsignarPuntosInicialesYGrupo() {
        UsuarioResponse usuario = usuarioService.registrar("jugador-demo", "segura123");

        assertEquals(UsuarioService.PUNTOS_INICIALES, usuario.puntos());
        assertEquals(1, usuario.grupo());
    }

    @Test
    void comprarPuntosDebeSumarSaldoCorrectamente() {
        UsuarioResponse usuario = usuarioService.registrar("comprador-demo", "segura123");
        UsuarioEntity entidad = usuarioService.obtenerEntidad(usuario.id());

        CompraPuntosResponse compra = usuarioService.comprarPuntos(entidad, 2);

        assertEquals(2000, compra.puntosRecibidos());
        assertEquals(20_000, compra.costoPesos());
        assertEquals(3000, compra.saldoActual());
    }

    @Test
    void plataformaDebeRespetarCapacidadMaximaDeDieciseisUsuarios() {
        for (int i = 1; i <= 16; i++) {
            usuarioService.registrar("usuario-" + i, "segura123");
        }

        PlataformaResponse plataforma = usuarioService.obtenerEstadoPlataforma();

        assertEquals(16, plataforma.usuariosRegistrados());
        assertEquals(0, plataforma.cuposDisponibles());
        assertThrows(ReglaNegocioException.class, () -> usuarioService.registrar("usuario-17", "segura123"));
    }

}
