package com.backend.circuler.controller;

import com.backend.circuler.dto.collectionpoint.CollectionPointCreateDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointResponseDTO;
import com.backend.circuler.dto.collectionpoint.CollectionPointUpdateDTO;
import com.backend.circuler.service.CollectionPointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collection-points")
@Tag(name = "Pontos de Coleta", description = "Endpoints para gerenciamento dos pontos de coleta de livros.")
public class CollectionPointController {

    private final CollectionPointService service;

    public CollectionPointController(CollectionPointService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CollectionPointResponseDTO> create(@RequestBody CollectionPointCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CollectionPointResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionPointResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CollectionPointResponseDTO> update(@PathVariable Integer id,
                                                             @RequestBody CollectionPointUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
