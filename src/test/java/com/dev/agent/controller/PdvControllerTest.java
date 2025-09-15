package com.dev.agent.controller;

import org.mockito.Mockito;
import org.instancio.Select;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import com.dev.agent.services.PdvService;
import org.springframework.http.MediaType;
import com.dev.agent.dto.pdv.request.PdvRequestDTO;
import org.springframework.test.web.servlet.MockMvc;
import com.dev.agent.dto.pdv.response.PdvResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(PdvController.class)
public class PdvControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PdvService pdvService;
    @Test
    void createPdv() throws Exception {
        PdvRequestDTO request = Instancio.of(PdvRequestDTO.class)
                .set(Select.field("pdvName"), "PDV Teste")
                .set(Select.field("agentAdress"), "127.0.0.1")
                .set(Select.field("sistema"), "LINUX")
                .set(Select.field("versao"), "1.0.0")
                .set(Select.field("mercadoId"), 10L)
                .create();

        PdvResponseDTO response = Instancio.of(PdvResponseDTO.class)
                .set(Select.field("id"), 1L)
                .set(Select.field("pdvName"), request.getPdvName())
                .set(Select.field("agentAdress"), request.getAgentAdress())
                .set(Select.field("sistema"), request.getSistema())
                .set(Select.field("versao"), request.getVersao())
                .set(Select.field("mercadoId"), request.getMercadoId())
                .create();

        Mockito.when(pdvService.create(Mockito.any(PdvRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(post("/pdv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "pdv_name": "%s",
                              "agent_adress": "%s",
                              "sistema": "%s",
                              "versao": "%s",
                              "mercado_id": %d
                            }
                            """.formatted(
                                request.getPdvName(),
                                request.getAgentAdress(),
                                request.getSistema(),
                                request.getVersao(),
                                request.getMercadoId()
                        )))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.pdvName").value("PDV Teste"))
                .andExpect(jsonPath("$.agentAdress").value("127.0.0.1"))
                .andExpect(jsonPath("$.sistema").value("LINUX"))
                .andExpect(jsonPath("$.versao").value("1.0.0"))
                .andExpect(jsonPath("$.mercadoId").value(10));
    }
}
