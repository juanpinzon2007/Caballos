package com.example.caballos.dto;

public record GrupoEstadoResponse(
        Integer numero,
        Integer ocupacion,
        Integer capacidad,
        Boolean lleno
) {
}
