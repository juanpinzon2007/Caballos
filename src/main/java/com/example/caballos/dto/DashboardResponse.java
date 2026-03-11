package com.example.caballos.dto;

import java.util.List;

public record DashboardResponse(
        UsuarioResponse usuario,
        PlataformaResponse plataforma,
        List<RankingUsuarioResponse> ranking,
        List<ActividadApuestaResponse> apuestasRecientes,
        List<ActividadCompraResponse> comprasRecientes
) {
}
