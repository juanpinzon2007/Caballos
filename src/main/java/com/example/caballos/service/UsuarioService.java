package com.example.caballos.service;

import com.example.caballos.dto.ActividadApuestaResponse;
import com.example.caballos.dto.ActividadCompraResponse;
import com.example.caballos.dto.CompraPuntosResponse;
import com.example.caballos.dto.DashboardResponse;
import com.example.caballos.dto.GrupoEstadoResponse;
import com.example.caballos.dto.PlataformaResponse;
import com.example.caballos.dto.RankingUsuarioResponse;
import com.example.caballos.dto.UsuarioResponse;
import com.example.caballos.entity.ApuestaEntity;
import com.example.caballos.entity.CompraPuntosEntity;
import com.example.caballos.entity.GrupoEntity;
import com.example.caballos.entity.UsuarioEntity;
import com.example.caballos.exception.ReglaNegocioException;
import com.example.caballos.repository.ApuestaRepository;
import com.example.caballos.repository.CompraPuntosRepository;
import com.example.caballos.repository.GrupoRepository;
import com.example.caballos.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    public static final int PUNTOS_INICIALES = 1000;
    public static final int MAX_GRUPOS = 4;
    public static final int MAX_USUARIOS_POR_GRUPO = 4;
    public static final int PUNTOS_POR_PAQUETE = 1000;
    public static final int COSTO_POR_PAQUETE = 10_000;
    public static final int MULTIPLICADOR_GANANCIA = 5;

    private final UsuarioRepository usuarioRepository;
    private final GrupoRepository grupoRepository;
    private final CompraPuntosRepository compraPuntosRepository;
    private final ApuestaRepository apuestaRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository,
                          GrupoRepository grupoRepository,
                          CompraPuntosRepository compraPuntosRepository,
                          ApuestaRepository apuestaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.grupoRepository = grupoRepository;
        this.compraPuntosRepository = compraPuntosRepository;
        this.apuestaRepository = apuestaRepository;
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
    public UsuarioResponse registrar(String nombre, String password) {
        String nombreLimpio = normalizarNombre(nombre);
        validarPassword(password);

        if (usuarioRepository.findByNombreIgnoreCase(nombreLimpio).isPresent()) {
            throw new ReglaNegocioException("Ese nombre de usuario ya existe");
        }

        GrupoEntity grupo = obtenerGrupoDisponible();
        UsuarioEntity nuevo = new UsuarioEntity(
                nombreLimpio,
                PUNTOS_INICIALES,
                passwordEncoder.encode(password),
                grupo
        );
        UsuarioEntity guardado = usuarioRepository.save(nuevo);

        return mapUsuario(guardado);
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtener(Long usuarioId) {
        return mapUsuario(obtenerEntidad(usuarioId));
    }

    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfil(UsuarioEntity usuario) {
        return mapUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public DashboardResponse obtenerDashboard(UsuarioEntity usuario) {
        return new DashboardResponse(
                mapUsuario(usuario),
                construirPlataforma(),
                obtenerRanking(),
                apuestaRepository.findTop6ByUsuarioOrderByFechaDesc(usuario).stream()
                        .map(this::mapApuesta)
                        .toList(),
                compraPuntosRepository.findTop6ByUsuarioOrderByFechaDesc(usuario).stream()
                        .map(this::mapCompra)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public PlataformaResponse obtenerEstadoPlataforma() {
        return construirPlataforma();
    }

    @Transactional
    public CompraPuntosResponse comprarPuntos(UsuarioEntity usuario, Integer paquetes) {
        if (paquetes == null || paquetes <= 0) {
            throw new ReglaNegocioException("Debes comprar al menos 1 paquete de puntos");
        }

        if (paquetes > 1000) {
            throw new ReglaNegocioException("Compra demasiado grande en una sola operacion. Maximo 1000 paquetes");
        }

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
    public UsuarioEntity validarCredenciales(String nombre, String password) {
        String nombreLimpio = normalizarNombre(nombre);
        validarPassword(password);

        UsuarioEntity usuario = usuarioRepository.findByNombreIgnoreCase(nombreLimpio)
                .orElseThrow(() -> new ReglaNegocioException("Usuario o contrasena invalida"));

        if (usuario.getPasswordHash() == null || usuario.getPasswordHash().isBlank()) {
            throw new ReglaNegocioException("Usuario sin contrasena registrada. Crea una cuenta nueva.");
        }

        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new ReglaNegocioException("Usuario o contrasena invalida");
        }

        return usuario;
    }

    @Transactional(readOnly = true)
    public UsuarioEntity obtenerEntidad(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ReglaNegocioException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<RankingUsuarioResponse> obtenerRanking() {
        return usuarioRepository.findTop8ByOrderByPuntosDescNombreAsc().stream()
                .map(usuario -> new RankingUsuarioResponse(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getPuntos(),
                        usuario.getGrupo().getNumero()
                ))
                .collect(Collectors.toList());
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

    private void validarPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new ReglaNegocioException("La contrasena es obligatoria");
        }

        if (password.length() < 6 || password.length() > 72) {
            throw new ReglaNegocioException("La contrasena debe tener entre 6 y 72 caracteres");
        }
    }

    public UsuarioResponse mapUsuario(UsuarioEntity usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getPuntos(),
                usuario.getGrupo().getNumero()
        );
    }

    private PlataformaResponse construirPlataforma() {
        List<GrupoEntity> grupos = grupoRepository.findAll().stream()
                .sorted(Comparator.comparing(GrupoEntity::getNumero))
                .toList();

        int usuariosRegistrados = (int) usuarioRepository.count();
        int capacidadMaxima = MAX_GRUPOS * MAX_USUARIOS_POR_GRUPO;

        List<GrupoEstadoResponse> estadoGrupos = grupos.stream()
                .map(grupo -> {
                    int ocupacion = (int) usuarioRepository.countByGrupo(grupo);
                    return new GrupoEstadoResponse(
                            grupo.getNumero(),
                            ocupacion,
                            MAX_USUARIOS_POR_GRUPO,
                            ocupacion >= MAX_USUARIOS_POR_GRUPO
                    );
                })
                .toList();

        return new PlataformaResponse(
                usuariosRegistrados,
                capacidadMaxima,
                Math.max(capacidadMaxima - usuariosRegistrados, 0),
                PUNTOS_INICIALES,
                PUNTOS_POR_PAQUETE,
                COSTO_POR_PAQUETE,
                MULTIPLICADOR_GANANCIA,
                estadoGrupos
        );
    }

    private ActividadApuestaResponse mapApuesta(ApuestaEntity apuesta) {
        return new ActividadApuestaResponse(
                apuesta.getCaballoElegido(),
                apuesta.getCaballoGanador(),
                apuesta.getPuntosApostados(),
                apuesta.getGano(),
                apuesta.getPremio(),
                apuesta.getSaldoDespues(),
                apuesta.getFecha()
        );
    }

    private ActividadCompraResponse mapCompra(CompraPuntosEntity compra) {
        return new ActividadCompraResponse(
                compra.getPaquetes(),
                compra.getPuntosOtorgados(),
                compra.getCostoPesos(),
                compra.getSaldoDespues(),
                compra.getFecha()
        );
    }
}
