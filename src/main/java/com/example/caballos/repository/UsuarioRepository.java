package com.example.caballos.repository;

import com.example.caballos.entity.GrupoEntity;
import com.example.caballos.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByNombreIgnoreCase(String nombre);

    long countByGrupo(GrupoEntity grupo);
}
