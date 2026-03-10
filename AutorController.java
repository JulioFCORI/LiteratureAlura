package com.literalura.controller;

import com.literalura.dto.AutorRequestDTO;
import com.literalura.dto.AutorResponseDTO;
import com.literalura.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Autores.
 *
 * ┌────────────────────────────────────────────────────────────────────┐
 * │  POST   /api/autores                    → crear autor              │
 * │  GET    /api/autores                    → listar todos             │
 * │  GET    /api/autores/{id}               → buscar por ID            │
 * │  PUT    /api/autores/{id}               → actualizar               │
 * │  DELETE /api/autores/{id}               → eliminar                 │
 * │  GET    /api/autores/vivos/{anyo}       → autores vivos en un año  │
 * └────────────────────────────────────────────────────────────────────┘
 */
@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> crear(@Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(autorService.crearAutor(dto));
    }

    @GetMapping
    public ResponseEntity<List<AutorResponseDTO>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AutorRequestDTO dto) {
        return ResponseEntity.ok(autorService.actualizarAutor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        autorService.eliminarAutor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vivos/{anyo}")
    public ResponseEntity<List<AutorResponseDTO>> vivosEnAnyo(@PathVariable int anyo) {
        return ResponseEntity.ok(autorService.autoresVivosEnAnyo(anyo));
    }
}
