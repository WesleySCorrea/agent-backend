package com.dev.agent.service;

import org.mockito.Mockito;
import org.instancio.Select;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import com.dev.agent.enums.StatusEnum;
import com.dev.agent.enums.CommandEnum;
import com.dev.agent.entity.CommandHistory;
import com.dev.agent.repository.CommandHistoryRepository;
import com.dev.agent.services.impl.CommandHistoryServiceImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandHistoryServiceImplTest {

    private final CommandHistoryRepository commandHistoryRepository = Mockito.mock(CommandHistoryRepository.class);
    private final CommandHistoryServiceImpl historyService = new CommandHistoryServiceImpl(commandHistoryRepository);

    @Test
    void createCommandHistory() {
        Map<String, Object> payload = Map.of(
                "cmd", "LS",
                "path", "/tmp/file.txt"
        );

        CommandHistory saved = Instancio.of(CommandHistory.class)
                .set(Select.field("id"), 1L)
                .set(Select.field("command"), CommandEnum.OPEN)
                .set(Select.field("status"), StatusEnum.PENDING)
                .set(Select.field("agentAddress"), "127.0.0.1")
                .set(Select.field("usuario"), "usuario teste")
                .set(Select.field("path"), "/tmp/file.txt")
                .create();

        Mockito.when(commandHistoryRepository.save(Mockito.any(CommandHistory.class)))
                .thenReturn(saved);

        CommandHistory result = historyService.create("127.0.0.1", "usuario teste", payload, "{\"cmd\":\"OPEN\"}");

        assertEquals(1L, result.getId());
        assertEquals(CommandEnum.OPEN, result.getCommand());
        assertEquals(StatusEnum.PENDING, result.getStatus());
        assertEquals("127.0.0.1", result.getAgentAddress());
        assertEquals("usuario teste", result.getUsuario());
        assertEquals("/tmp/file.txt", result.getPath());

        Mockito.verify(commandHistoryRepository)
                .save(Mockito.argThat(h -> h.getCommand() == CommandEnum.LS && h.getStatus() == StatusEnum.PENDING));
    }

    @Test
    void updateStatusCommandHistory() {
        Map<String, Object> payload = Map.of(
                "cmd", "LS",
                "path", "/tmp/file.txt"
        );

        CommandHistory existing = Instancio.of(CommandHistory.class)
                .set(Select.field("id"), 1L)
                .set(Select.field("status"), StatusEnum.PENDING)
                .create();

        CommandHistory updated = Instancio.of(CommandHistory.class)
                .set(Select.field("id"), 1L)
                .set(Select.field("status"), StatusEnum.SUCCESS)
                .create();

        Mockito.when(commandHistoryRepository.save(Mockito.any(CommandHistory.class)))
                .thenReturn(updated);

        CommandHistory result = historyService.updateStatus(existing, StatusEnum.SUCCESS, "OK", payload.toString());

        assertEquals(StatusEnum.SUCCESS, result.getStatus());

        Mockito.verify(commandHistoryRepository)
                .save(Mockito.argThat(h -> h.getStatus() == StatusEnum.SUCCESS));
    }

    @Test
    void saveCommandHistory() {
        CommandHistory ch = Instancio.create(CommandHistory.class);

        Mockito.when(commandHistoryRepository.save(Mockito.any(CommandHistory.class)))
                .thenReturn(ch);

        CommandHistory result = historyService.save(ch);

        assertEquals(ch, result);

        Mockito.verify(commandHistoryRepository).save(ch);
    }
}
