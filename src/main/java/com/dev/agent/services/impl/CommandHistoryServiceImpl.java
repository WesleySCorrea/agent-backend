package com.dev.agent.services.impl;

import lombok.RequiredArgsConstructor;
import com.dev.agent.enums.StatusEnum;
import com.dev.agent.enums.CommandEnum;
import com.dev.agent.entity.CommandHistory;
import org.springframework.stereotype.Service;
import com.dev.agent.services.CommandHistoryService;
import com.dev.agent.repository.CommandHistoryRepository;

import java.util.Map;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommandHistoryServiceImpl implements CommandHistoryService {

    private final CommandHistoryRepository commandHistoryRepository;
    @Override
    public CommandHistory save(CommandHistory commandHistory) {
        return commandHistoryRepository.save(commandHistory);
    }

    @Override
    public CommandHistory create(String agentAddress, String usuario, Map<String, Object> map, String requestBody) {

        String correlationId = UUID.randomUUID().toString();
        String cmdStr = (String) map.get("cmd");

        CommandHistory history = new CommandHistory();
        history.setCorrelationId(correlationId);
        history.setUsuario(usuario);
        history.setAgentAddress(agentAddress);
        history.setCommand(CommandEnum.valueOf(cmdStr.toUpperCase()));
        history.setPath((String) map.get("path"));
        history.setStatus(StatusEnum.PENDING);
        history.setRequestPayload(requestBody);
        history.setCreatedAt(LocalDateTime.now());
        return commandHistoryRepository.save(history);
    }

    @Override
    public CommandHistory  updateStatus(CommandHistory commandHistory, StatusEnum status, String message, String payload) {

        commandHistory.setStatus(status);
        commandHistory.setFinishedAt(LocalDateTime.now());
        commandHistory.setMsg(message);
        commandHistory.setResponsePayload(payload);
        return commandHistoryRepository.save(commandHistory);
    }
}
