package com.dev.agent.dto.pdv.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.dev.agent.entity.Pdv;

@Getter
@Setter
public class PdvResponseDTO {

    private Long id;
    private String pdvName;
    private String agentAdress;
    private String sistema;
    private String versao;
    private Long mercadoId;

    public PdvResponseDTO(Pdv pdv) {
        this.id = pdv.getId();
        this.pdvName = pdv.getPdvName();
        this.agentAdress = pdv.getAgentAdress();
        this.sistema = pdv.getSistema().toString();
        this.versao = pdv.getVersao();
        this.mercadoId = pdv.getMercado().getId();
    }
}
