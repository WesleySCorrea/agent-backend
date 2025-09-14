package com.dev.agent.dto.mercado.response;

import lombok.Getter;
import lombok.Setter;
import com.dev.agent.entity.Pdv;
import com.dev.agent.entity.Mercado;
import com.dev.agent.dto.pdv.PdvResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MercadoResponseDTO {

    private Long id;
    private String mercado;
    private String cnpj;
    private String redeId;
    private List<PdvResponseDTO> pdvs;

    public MercadoResponseDTO(Mercado mercado) {
        this.id = mercado.getId();
        this.mercado = mercado.getMercado();
        this.cnpj = mercado.getCnpj();
        this.redeId = mercado.getRede().getId().toString();

        this.pdvs = new ArrayList<>();
        for (Pdv pdv : mercado.getPdvs()) {
            this.pdvs.add(new PdvResponseDTO(pdv));
        }
    }
}
