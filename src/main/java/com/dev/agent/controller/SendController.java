package com.dev.agent.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/send")
public class SendController {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final MinioClient minioClient;

    @Value("${app.queue}")
    private String queueName;

    @Value("${app.defaultPath}")
    private String defaultPath;

    @GetMapping("/rpc")
    public ResponseEntity<?> rpc(@RequestParam(value = "path", required = false) String path,
                                 @RequestParam(value = "comand", required = false) String comand,
                                 @RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "url", required = false) String url,
                                 @RequestParam(value = "oldPath", required = false) String oldPath,
                                 @RequestParam(value = "content", required = false) String content) throws Exception {
        if (path == null || path.isBlank()) path = defaultPath;
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
        if (comand.equals("down")) {
            msg.put("url", url);
        }
        if (comand.equals("copy")) {
            msg.put("oldPath", oldPath);
        }
        if (comand.equals("rename")) {
            msg.put("name", name);
        }
        if (comand.equals("save")) {
            msg.put("content", content);
        }

        System.out.println(msg);
        String body = objectMapper.writeValueAsString(msg);

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