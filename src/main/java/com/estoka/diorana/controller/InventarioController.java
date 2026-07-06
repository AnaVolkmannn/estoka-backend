// src/main/java/com/estoka/diorana/controller/InventarioController.java
package com.estoka.diorana.controller;

import com.estoka.diorana.dto.*;
import com.estoka.diorana.service.InventarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventarios")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<InventarioResponseDTO> buscar(
            @RequestParam Integer mes,
            @RequestParam Integer ano) {
        return ResponseEntity.ok(inventarioService.buscarInventario(mes, ano));
    }

    @PutMapping("/item/quantidade")
    public ResponseEntity<Void> salvarQuantidade(@Valid @RequestBody QuantidadeRequestDTO dto) {
        inventarioService.salvarQuantidade(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/item/valor")
    public ResponseEntity<Void> salvarValor(@Valid @RequestBody ValorRequestDTO dto) {
        inventarioService.salvarValor(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/item/ipi")
    public ResponseEntity<Void> salvarIpi(@Valid @RequestBody IpiRequestDTO dto) {
        inventarioService.salvarIpi(dto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/item/frete")
    public ResponseEntity<Void> salvarFrete(@Valid @RequestBody FreteRequestDTO dto) {
        inventarioService.salvarFrete(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/fechar")
    public ResponseEntity<Void> fecharMes(@Valid @RequestBody FecharMesRequestDTO dto) {
        inventarioService.fecharMes(dto);
        return ResponseEntity.noContent().build();
    }
}