package com.dev.agent.services;

import com.dev.agent.dto.rede.response.RedeWithMercadoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.dev.agent.dto.rede.request.RedeRequestDTO;
import com.dev.agent.dto.rede.response.RedeResponseDTO;
import com.dev.agent.dto.rede.response.RedeBasicResponseDTO;

import java.util.List;

public interface RedeService {

    List<RedeBasicResponseDTO> findAll();
    List<RedeWithMercadoResponseDTO>findAllWithMercados();
    Page<RedeResponseDTO> findByRede(Pageable pageable, String rede);
    RedeResponseDTO create(RedeRequestDTO request);
}
