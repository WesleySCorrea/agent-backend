package com.dev.agent.repository;

import com.dev.agent.entity.Mercado;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MercadoRepository extends JpaRepository<Mercado, Long> {
}
