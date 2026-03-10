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
@Table(name = "compras_puntos")
public class CompraPuntosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private Integer paquetes;

    @Column(nullable = false)
    private Integer puntosOtorgados;

    @Column(nullable = false)
    private Integer costoPesos;

    @Column(nullable = false)
    private Integer saldoDespues;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected CompraPuntosEntity() {
    }

    public CompraPuntosEntity(UsuarioEntity usuario, Integer paquetes, Integer puntosOtorgados,
                              Integer costoPesos, Integer saldoDespues, LocalDateTime fecha) {
        this.usuario = usuario;
        this.paquetes = paquetes;
        this.puntosOtorgados = puntosOtorgados;
        this.costoPesos = costoPesos;
        this.saldoDespues = saldoDespues;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }
}
