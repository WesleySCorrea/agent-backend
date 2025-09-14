package com.dev.agent.services;

import com.dev.agent.dto.mercado.request.MercadoRequestDTO;
import com.dev.agent.dto.mercado.response.MercadoResponseDTO;

public interface MercadoService {

    MercadoResponseDTO create(MercadoRequestDTO request);
}
