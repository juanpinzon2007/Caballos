package com.example.caballos.dto;

import com.example.caballos.model.ResultadoJuego;

public record ApuestaResponse(
        ResultadoJuego resultado,
        String caballoElegido,
        Integer puntosApostados,
        Boolean gano,
        Integer premio,
        Integer saldoActual,
        String mensaje
) {
}
