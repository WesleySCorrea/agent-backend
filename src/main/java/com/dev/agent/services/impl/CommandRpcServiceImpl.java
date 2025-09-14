package com.dev.agent.services.impl;

import com.dev.agent.enums.StatusEnum;
import lombok.RequiredArgsConstructor;
import com.dev.agent.entity.CommandHistory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.dev.agent.services.CommandRpcService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dev.agent.services.CommandHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommandRpcServiceImpl implements CommandRpcService {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final CommandHistoryService historyService;

    /**
     * Envia um mapa de parâmetros para um agente via RabbitMQ RPC e retorna a resposta.
     * Registra o histórico do comando.
     *
     * @param commandPayload Um mapa contendo o comando e seus parâmetros para o agente.
     * @param agentAddress   Endereço da fila do agente.
     * @return Um mapa representando a resposta JSON do agente.
     * @throws JsonProcessingException Se houver erro ao serializar/desserializar JSON.
     * @throws ResponseStatusException Se ocorrer um timeout ou erro na comunicação RabbitMQ.
     */
    @Override
    public Map<String, Object> sendCommandAndReceiveResponse(Map<String, Object> commandPayload, String agentAddress) {

        if (agentAddress == null || agentAddress.equals("")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O 'agentAddress' é obrigatório para enviar o comando.");
        }

        String cmdStr = extractString(commandPayload, "cmd");
        if (cmdStr == null || cmdStr.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "'cmd' é obrigatório no payload.");
        }

        String body = null;
        try {
            body = objectMapper.writeValueAsString(commandPayload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar comando", e);
        }

        CommandHistory history = null;
        if(!cmdStr.equals("ls")) history = historyService.create(agentAddress, "Teste", commandPayload, body);

        Object resp = rabbitTemplate.convertSendAndReceive(agentAddress, body);


        if (resp == null) {

            if(!cmdStr.equals("ls")) historyService.updateStatus(history, StatusEnum.ERROR, "Erro interno", null);
            return Map.of(
                    "status", StatusEnum.ERROR,
                    "error", "timeout esperando resposta do agent",
                    "queue", agentAddress,
                    "message", commandPayload
            );
        }

        // converter resposta (pode vir como byte[] ou String)
        String json = (resp instanceof byte[])
                ? new String((byte[]) resp, java.nio.charset.StandardCharsets.UTF_8)
                : resp.toString();

        Map<String, Object> parsed = null;
        try {
            parsed = objectMapper.readValue(json, Map.class);
            parsed.put("status", StatusEnum.SUCCESS);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao serializar comando", e);
        }

        if(!cmdStr.equals("ls")) historyService.updateStatus(history, StatusEnum.SUCCESS, "Ação finalizada", json);

        return parsed;
    }

    private String extractString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val == null) return null;
        if (val instanceof String) return (String) val;
        return val.toString();
    }
}

