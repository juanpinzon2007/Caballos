package com.example.caballos.controller;

import com.example.caballos.dto.CompraPuntosResponse;
import com.example.caballos.dto.ComprarPuntosRequest;
import com.example.caballos.dto.RegistroUsuarioRequest;
import com.example.caballos.dto.UsuarioResponse;
import com.example.caballos.service.UsuarioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsuarioApiController {

    private final UsuarioService usuarioService;

    public UsuarioApiController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/api/usuarios/registrar")
    public UsuarioResponse registrar(@RequestBody RegistroUsuarioRequest request) {
        return usuarioService.registrar(request.nombre());
    }

    @GetMapping("/api/usuarios/{usuarioId}")
    public UsuarioResponse obtener(@PathVariable Long usuarioId) {
        return usuarioService.obtener(usuarioId);
    }

    @PostMapping("/api/usuarios/{usuarioId}/comprar")
    public CompraPuntosResponse comprarPuntos(@PathVariable Long usuarioId,
                                              @RequestBody ComprarPuntosRequest request) {
        return usuarioService.comprarPuntos(usuarioId, request.paquetes());
    }
}
