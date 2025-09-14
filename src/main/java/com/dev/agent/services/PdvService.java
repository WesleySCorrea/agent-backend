package com.dev.agent.services;

import com.dev.agent.dto.pdv.request.PdvRequestDTO;
import com.dev.agent.dto.pdv.response.PdvResponseDTO;

public interface PdvService {

    PdvResponseDTO create(PdvRequestDTO request);
}
