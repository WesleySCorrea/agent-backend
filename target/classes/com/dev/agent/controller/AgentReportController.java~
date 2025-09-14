package com.dev.agent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Recebe o POST do agente em /agent/report e apenas loga/imprime o conteúdo.
 */
@RestController
@RequestMapping("/agent")
public class AgentReportController {

    private static final Logger LOG = LoggerFactory.getLogger(AgentReportController.class);

    @PostMapping(path = "/report", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> receiveReport(@RequestBody Map<String, Object> body) {
        // Log estruturado
        LOG.info("AGENT REPORT RECEIVED: {}", body);

        // Opcional: prints no console (já vem via log)
        LOG.info("=== AGENT REPORT ===");
        LOG.info(body.toString());
        LOG.info("====================");

        return ResponseEntity.ok(Map.of(
                "received", true,
                "size", body.size()
        ));
    }
}