package com.example.caballos.controller;

import com.example.caballos.dto.ApuestaRequest;
import com.example.caballos.dto.ApuestaResponse;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.service.ApuestaService;
import com.example.caballos.service.AuthService;
import com.example.caballos.service.JuegoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JuegoApiController {
    private final JuegoService juegoService;
    private final ApuestaService apuestaService;
    private final AuthService authService;

    public JuegoApiController(JuegoService juegoService, ApuestaService apuestaService, AuthService authService) {
        this.juegoService = juegoService;
        this.apuestaService = apuestaService;
        this.authService = authService;
    }

    @GetMapping("/api/juego")
    public ResultadoJuego juego() {
        return juegoService.jugar();
    }

    @PostMapping("/api/juego/apostar")
    public ApuestaResponse apostar(@RequestHeader("Authorization") String authorization,
                                   @RequestBody ApuestaRequest request) {
        UsuarioEntity usuario = authService.autenticar(authorization);
        return apuestaService.apostar(usuario, request.caballo(), request.puntosApostados());
    }
}
