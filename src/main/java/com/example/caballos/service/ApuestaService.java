package com.example.caballos.service;

import com.example.caballos.dto.ApuestaResponse;
import com.example.caballos.entity.ApuestaEntity;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.exception.ReglaNegocioException;
import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.repository.ApuestaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class ApuestaService {

    private static final Set<String> CABALLOS_VALIDOS = Set.of(
            "As de Corazones",
            "As de Diamantes",
            "As de Treboles",
            "As de Picas"
    );

    private final JuegoService juegoService;
    private final UsuarioService usuarioService;
    private final ApuestaRepository apuestaRepository;

    public ApuestaService(JuegoService juegoService,
                          UsuarioService usuarioService,
                          ApuestaRepository apuestaRepository) {
        this.juegoService = juegoService;
        this.usuarioService = usuarioService;
        this.apuestaRepository = apuestaRepository;
    }

    @Transactional
    public ApuestaResponse apostar(Long usuarioId, String caballoElegido, Integer puntosApostados) {
        validarApuesta(caballoElegido, puntosApostados);

        UsuarioEntity usuario = usuarioService.obtenerEntidad(usuarioId);

        if (usuario.getPuntos() < puntosApostados) {
            throw new ReglaNegocioException("No tienes puntos suficientes para apostar ese valor");
        }

        int saldoAntes = usuario.getPuntos();
        usuario.setPuntos(saldoAntes - puntosApostados);

        ResultadoJuego resultado = juegoService.jugar();
        String caballoGanador = resultado.getGanador().getNombre();
        boolean gano = caballoGanador.equals(caballoElegido);
        int premio = gano ? puntosApostados * 5 : 0;

        usuario.setPuntos(usuario.getPuntos() + premio);

        apuestaRepository.save(new ApuestaEntity(
                usuario,
                caballoElegido,
                puntosApostados,
                caballoGanador,
                gano,
                premio,
                saldoAntes,
                usuario.getPuntos(),
                LocalDateTime.now()
        ));

        String mensaje = gano
                ? "Ganaste. Se acreditaron " + premio + " puntos (x5)."
                : "No ganaste esta carrera. Intenta de nuevo.";

        return new ApuestaResponse(
                resultado,
                caballoElegido,
                puntosApostados,
                gano,
                premio,
                usuario.getPuntos(),
                mensaje
        );
    }

    private void validarApuesta(String caballoElegido, Integer puntosApostados) {
        if (caballoElegido == null || !CABALLOS_VALIDOS.contains(caballoElegido)) {
            throw new ReglaNegocioException("Debes elegir un caballo valido");
        }

        if (puntosApostados == null || puntosApostados <= 0) {
            throw new ReglaNegocioException("La apuesta debe ser mayor que cero");
        }
    }
}
