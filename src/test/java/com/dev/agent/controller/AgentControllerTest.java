package com.dev.agent.controller;

import com.dev.agent.services.CommandRpcService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AgentController.class)
public class AgentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CommandRpcService commandRpcService;
    private static final String URL = "/api/agent";
    @Test
    void testListFilesSuccess() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "files", List.of("file1.txt", "file2.txt")
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.eq("127.0.0.1")))
                .thenReturn(response);

        mockMvc.perform(get(URL + "/list")
                        .param("agentAddress", "127.0.0.1")
                        .param("path", "/tmp"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.files[0]").value("file1.txt"));
    }

    @Test
    void testListFilesErrorStatus() throws Exception {
        Map<String, Object> response = Map.of("status", "ERROR", "message", "Falha");

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.eq("127.0.0.1")))
                .thenReturn(response);

        mockMvc.perform(get(URL + "/list")
                        .param("agentAddress", "127.0.0.1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha"));
    }

    @Test
    void testListFilesException_onIllegalArgumentException() throws Exception {
        String invalidAgentAddress = "";

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(Mockito.any(Map.class), Mockito.eq(invalidAgentAddress)))
                .thenThrow(new IllegalArgumentException("Agent address inválido"));

        mockMvc.perform(get(URL + "/list")
                        .param("agentAddress", invalidAgentAddress))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Agent address inválido"));
    }

    @Test
    void testListFilesException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.eq("127.0.0.1")))
                .thenThrow(new RuntimeException("Erro crítico"));

        mockMvc.perform(get(URL + "/list")
                        .param("agentAddress", "127.0.0.1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }

    @Test
    void openFile_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Arquivo aberto"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.eq("127.0.0.1")))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Arquivo aberto"));
    }

    @Test
    void openFile_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao abrir arquivo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao abrir arquivo"));
    }

    @Test
    void openFile_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("Input inválido"));

        mockMvc.perform(post(URL + "/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Input inválido"));
    }

    @Test
    void openFile_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/open")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }

    @Test
    void saveFile_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Arquivo salvo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "content": "conteúdo teste",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Arquivo salvo"));
    }

    @Test
    void saveFile_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao salvar arquivo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "content": "conteúdo teste",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao salvar arquivo"));
    }

    @Test
    void saveFile_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("Input inválido"));

        mockMvc.perform(post(URL + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "",
                                  "content": "",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Input inválido"));
    }

    @Test
    void saveFile_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "content": "conteúdo teste",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }

    @Test
    void createFolder_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Pasta criada"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp",
                                  "name": "nova_pasta",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Pasta criada"));
    }

    @Test
    void createFolder_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao criar pasta"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp",
                                  "name": "nova_pasta",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao criar pasta"));
    }

    @Test
    void createFolder_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("Input inválido"));

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "",
                                  "name": "",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Input inválido"));
    }

    @Test
    void createFolder_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp",
                                  "name": "nova_pasta",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }

    @Test
    void deleteFile_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Arquivo deletado"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Arquivo deletado"));
    }

    @Test
    void deleteFile_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao deletar arquivo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao deletar arquivo"));
    }

    @Test
    void deleteFile_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("Path inválido"));

        mockMvc.perform(post(URL + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Path inválido"));
    }

    @Test
    void deleteFile_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }

    @Test
    void downloadFile_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Arquivo baixado"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "url": "http://exemplo.com/file.txt",
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Arquivo baixado"));
    }

    @Test
    void downloadFile_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao baixar arquivo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "url": "http://exemplo.com/file.txt",
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao baixar arquivo"));
    }

    @Test
    void downloadFile_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("URL inválida"));

        mockMvc.perform(post(URL + "/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "url": "",
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("URL inválida"));
    }

    @Test
    void downloadFile_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "url": "http://exemplo.com/file.txt",
                                  "path": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }

    @Test
    void copyFile_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Arquivo copiado"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file_copy.txt",
                                  "oldPath": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Arquivo copiado"));
    }

    @Test
    void copyFile_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao copiar arquivo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file_copy.txt",
                                  "oldPath": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao copiar arquivo"));
    }

    @Test
    void copyFile_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("Caminho inválido"));

        mockMvc.perform(post(URL + "/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "",
                                  "oldPath": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Caminho inválido"));
    }

    @Test
    void copyFile_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/copy")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file_copy.txt",
                                  "oldPath": "/tmp/file.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }


    @Test
    void renameFile_success() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "OK",
                "message", "Arquivo renomeado"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "name": "file_renamed.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Arquivo renomeado"));
    }

    @Test
    void renameFile_statusError() throws Exception {
        Map<String, Object> response = Map.of(
                "status", "ERROR",
                "message", "Falha ao renomear arquivo"
        );

        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenReturn(response);

        mockMvc.perform(post(URL + "/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "name": "file_renamed.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Falha ao renomear arquivo"));
    }

    @Test
    void renameFile_illegalArgumentException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new IllegalArgumentException("Nome inválido"));

        mockMvc.perform(post(URL + "/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "name": "",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Nome inválido"));
    }

    @Test
    void renameFile_genericException() throws Exception {
        Mockito.when(commandRpcService.sendCommandAndReceiveResponse(
                        Mockito.anyMap(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Falha inesperada"));

        mockMvc.perform(post(URL + "/rename")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "path": "/tmp/file.txt",
                                  "name": "file_renamed.txt",
                                  "agentAddress": "127.0.0.1"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Um erro inesperado ocorreu"));
    }
}





