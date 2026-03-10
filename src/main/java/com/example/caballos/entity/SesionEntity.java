package com.example.caballos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
public class SesionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Column(nullable = false, unique = true, length = 80)
    private String token;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private Boolean activa;

    protected SesionEntity() {
    }

    public SesionEntity(UsuarioEntity usuario, String token, LocalDateTime fechaCreacion,
                        LocalDateTime fechaExpiracion, Boolean activa) {
        this.usuario = usuario;
        this.token = token;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.activa = activa;
    }

    public Long getId() {
        return id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }
}
