package com.dev.agent.repository;

import com.dev.agent.entity.Pdv;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PdvRepository extends JpaRepository<Pdv, Long> {
}
