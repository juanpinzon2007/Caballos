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
@Table(name = "apuestas")
public class ApuestaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Column(nullable = false, length = 40)
    private String caballoElegido;

    @Column(nullable = false)
    private Integer puntosApostados;

    @Column(nullable = false, length = 40)
    private String caballoGanador;

    @Column(nullable = false)
    private Boolean gano;

    @Column(nullable = false)
    private Integer premio;

    @Column(nullable = false)
    private Integer saldoAntes;

    @Column(nullable = false)
    private Integer saldoDespues;

    @Column(nullable = false)
    private LocalDateTime fecha;

    protected ApuestaEntity() {
    }

    public ApuestaEntity(UsuarioEntity usuario, String caballoElegido, Integer puntosApostados,
                         String caballoGanador, Boolean gano, Integer premio,
                         Integer saldoAntes, Integer saldoDespues, LocalDateTime fecha) {
        this.usuario = usuario;
        this.caballoElegido = caballoElegido;
        this.puntosApostados = puntosApostados;
        this.caballoGanador = caballoGanador;
        this.gano = gano;
        this.premio = premio;
        this.saldoAntes = saldoAntes;
        this.saldoDespues = saldoDespues;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public String getCaballoElegido() {
        return caballoElegido;
    }

    public Integer getPuntosApostados() {
        return puntosApostados;
    }

    public String getCaballoGanador() {
        return caballoGanador;
    }

    public Boolean getGano() {
        return gano;
    }

    public Integer getPremio() {
        return premio;
    }

    public Integer getSaldoDespues() {
        return saldoDespues;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
