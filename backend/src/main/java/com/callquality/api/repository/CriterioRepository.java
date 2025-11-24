package com.callquality.api.repository;

import com.callquality.api.model.Criterio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CriterioRepository extends JpaRepository<Criterio, Long> {
    
    // Método para listar apenas os critérios ativos (para não usar regras antigas)
    List<Criterio> findByAtivoTrue();
}
