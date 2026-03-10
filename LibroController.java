package com.literalura.controller;

import com.literalura.dto.LibroResponseDTO;
import com.literalura.service.LibroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para Libros.
 *
 * ┌─────────────────────────────────────────────────────────────────┐
 * │  POST   /api/libros/buscar?titulo={titulo}  → busca y persiste  │
 * │  GET    /api/libros                         → lista todos        │
 * │  GET    /api/libros/{id}                    → buscar por ID      │
 * │  GET    /api/libros/idioma/{idioma}          → filtrar por idioma │
 * │  GET    /api/libros/autor/{autorId}          → libros del autor   │
 * │  GET    /api/libros/autor/nombre?q={nombre} → por nombre autor   │
 * │  DELETE /api/libros/{id}                    → eliminar           │
 * └─────────────────────────────────────────────────────────────────┘
 */
@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    /** Busca en Gutendex y persiste el primer resultado. */
    @PostMapping("/buscar")
    public ResponseEntity<LibroResponseDTO> buscarLibro(@RequestParam String titulo) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(libroService.buscarYGuardarLibro(titulo));
    }

    /** Lista todos los libros registrados. */
    @GetMapping
    public ResponseEntity<List<LibroResponseDTO>> listarTodos() {
        return ResponseEntity.ok(libroService.listarTodos());
    }

    /** Obtiene un libro por ID. */
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(libroService.buscarPorId(id));
    }

    /** Filtra libros por idioma (ej: es, en, fr). */
    @GetMapping("/idioma/{idioma}")
    public ResponseEntity<List<LibroResponseDTO>> listarPorIdioma(@PathVariable String idioma) {
        return ResponseEntity.ok(libroService.listarPorIdioma(idioma));
    }

    /** Lista todos los libros de un autor por su ID. */
    @GetMapping("/autor/{autorId}")
    public ResponseEntity<List<LibroResponseDTO>> listarPorAutor(@PathVariable Long autorId) {
        return ResponseEntity.ok(libroService.listarPorAutorId(autorId));
    }

    /** Busca libros cuyo autor contiene el nombre indicado. */
    @GetMapping("/autor/nombre")
    public ResponseEntity<List<LibroResponseDTO>> listarPorNombreAutor(@RequestParam String q) {
        return ResponseEntity.ok(libroService.listarPorNombreAutor(q));
    }

    /** Elimina un libro por ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}
