package com.dev.agent.dto.mercado.request;

import lombok.Getter;
import com.dev.agent.entity.Rede;
import com.dev.agent.entity.Mercado;

@Getter
public class MercadoRequestDTO {

    private String mercado;
    private String cnpj;
    private Long redeId;

    public Mercado toEntity() {
        Mercado mercado = new Mercado();
        mercado.setMercado(this.mercado);
        mercado.setCnpj(this.cnpj);

        Rede rede = new Rede();
        rede.setId(this.redeId);
        mercado.setRede(rede);

        return mercado;
    }
}
