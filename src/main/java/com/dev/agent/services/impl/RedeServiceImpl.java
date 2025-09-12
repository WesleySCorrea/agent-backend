package com.dev.agent.services.impl;

import lombok.RequiredArgsConstructor;
import com.dev.agent.services.RedeService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.dev.agent.dto.rede.RedeResponseDTO;
import com.dev.agent.repository.RedeRepository;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class RedeServiceImpl implements RedeService {

    private final RedeRepository redeRepository;
    @Override
    public Page<RedeResponseDTO> findAll(Pageable pageable) {

        return redeRepository.findAll(pageable)
                .map(RedeResponseDTO::new);
    }

    @Override
    public Page<RedeResponseDTO> findByRede(Pageable pageable, String rede) {

        return  redeRepository.findByRedeContainingIgnoreCase(rede, pageable)
                .map(RedeResponseDTO::new);
    }
}
