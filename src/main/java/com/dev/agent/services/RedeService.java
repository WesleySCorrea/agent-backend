package com.dev.agent.services;

import com.dev.agent.dto.rede.RedeRequestDTO;
import org.springframework.data.domain.Page;
import com.dev.agent.dto.rede.RedeResponseDTO;
import org.springframework.data.domain.Pageable;

public interface RedeService {

    Page<RedeResponseDTO> findAll(Pageable pageable);
    Page<RedeResponseDTO> findByRede(Pageable pageable, String rede);
    RedeResponseDTO create(RedeRequestDTO request);
}
