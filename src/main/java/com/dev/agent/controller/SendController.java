package com.dev.agent.controller;

import com.dev.agent.entity.CommandHistory;
import com.dev.agent.enums.StatusEnum;
import com.dev.agent.services.CommandHistoryService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.BucketExistsArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Map;
import java.util.HashMap;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping("/send")
public class SendController {

    private final MinioClient minioClient;
    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;
    private final CommandHistoryService historyService;

    @GetMapping("/rpc")
    public ResponseEntity<?> rpc(@RequestParam(value = "path", required = false) String path,
                                 @RequestParam(value = "comand", required = false) String comand,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "url", required = false) String url,
                                 @RequestParam(value = "oldPath", required = false) String oldPath,
                                 @RequestParam(value = "content", required = false) String content,
                                 @RequestParam(value = "agentAddress", required = false) String agentAddress) throws Exception {
        if (comand == null || comand.isBlank()) comand = "ls";
        if (comand.equals("mkdir") && (name == null) || ( path == null)) return ResponseEntity.status(400).body(Map.of("error", "name param required"));
        if (comand.equals("rm") && path.isBlank()) return ResponseEntity.status(400).body(Map.of("error", "name param required"));
        if (comand.equals("down") && (url == null || url.isBlank())) return ResponseEntity.status(400).body(Map.of("error", "url param required"));

        if (comand.equals("mkdir")) {
            path = path + "/" + name;
        }
//        var msg = Map.of("cmd", comand, "path", path);
        Map<String, Object> msg = new HashMap<>();
        msg.put("cmd", comand);
        msg.put("path", path);
        if ("down".equals(comand)) msg.put("url", url);
        if ("copy".equals(comand)) msg.put("oldPath", oldPath);
        if ("rename".equals(comand)) msg.put("name", name);
        if ("save".equals(comand)) msg.put("content", content);

        System.out.println(msg);
        String body = objectMapper.writeValueAsString(msg);

        CommandHistory history = null;
        if(!comand.equals("ls")) history = historyService.create(agentAddress, "Teste", msg, body);

        // publicamos e aguardamos a RESPOSTA do agent (RPC)
        Object resp = rabbitTemplate.convertSendAndReceive(agentAddress, body);

        if (resp == null) {

            if(!comand.equals("ls")) historyService.updateStatus(history, StatusEnum.ERROR, "Erro interno", null);
            return ResponseEntity.status(504).body(Map.of(
                    "error", "timeout esperando resposta do agent",
                    "queue", agentAddress,
                    "message", msg
            ));
        }

        // converter resposta (pode vir como byte[] ou String)
        String json = (resp instanceof byte[])
                ? new String((byte[]) resp, java.nio.charset.StandardCharsets.UTF_8)
                : resp.toString();

        @SuppressWarnings("unchecked")
        Map<String, Object> parsed = objectMapper.readValue(json, Map.class);

        if(!comand.equals("ls")) historyService.updateStatus(history, StatusEnum.SUCCESS, "Ação finalizada", json);

        return ResponseEntity.ok(parsed);
    }

    @PostMapping("/rpc/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String bucketName = "agent";

            // Garante que o bucket existe
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();

            // Faz upload pro bucket
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // Gera URL temporária para download (expira em 1 hora)
            String url = minioClient.getPresignedObjectUrl(
                    io.minio.GetPresignedObjectUrlArgs.builder()
                            .method(io.minio.http.Method.GET)
                            .bucket(bucketName)
                            .object(fileName)
                            .expiry(3600) // segundos (1 hora)
                            .build()
            );

            return ResponseEntity.ok(url);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no upload: " + e.getMessage());
        }
    }
}