package com.dev.agent.entity;

import lombok.Setter;
import lombok.Getter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import com.dev.agent.enums.StatusEnum;
import com.dev.agent.enums.CommandEnum;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "command_history")
public class CommandHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "usuario", nullable = false)
    private String usuario;
    @Column(name = "correlation_id", nullable = false)
    private String correlationId;
    @Column(name = "agent_address", nullable = false)
    private String agentAddress;
    @Enumerated(EnumType.STRING)
    @Column(name = "command", nullable = false)
    private CommandEnum command;
    @Column(name = "path")
    private String path;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;
    @Column(name = "msg")
    private String msg;
    @Lob
    private String requestPayload;
    @Lob
    private String responsePayload;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
}
