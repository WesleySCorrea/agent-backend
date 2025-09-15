package com.dev.agent.service;

import org.mockito.Mockito;
import org.instancio.Select;
import org.instancio.Instancio;
import com.dev.agent.entity.Pdv;
import org.junit.jupiter.api.Test;
import com.dev.agent.entity.Mercado;
import com.dev.agent.enums.SistemaEnum;
import com.dev.agent.repository.PdvRepository;
import com.dev.agent.services.impl.PdvServiceImpl;
import com.dev.agent.dto.pdv.request.PdvRequestDTO;
import com.dev.agent.dto.pdv.response.PdvResponseDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PdvServiceImplTest {
    private final PdvRepository pdvRepository = Mockito.mock(PdvRepository.class);
    private final PdvServiceImpl pdvService = new PdvServiceImpl(pdvRepository);

    @Test
    void createPdv() {
        PdvRequestDTO request = Instancio.of(PdvRequestDTO.class)
                .set(Select.field("pdvName"), "PDV Teste")
                .set(Select.field("agentAdress"), "127.0.0.1")
                .set(Select.field("sistema"), SistemaEnum.WINDOWS.name())
                .set(Select.field("versao"), "1.0.0")
                .set(Select.field("mercadoId"), 1L)
                .create();

        Mercado mercado = new Mercado();
        mercado.setId(request.getMercadoId());

        Pdv savedEntity = new Pdv();
        savedEntity.setId(1L);
        savedEntity.setPdvName(request.getPdvName());
        savedEntity.setAgentAdress(request.getAgentAdress());
        savedEntity.setSistema(SistemaEnum.WINDOWS);
        savedEntity.setVersao(request.getVersao());
        savedEntity.setMercado(mercado);

        Mockito.when(pdvRepository.save(Mockito.any(Pdv.class))).thenReturn(savedEntity);

        PdvResponseDTO response = pdvService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("PDV Teste", response.getPdvName());
        assertEquals("127.0.0.1", response.getAgentAdress());
        assertEquals("WINDOWS", response.getSistema());
        assertEquals("1.0.0", response.getVersao());
        assertEquals(1L, response.getMercadoId());

        Mockito.verify(pdvRepository).save(Mockito.argThat(p ->
                p.getPdvName().equals(request.getPdvName()) &&
                        p.getSistema() == SistemaEnum.WINDOWS
        ));
    }
}