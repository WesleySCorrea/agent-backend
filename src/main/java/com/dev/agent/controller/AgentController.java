package com.dev.agent.controller;

import lombok.RequiredArgsConstructor;
import com.dev.agent.enums.StatusEnum;
import com.dev.agent.enums.CommandEnum;
import org.springframework.http.ResponseEntity;
import com.dev.agent.services.CommandRpcService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentController {

    private final CommandRpcService commandRpcService;

    @GetMapping("/list")
    public ResponseEntity<?> listFiles(@RequestParam(value = "path", required = false) String path,
                                       @RequestParam(value = "agentAddress", required = true) String agentAddress) {

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.LS.getCommand());
            lsCommandPayload.put("path", path);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/open")
    public ResponseEntity<?> openFile(@RequestBody Map<String, String> body) {

        String path = body.get("path");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.OPEN.getCommand());
            lsCommandPayload.put("path", path);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveFile(@RequestBody Map<String, String> body) {

        String path = body.get("path");
        String content = body.get("content");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.SAVE.getCommand());
            lsCommandPayload.put("path", path);
            lsCommandPayload.put("content", content);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFolder(@RequestBody Map<String, String> body) {

        String name = body.get("name");
        String path = body.get("path");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.MKDIR.getCommand());
            lsCommandPayload.put("path", path + "/" + name);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Map<String, String> body) {

        String path = body.get("path");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.RM.getCommand());
            lsCommandPayload.put("path", path);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/download")
    public ResponseEntity<?> downloadFile(@RequestBody Map<String, String> body) {

        String url = body.get("url");
        String path = body.get("path");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.DOWN.getCommand());
            lsCommandPayload.put("path", path);
            lsCommandPayload.put("url", url);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/copy")
    public ResponseEntity<?> copyFile(@RequestBody Map<String, String> body) {

        String path = body.get("path");
        String oldPath = body.get("oldPath");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.COPY.getCommand());
            lsCommandPayload.put("path", path);
            lsCommandPayload.put("oldPath", oldPath);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }

    @PostMapping("/rename")
    public ResponseEntity<?> rename(@RequestBody Map<String, String> body) {

        String name = body.get("name");
        String path = body.get("path");
        String agentAddress = body.get("agentAddress");

        try {
            Map<String, Object> lsCommandPayload = new HashMap<>();
            lsCommandPayload.put("cmd", CommandEnum.RENAME.getCommand());
            lsCommandPayload.put("path", path);
            lsCommandPayload.put("name", name);

            Map<String, Object> response = commandRpcService.sendCommandAndReceiveResponse(
                    lsCommandPayload, agentAddress);

            if (StatusEnum.ERROR.name().equals(response.get("status").toString())) {
                return ResponseEntity.badRequest().body(response); // HTTP 400
            }

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            // Erros de input ou de JSON -> 400
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            // Erros inesperados -> 500
            System.err.println("Erro inesperado ao listar conteúdo do agente: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", "Um erro inesperado ocorreu"));
        }
    }
}
