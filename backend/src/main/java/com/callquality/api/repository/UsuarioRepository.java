package com.callquality.api.repository;

import com.callquality.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Busca quem responde a esse supervisor
    List<Usuario> findBySupervisorId(Long supervisorId);
}
