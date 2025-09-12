package com.dev.agent.repository;

import com.dev.agent.entity.Rede;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RedeRepository extends JpaRepository<Rede, Long> {
    Page<Rede> findByRedeContainingIgnoreCase(String rede, Pageable pageable);
}
