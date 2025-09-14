package com.dev.agent.dto.rede.response;

import lombok.Getter;
import lombok.Setter;
import com.dev.agent.entity.Rede;
import com.dev.agent.entity.Mercado;
import com.dev.agent.dto.mercado.response.MercadoResponseDTO;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class RedeResponseDTO {

    private Long id;
    private String rede;
    private String cnpj;
    private List<MercadoResponseDTO> mercados;

    public RedeResponseDTO(Rede rede) {
        this.id = rede.getId();
        this.rede = rede.getRede();
        this.cnpj = rede.getCnpj();

        this.mercados = new ArrayList<>();
        for (Mercado mercado : rede.getMercados()) {
            MercadoResponseDTO mDto = new MercadoResponseDTO(mercado);

            this.mercados.add(mDto);
        }
    }
}
