package com.example.caballos.service;

import com.example.caballos.dto.AuthResponse;
import com.example.caballos.dto.UsuarioResponse;
import com.example.caballos.entity.SesionEntity;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.exception.AuthException;
import com.example.caballos.repository.SesionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private static final int DURACION_HORAS = 24;

    private final UsuarioService usuarioService;
    private final SesionRepository sesionRepository;

    public AuthService(UsuarioService usuarioService, SesionRepository sesionRepository) {
        this.usuarioService = usuarioService;
        this.sesionRepository = sesionRepository;
    }

    @Transactional
    public AuthResponse registrarYAutenticar(String nombre, String password) {
        UsuarioResponse usuario = usuarioService.registrar(nombre, password);
        UsuarioEntity entidad = usuarioService.obtenerEntidad(usuario.id());
        return crearSesion(entidad);
    }

    @Transactional
    public AuthResponse login(String nombre, String password) {
        UsuarioEntity usuario = usuarioService.validarCredenciales(nombre, password);
        return crearSesion(usuario);
    }

    @Transactional(readOnly = true)
    public UsuarioEntity autenticar(String authorizationHeader) {
        String token = extraerToken(authorizationHeader);

        SesionEntity sesion = sesionRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Sesion invalida"));

        if (!Boolean.TRUE.equals(sesion.getActiva())) {
            throw new AuthException("Sesion cerrada");
        }

        if (sesion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new AuthException("Sesion expirada");
        }

        return sesion.getUsuario();
    }

    @Transactional
    public void logout(String authorizationHeader) {
        String token = extraerToken(authorizationHeader);
        SesionEntity sesion = sesionRepository.findByToken(token)
                .orElseThrow(() -> new AuthException("Sesion invalida"));

        sesion.setActiva(false);
    }

    private AuthResponse crearSesion(UsuarioEntity usuario) {
        String token = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        LocalDateTime ahora = LocalDateTime.now();

        SesionEntity sesion = new SesionEntity(
                usuario,
                token,
                ahora,
                ahora.plusHours(DURACION_HORAS),
                true
        );
        sesionRepository.save(sesion);

        return new AuthResponse(token, usuarioService.mapUsuario(usuario));
    }

    private String extraerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new AuthException("No autorizado");
        }

        String prefix = "Bearer ";
        if (!authorizationHeader.startsWith(prefix)) {
            throw new AuthException("Token invalido");
        }

        String token = authorizationHeader.substring(prefix.length()).trim();
        if (token.isEmpty()) {
            throw new AuthException("Token invalido");
        }

        return token;
    }
}
