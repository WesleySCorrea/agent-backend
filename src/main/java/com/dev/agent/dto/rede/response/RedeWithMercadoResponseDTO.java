package com.dev.agent.dto.rede.response;

import com.dev.agent.entity.Mercado;
import com.dev.agent.entity.Rede;
import lombok.Getter;
import lombok.Setter;
import com.dev.agent.dto.mercado.response.MercadoBasicResponseDTO;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
public class RedeWithMercadoResponseDTO {

    private Long id;
    private String rede;
    private List<MercadoBasicResponseDTO> mercados;

    public RedeWithMercadoResponseDTO(Rede rede) {
        this.id = rede.getId();
        this.rede = rede.getRede();

        this.mercados = new ArrayList<>();
        for (Mercado mercado : rede.getMercados()) {
            MercadoBasicResponseDTO mDto = new MercadoBasicResponseDTO(mercado);

            this.mercados.add(mDto);
        }
    }
}
