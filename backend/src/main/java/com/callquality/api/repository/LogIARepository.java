package com.callquality.api.repository;

import com.callquality.api.model.LogUsoIA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogIARepository extends JpaRepository<LogUsoIA, Long> {
    
    // Permite procurar logs de uma ligação específica para auditoria
    List<LogUsoIA> findByLigacaoId(Long ligacaoId);
}