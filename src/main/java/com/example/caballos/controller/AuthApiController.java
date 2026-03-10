package com.example.caballos.controller;

import com.example.caballos.dto.AuthResponse;
import com.example.caballos.dto.LoginRequest;
import com.example.caballos.dto.RegistroUsuarioRequest;
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
public class AuthApiController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    public AuthApiController(AuthService authService, UsuarioService usuarioService) {
        this.authService = authService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/api/auth/registro")
    public AuthResponse registro(@RequestBody RegistroUsuarioRequest request) {
        return authService.registrarYAutenticar(request.nombre(), request.password());
    }

    @PostMapping("/api/auth/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request.nombre(), request.password());
    }

    @PostMapping("/api/auth/logout")
    public void logout(@RequestHeader("Authorization") String authorization) {
        authService.logout(authorization);
    }

    @GetMapping("/api/auth/me")
    public UsuarioResponse me(@RequestHeader("Authorization") String authorization) {
        UsuarioEntity usuario = authService.autenticar(authorization);
        return usuarioService.obtenerPerfil(usuario);
    }
}
