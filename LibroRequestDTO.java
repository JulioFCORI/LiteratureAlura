package com.literalura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** DTO de entrada para crear o actualizar un Libro manualmente. */
public record LibroRequestDTO(

        @NotBlank(message = "El título no puede estar vacío")
        @Size(max = 300, message = "El título no puede superar los 300 caracteres")
        String titulo,

        @NotBlank(message = "El idioma es obligatorio")
        @Pattern(regexp = "^(es|en|fr|de|pt|it)$",
                 message = "Idioma no válido. Valores aceptados: es, en, fr, de, pt, it")
        String idioma,

        Integer numeroDescargas,

        @NotNull(message = "El ID del autor es obligatorio")
        Long autorId
) {}
