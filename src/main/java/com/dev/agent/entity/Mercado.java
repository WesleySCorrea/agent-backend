package com.dev.agent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "mercado")
public class Mercado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "mercado", unique = true, nullable = false)
    private String mercado;
    @Column(name = "cnpj", unique = true, nullable = false)
    private String cnpj;
    @ManyToOne
    @JoinColumn(name = "rede_id", nullable = false)
    private Rede rede;
    @OneToMany(mappedBy = "mercado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pdv> pdvs = new ArrayList<>();
}
