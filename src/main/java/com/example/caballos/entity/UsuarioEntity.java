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

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    @Column(nullable = false)
    private Integer puntos;

    @Column(length = 120)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id")
    private GrupoEntity grupo;

    protected UsuarioEntity() {
    }

    public UsuarioEntity(String nombre, Integer puntos, String passwordHash, GrupoEntity grupo) {
        this.nombre = nombre;
        this.puntos = puntos;
        this.passwordHash = passwordHash;
        this.grupo = grupo;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public GrupoEntity getGrupo() {
        return grupo;
    }
}
