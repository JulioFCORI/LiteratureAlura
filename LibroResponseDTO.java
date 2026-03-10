package com.literalura.dto;

import com.literalura.model.Libro;

/** DTO de salida para representar un Libro. */
public record LibroResponseDTO(
        Long id,
        String titulo,
        String idioma,
        Integer descargas,
        AutorResponseDTO autor
) {
    /** Factory method: convierte entidad → DTO */
    public static LibroResponseDTO fromEntity(Libro l) {
        return new LibroResponseDTO(
                l.getId(),
                l.getTitulo(),
                l.getIdioma(),
                l.getDescargas(),
                l.getAutor() != null ? AutorResponseDTO.fromEntity(l.getAutor()) : null
        );
    }
}
