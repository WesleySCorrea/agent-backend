package com.dev.agent.dto.rede.request;

import com.dev.agent.entity.Rede;
import lombok.Getter;

@Getter
public class RedeRequestDTO {
    private String rede;
    private String cnpj;

    public Rede toEntity( ) {
        Rede rede = new Rede();
        rede.setRede(this.rede);
        rede.setCnpj(this.cnpj);
        return rede;
    }
}
