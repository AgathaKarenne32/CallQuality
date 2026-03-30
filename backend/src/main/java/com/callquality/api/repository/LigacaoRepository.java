package com.callquality.api.repository;

import com.callquality.api.model.Ligacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LigacaoRepository extends JpaRepository<Ligacao, Long> {
    // Busca personalizada
    List<Ligacao> findByAnalistaId(Long analistaId);

@Query("SELECT l FROM Ligacao l WHERE l.analista.supervisor.id = :supervisorId")
    List<Ligacao> findBySupervisorId(@Param("supervisorId") Long supervisorId);
}


