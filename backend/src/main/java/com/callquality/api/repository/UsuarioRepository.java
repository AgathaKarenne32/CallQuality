package com.callquality.api.repository;

import com.callquality.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca usuário por email
    Optional<Usuario> findByEmail(String email);

    // Verifica se existe
    boolean existsByEmail(String email);
}
