package com.literalura.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** DTO de entrada para crear o actualizar un Autor. */
public record AutorRequestDTO(

        @NotBlank(message = "El nombre del autor no puede estar vacío")
        @Size(max = 200, message = "El nombre no puede superar los 200 caracteres")
        String nombre,

        LocalDate fechaNacimiento,
        LocalDate fechaFallecimiento
) {}
