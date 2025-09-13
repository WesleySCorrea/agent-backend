package com.dev.agent.repository;

import com.dev.agent.entity.CommandHistory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CommandHistoryRepository extends JpaRepository<CommandHistory, Long> {
}
