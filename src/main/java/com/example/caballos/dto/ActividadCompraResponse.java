package com.example.caballos.dto;

import java.time.LocalDateTime;

public record ActividadCompraResponse(
        Integer paquetes,
        Integer puntosRecibidos,
        Integer costoPesos,
        Integer saldoDespues,
        LocalDateTime fecha
) {
}
