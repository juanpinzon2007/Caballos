package com.example.caballos.dto;

import java.util.List;

public record PlataformaResponse(
        Integer usuariosRegistrados,
        Integer capacidadMaxima,
        Integer cuposDisponibles,
        Integer puntosIniciales,
        Integer puntosPorPaquete,
        Integer costoPorPaquete,
        Integer multiplicadorGanancia,
        List<GrupoEstadoResponse> grupos
) {
}
