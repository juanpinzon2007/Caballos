package com.example.caballos.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "grupos")
public class GrupoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<UsuarioEntity> usuarios = new ArrayList<>();

    protected GrupoEntity() {
    }

    public GrupoEntity(Integer numero) {
        this.numero = numero;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public List<UsuarioEntity> getUsuarios() {
        return usuarios;
    }
}
