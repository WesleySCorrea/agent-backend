package com.dev.agent.dto.mercado.response;

import lombok.Getter;
import lombok.Setter;
import com.dev.agent.entity.Mercado;

@Getter
@Setter
public class MercadoBasicResponseDTO {

    private Long id;
    private String mercado;

    public MercadoBasicResponseDTO(Mercado mercado) {
        this.id = mercado.getId();
        this.mercado = mercado.getMercado();
    }
}
