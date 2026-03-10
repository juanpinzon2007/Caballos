package com.example.caballos.repository;

import com.example.caballos.entity.SesionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SesionRepository extends JpaRepository<SesionEntity, Long> {
    Optional<SesionEntity> findByToken(String token);
}
