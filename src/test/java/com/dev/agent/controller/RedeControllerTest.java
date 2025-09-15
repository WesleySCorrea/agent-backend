package com.dev.agent.controller;

import org.mockito.Mockito;
import org.instancio.Select;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import com.dev.agent.services.RedeService;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import com.dev.agent.dto.rede.request.RedeRequestDTO;
import com.dev.agent.dto.rede.response.RedeResponseDTO;
import com.dev.agent.dto.rede.response.RedeBasicResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import com.dev.agent.dto.rede.response.RedeWithMercadoResponseDTO;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(RedeController.class)
public class RedeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RedeService redeService;

    @Test
    void findAll() throws Exception {
        List<RedeBasicResponseDTO> redeList = Instancio.ofList(RedeBasicResponseDTO.class)
                .size(2)
                .create();

        Mockito.when(redeService.findAll()).thenReturn(redeList);

        mockMvc.perform(get("/rede")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findAllWithMercado() throws Exception {
        List<RedeWithMercadoResponseDTO> redeList = Instancio.ofList(RedeWithMercadoResponseDTO.class)
                .size(2)
                .create();

        Mockito.when(redeService.findAllWithMercados()).thenReturn(redeList);

        mockMvc.perform(get("/rede/mercado")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findByRede() throws Exception {
        // Cria uma página de DTOs simulada
        List<RedeResponseDTO> redeList = Instancio.ofList(RedeResponseDTO.class)
                .size(2)
                .create();
        PageImpl<RedeResponseDTO> page = new PageImpl<>(redeList);

        // Mock do service
        Mockito.when(redeService.findByRede(Mockito.any(Pageable.class), Mockito.eq("RedeTeste")))
                .thenReturn(page);

        // Executa GET passando a path variable e pageable como query params
        mockMvc.perform(get("/rede/{rede}", "RedeTeste")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void createRede() throws Exception {
        RedeRequestDTO request = Instancio.of(RedeRequestDTO.class)
                .set(Select.field("rede"), "Rede Teste")
                .create();

        RedeResponseDTO response = Instancio.of(RedeResponseDTO.class)
                .set(Select.field("id"), 1L)
                .set(Select.field("rede"), request.getRede())
                .create();

        Mockito.when(redeService.create(Mockito.any(RedeRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/rede")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "nome_rede": "%s"
                            }
                            """.formatted(request.getRede())))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.rede").value("Rede Teste"));
    }
}