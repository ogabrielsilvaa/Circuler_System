package com.backend.circuler.controller;

import com.backend.circuler.dto.bookinstance.BookInstanceCreateDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceResponseDTO;
import com.backend.circuler.dto.bookinstance.BookInstanceUpdateDTO;
import com.backend.circuler.service.BookInstanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-instances")
@Tag(name = "Exemplares", description = "Endpoints para gerenciamento de exemplares de livros nos pontos de coleta.")
public class BookInstanceController {

    private final BookInstanceService service;

    public BookInstanceController(BookInstanceService service) {
        this.service = service;
    }

    @PostMapping("/point/{pointId}")
    public ResponseEntity<BookInstanceResponseDTO> createForPoint(
            @PathVariable Integer pointId,
            @RequestBody BookInstanceCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createForPoint(pointId, request));
    }

    @PostMapping("/my-point")
    public ResponseEntity<BookInstanceResponseDTO> createForMyPoint(
            @RequestBody BookInstanceCreateDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createForMyPoint(request));
    }

    @GetMapping
    public ResponseEntity<List<BookInstanceResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookInstanceResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookInstanceResponseDTO> update(
            @PathVariable Integer id,
            @RequestBody BookInstanceUpdateDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}