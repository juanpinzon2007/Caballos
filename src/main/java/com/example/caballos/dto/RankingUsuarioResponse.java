package com.example.caballos.dto;

public record RankingUsuarioResponse(
        Long id,
        String nombre,
        Integer puntos,
        Integer grupo
) {
}
