package com.dev.agent.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "rede")
public class Rede {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "rede", unique = true, nullable = false)
    private String rede;
    @Column(name = "cnpj", unique = true, nullable = false)
    private String cnpj;
    @OneToMany(mappedBy = "rede", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mercado> mercados = new ArrayList<Mercado>();
}
