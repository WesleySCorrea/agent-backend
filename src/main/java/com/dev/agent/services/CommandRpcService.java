package com.dev.agent.services;

import java.util.Map;

public interface CommandRpcService {
    Map<String, Object> sendCommandAndReceiveResponse(Map<String, Object> commandPayload, String agentAddress);
}
