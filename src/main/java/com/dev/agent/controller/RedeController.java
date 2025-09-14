package com.dev.agent.controller;

import com.dev.agent.dto.rede.RedeRequestDTO;
import lombok.RequiredArgsConstructor;
import com.dev.agent.services.RedeService;
import org.springframework.data.domain.Page;
import com.dev.agent.dto.rede.RedeResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rede")
public class RedeController {

    private final RedeService redeService;
    @GetMapping
    public ResponseEntity<Page<RedeResponseDTO>> findAll(Pageable pageable) {

        Page<RedeResponseDTO> dtoPage = redeService.findAll(pageable);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{rede}")
    public ResponseEntity<Page<RedeResponseDTO>> findByRede(@PathVariable("rede") String rede, Pageable pageable  ) {

        Page<RedeResponseDTO> dtoPage = redeService.findByRede(pageable, rede);

        return ResponseEntity.ok(dtoPage);
    }

    @PostMapping
    public ResponseEntity<RedeResponseDTO> createRede(@RequestBody RedeRequestDTO request) {

        RedeResponseDTO created = redeService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }
}
