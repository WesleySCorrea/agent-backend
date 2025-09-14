package com.dev.agent.dto.rede.response;

import lombok.Getter;
import lombok.Setter;
import com.dev.agent.entity.Rede;

@Getter
@Setter
public class RedeBasicResponseDTO {

    private Long id;
    private String rede;

    public RedeBasicResponseDTO(Rede rede) {
        this.id = rede.getId();
        this.rede = rede.getRede();
    }
}
