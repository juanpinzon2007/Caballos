package com.example.caballos.repository;

import com.example.caballos.entity.GrupoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GrupoRepository extends JpaRepository<GrupoEntity, Long> {
    Optional<GrupoEntity> findByNumero(Integer numero);
}
