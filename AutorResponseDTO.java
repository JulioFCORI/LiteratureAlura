package com.literalura.dto;

import com.literalura.model.Autor;
import java.time.LocalDate;

/** DTO de salida para representar un Autor. */
public record AutorResponseDTO(
        Long id,
        String nombre,
        LocalDate fechaNacimiento,
        LocalDate fechaFallecimiento
) {
    /** Factory method: convierte entidad → DTO */
    public static AutorResponseDTO fromEntity(Autor a) {
        return new AutorResponseDTO(
                a.getId(),
                a.getNombre(),
                a.getFechaNacimiento(),
                a.getFechaFallecimiento()
        );
    }
}
