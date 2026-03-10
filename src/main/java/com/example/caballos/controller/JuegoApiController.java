package com.example.caballos.controller;

import com.example.caballos.dto.ApuestaRequest;
import com.example.caballos.dto.ApuestaResponse;
import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.service.ApuestaService;
import com.example.caballos.service.JuegoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JuegoApiController {
    private final JuegoService juegoService;
    private final ApuestaService apuestaService;

    public JuegoApiController(JuegoService juegoService, ApuestaService apuestaService) {
        this.juegoService = juegoService;
        this.apuestaService = apuestaService;
    }

    @GetMapping("/api/juego")
    public ResultadoJuego juego() {
        return juegoService.jugar();
    }

    @PostMapping("/api/juego/apostar")
    public ApuestaResponse apostar(@RequestBody ApuestaRequest request) {
        return apuestaService.apostar(request.usuarioId(), request.caballo(), request.puntosApostados());
    }
}
