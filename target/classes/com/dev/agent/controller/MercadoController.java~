package com.dev.agent.controller;

import com.dev.agent.dto.mercado.request.MercadoRequestDTO;
import com.dev.agent.services.MercadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import com.dev.agent.dto.mercado.response.MercadoResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mercado")
public class MercadoController {

    private final MercadoService mercadoService;
    @PostMapping
    public ResponseEntity<MercadoResponseDTO> createMercado(@RequestBody MercadoRequestDTO request) {

        MercadoResponseDTO created = mercadoService.create(request);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created.getId())
                    .toUri();

            return ResponseEntity.created(location).body(created);
    }
}
