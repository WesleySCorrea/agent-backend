package com.dev.agent.controller;

import io.minio.*;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(SendController.class)
public class SendControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MinioClient minioClient;
    @Test
    void testUploadFile() throws Exception {
        // Cria um arquivo mock
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "teste.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "conteudo do arquivo".getBytes()
        );

        // Mock do comportamento do MinioClient
        Mockito.when(minioClient.bucketExists(Mockito.any(BucketExistsArgs.class))).thenReturn(true);
        Mockito.when(minioClient.getPresignedObjectUrl(Mockito.any(GetPresignedObjectUrlArgs.class)))
                .thenReturn("http://minio/agent/teste.txt");

        // Executa a chamada
        mockMvc.perform(multipart("/api/agent/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("http://minio/agent/teste.txt"));

        // Verifica que o putObject foi chamado
        Mockito.verify(minioClient).putObject(Mockito.any(PutObjectArgs.class));
    }

    @Test
    void testUploadFileError() throws Exception {
        // Cria um arquivo mock
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "erro.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "conteudo do arquivo".getBytes()
        );

        // Simula que o bucket não existe
        Mockito.when(minioClient.bucketExists(Mockito.any(BucketExistsArgs.class))).thenReturn(false);

        // Simula que makeBucket lança exceção
        Mockito.doThrow(new RuntimeException("Falha ao criar bucket"))
                .when(minioClient).makeBucket(Mockito.any(MakeBucketArgs.class));

        // Executa a chamada
        mockMvc.perform(multipart("/api/agent/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Erro no upload: Falha ao criar bucket"));
    }
}