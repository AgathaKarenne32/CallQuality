package com.callquality.api.repository;

import com.callquality.api.model.Ligacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigacaoRepository extends JpaRepository<Ligacao, Long> {
}
