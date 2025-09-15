package com.dev.agent.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import com.dev.agent.enums.SistemaEnum;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "pdv")
public class Pdv {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pdv_name", nullable = false)
    private String pdvName;
    @Enumerated(EnumType.STRING)
    @Column(name = "sistema", nullable = false)
    private SistemaEnum sistema;
    @Column(name = "agent_adress", nullable = false, unique = true)
    private String agentAdress;
    @Column(name = "versao", nullable = false)
    private String versao;
    @ManyToOne
    @JoinColumn(name = "mercado_id")
    private Mercado mercado;
}
