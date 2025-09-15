package com.dev.agent.controller;

import lombok.RequiredArgsConstructor;
import com.dev.agent.services.PdvService;
import org.springframework.http.ResponseEntity;
import com.dev.agent.dto.pdv.request.PdvRequestDTO;
import com.dev.agent.dto.pdv.response.PdvResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pdv")
public class PdvController {

    private final PdvService pdvService;
    @PostMapping
    public ResponseEntity<PdvResponseDTO> createPdv(@RequestBody PdvRequestDTO request) {

        PdvResponseDTO created = pdvService.create(request);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(created.getId())
                    .toUri();

            return ResponseEntity.created(location).body(created);
    }
}
