package com.example.caballos.controller;

import com.example.caballos.model.ResultadoJuego;
import com.example.caballos.service.JuegoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JuegoApiController {
    private final JuegoService juegoService;

    public JuegoApiController(JuegoService juegoService) {
        this.juegoService = juegoService;
    }

    @GetMapping("/api/juego")
    public ResultadoJuego juego() {
        return juegoService.jugar();
    }
}
