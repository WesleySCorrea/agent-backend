package com.dev.agent.services;

import com.dev.agent.enums.StatusEnum;
import com.dev.agent.entity.CommandHistory;

import java.util.Map;

public interface CommandHistoryService {
    CommandHistory save(CommandHistory commandHistory);
    CommandHistory create(String agentAddress, String usuario, Map<String, Object> map, String requestBody);
    CommandHistory updateStatus(CommandHistory commandHistory, StatusEnum status, String message, String payload);
}
