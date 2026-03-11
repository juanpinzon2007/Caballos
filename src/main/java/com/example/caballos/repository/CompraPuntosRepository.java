package com.example.caballos.repository;

import com.example.caballos.entity.CompraPuntosEntity;
import com.example.caballos.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraPuntosRepository extends JpaRepository<CompraPuntosEntity, Long> {
    List<CompraPuntosEntity> findTop6ByUsuarioOrderByFechaDesc(UsuarioEntity usuario);
}
