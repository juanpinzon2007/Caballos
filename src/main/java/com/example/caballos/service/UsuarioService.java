package com.example.caballos.service;

import com.example.caballos.dto.CompraPuntosResponse;
import com.example.caballos.dto.UsuarioResponse;
import com.example.caballos.entity.CompraPuntosEntity;
import com.example.caballos.entity.GrupoEntity;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.exception.ReglaNegocioException;
import com.example.caballos.repository.CompraPuntosRepository;
import com.example.caballos.repository.GrupoRepository;
import com.example.caballos.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class UsuarioService {

    public static final int PUNTOS_INICIALES = 1000;
    public static final int MAX_GRUPOS = 4;
    public static final int MAX_USUARIOS_POR_GRUPO = 4;
    public static final int PUNTOS_POR_PAQUETE = 1000;
    public static final int COSTO_POR_PAQUETE = 10_000;

    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final CompraPuntosRepository compraPuntosRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          GrupoRepository grupoRepository,
                          CompraPuntosRepository compraPuntosRepository) {
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
        this.compraPuntosRepository = compraPuntosRepository;
    }

    @PostConstruct
    @Transactional
    public void inicializarGrupos() {
        for (int numero = 1; numero <= MAX_GRUPOS; numero++) {
            if (grupoRepository.findByNumero(numero).isEmpty()) {
                grupoRepository.save(new GrupoEntity(numero));
            }
        }
    }

    @Transactional
    public UsuarioResponse registrar(String nombre) {
        String nombreLimpio = normalizarNombre(nombre);

        UsuarioEntity existente = usuarioRepository.findByNombreIgnoreCase(nombreLimpio).orElse(null);
        if (existente != null) {
            return mapUsuario(existente);
        }

        GrupoEntity grupo = obtenerGrupoDisponible();
        UsuarioEntity nuevo = new UsuarioEntity(nombreLimpio, PUNTOS_INICIALES, grupo);
        UsuarioEntity guardado = usuarioRepository.save(nuevo);

        return mapUsuario(guardado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long usuarioId) {
        return mapUsuario(obtenerEntidad(usuarioId));
    }

    @Transactional
    public CompraPuntosResponse comprarPuntos(Long usuarioId, Integer paquetes) {
        if (paquetes == null || paquetes <= 0) {
            throw new ReglaNegocioException("Debes comprar al menos 1 paquete de puntos");
        }

        UsuarioEntity usuario = obtenerEntidad(usuarioId);
        int puntosRecibidos = paquetes * PUNTOS_POR_PAQUETE;
        int costoPesos = paquetes * COSTO_POR_PAQUETE;

        usuario.setPuntos(usuario.getPuntos() + puntosRecibidos);

        compraPuntosRepository.save(new CompraPuntosEntity(
                usuario,
                paquetes,
                puntosRecibidos,
                costoPesos,
                usuario.getPuntos(),
                LocalDateTime.now()
        ));

        return new CompraPuntosResponse(paquetes, puntosRecibidos, costoPesos, usuario.getPuntos());
    }

    @Transactional(readOnly = true)
    public UsuarioEntity obtenerEntidad(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ReglaNegocioException("Usuario no encontrado"));
    }

    private GrupoEntity obtenerGrupoDisponible() {
        List<GrupoEntity> grupos = grupoRepository.findAll();
        grupos.sort(Comparator.comparing(GrupoEntity::getNumero));

        for (GrupoEntity grupo : grupos) {
            long cantidad = usuarioRepository.countByGrupo(grupo);
            if (cantidad < MAX_USUARIOS_POR_GRUPO) {
                return grupo;
            }
        }

        throw new ReglaNegocioException("Capacidad completa: maximo 4 grupos de 4 usuarios");
    }

    private String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ReglaNegocioException("El nombre es obligatorio");
        }

        String limpio = nombre.trim();
        if (limpio.length() < 3 || limpio.length() > 60) {
            throw new ReglaNegocioException("El nombre debe tener entre 3 y 60 caracteres");
        }

        return limpio;
    }

    private UsuarioResponse mapUsuario(UsuarioEntity usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPuntos(),
                usuario.getGrupo().getNumero()
        );
    }
}
