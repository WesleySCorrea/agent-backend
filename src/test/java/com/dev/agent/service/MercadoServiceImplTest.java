package com.dev.agent.service;

import org.mockito.Mockito;
import org.instancio.Select;
import org.instancio.Instancio;
import com.dev.agent.entity.Rede;
import org.junit.jupiter.api.Test;
import com.dev.agent.entity.Mercado;
import com.dev.agent.repository.MercadoRepository;
import com.dev.agent.services.impl.MercadoServiceImpl;
import com.dev.agent.dto.mercado.request.MercadoRequestDTO;
import com.dev.agent.dto.mercado.response.MercadoResponseDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MercadoServiceImplTest {
    private final MercadoRepository mercadoRepository = Mockito.mock(MercadoRepository.class);
    private final MercadoServiceImpl mercadoService = new MercadoServiceImpl(mercadoRepository);

    @Test
    void createMercado() {
        MercadoRequestDTO request = Instancio.of(MercadoRequestDTO.class)
                .set(Select.field("mercado"), "Supermercado Teste")
                .create();

        Rede rede = new Rede();
        rede.setId(1L);
        Mercado savedEntity = new Mercado();
        savedEntity.setId(1L);
        savedEntity.setRede(rede);
        savedEntity.setMercado(request.getMercado());

        Mockito.when(mercadoRepository.save(Mockito.any(Mercado.class)))
                .thenReturn(savedEntity);

        MercadoResponseDTO response = mercadoService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("Supermercado Teste", response.getMercado());

        Mockito.verify(mercadoRepository).save(Mockito.argThat(m -> m.getMercado().equals(request.getMercado())));
    }
}
