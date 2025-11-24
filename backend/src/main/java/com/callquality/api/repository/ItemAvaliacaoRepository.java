package com.callquality.api.repository;

import com.callquality.api.model.ItemAvaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAvaliacaoRepository extends JpaRepository<ItemAvaliacao, Long> {
}
