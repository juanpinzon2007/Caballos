package com.example.caballos.controller;

import com.example.caballos.dto.CompraPuntosResponse;
import com.example.caballos.dto.ComprarPuntosRequest;
import com.example.caballos.dto.DashboardResponse;
import com.example.caballos.dto.PlataformaResponse;
import com.example.caballos.dto.UsuarioResponse;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.service.AuthService;
import com.example.caballos.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioApiController {

    private final UsuarioService usuarioService;
    private final AuthService authService;

    public UsuarioApiController(UsuarioService usuarioService, AuthService authService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
    }

    @GetMapping("/api/usuarios/me")
    public UsuarioResponse obtener(@RequestHeader("Authorization") String authorization) {
        UsuarioEntity usuario = authService.autenticar(authorization);
        return usuarioService.obtenerPerfil(usuario);
    }

    @GetMapping("/api/usuarios/me/dashboard")
    public DashboardResponse dashboard(@RequestHeader("Authorization") String authorization) {
        UsuarioEntity usuario = authService.autenticar(authorization);
        return usuarioService.obtenerDashboard(usuario);
    }

    @PostMapping("/api/usuarios/me/comprar")
    public CompraPuntosResponse comprarPuntos(@RequestHeader("Authorization") String authorization,
                                              @RequestBody ComprarPuntosRequest request) {
        UsuarioEntity usuario = authService.autenticar(authorization);
        return usuarioService.comprarPuntos(usuario, request.paquetes());
    }

    @GetMapping("/api/plataforma")
    public PlataformaResponse plataforma() {
        return usuarioService.obtenerEstadoPlataforma();
    }
}
