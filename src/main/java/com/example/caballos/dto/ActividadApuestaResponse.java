package com.example.caballos.dto;

import java.time.LocalDateTime;

public record ActividadApuestaResponse(
        String caballoElegido,
        String caballoGanador,
        Integer puntosApostados,
        Boolean gano,
        Integer premio,
        Integer saldoDespues,
        LocalDateTime fecha
) {
}
