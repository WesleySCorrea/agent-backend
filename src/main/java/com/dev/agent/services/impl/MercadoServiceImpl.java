package com.dev.agent.services.impl;

import com.dev.agent.dto.mercado.request.MercadoRequestDTO;
import com.dev.agent.dto.mercado.response.MercadoResponseDTO;
import com.dev.agent.entity.Mercado;
import com.dev.agent.repository.MercadoRepository;
import lombok.RequiredArgsConstructor;
import com.dev.agent.services.MercadoService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MercadoServiceImpl implements MercadoService {
    private final MercadoRepository mercadoRepository;
    @Override
    public MercadoResponseDTO create(MercadoRequestDTO request) {

        Mercado mercado = mercadoRepository.save(request.toEntity());

        return new MercadoResponseDTO(mercado);
    }
}
