package com.dev.agent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/send")
public class SendController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${app.queue}")
    private String queueName;

    @Value("${app.defaultPath}")
    private String defaultPath;

    public SendController(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/rpc")
    public ResponseEntity<?> rpcLs(@RequestParam(value = "path", required = false) String path,
                                   @RequestParam(value = "comand", required = false) String comand,
                                   @RequestParam(value = "name", required = false) String name) throws Exception {
        if (path == null || path.isBlank()) path = defaultPath;
        if (comand == null || comand.isBlank()) comand = "ls";
        if (comand.equals("mkdir") && (name == null) || ( path == null)) return ResponseEntity.status(400).body(Map.of("error", "name param required"));
        if (comand.equals("rm") && path.isBlank()) return ResponseEntity.status(400).body(Map.of("error", "name param required"));



        if (comand.equals("mkdir")) {
            path = path + "/" + name;
        }

        var msg = Map.of("cmd", comand, "path", path);
        var body = objectMapper.writeValueAsString(msg);

        // publicamos e aguardamos a RESPOSTA do agent (RPC)
        Object resp = rabbitTemplate.convertSendAndReceive(queueName, body);

        if (resp == null) {
            return ResponseEntity.status(504).body(Map.of(
                    "error", "timeout esperando resposta do agent",
                    "queue", queueName,
                    "message", msg
            ));
        }

        // converter resposta (pode vir como byte[] ou String)
        String json = (resp instanceof byte[])
                ? new String((byte[]) resp, java.nio.charset.StandardCharsets.UTF_8)
                : resp.toString();

        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = objectMapper.readValue(json, Map.class);

        return ResponseEntity.ok(parsed);
    }

}