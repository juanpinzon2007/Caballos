package com.example.caballos.repository;

import com.example.caballos.entity.ApuestaEntity;
import com.example.caballos.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApuestaRepository extends JpaRepository<ApuestaEntity, Long> {
    List<ApuestaEntity> findTop6ByUsuarioOrderByFechaDesc(UsuarioEntity usuario);
}
