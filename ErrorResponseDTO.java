package com.literalura.infra;

import java.time.LocalDateTime;

/** DTO estándar para respuestas de error de la API. */
public record ErrorResponseDTO(
        int status,
        String error,
        String mensaje,
        LocalDateTime timestamp
) {
    public static ErrorResponseDTO of(int status, String error, String mensaje) {
        return new ErrorResponseDTO(status, error, mensaje, LocalDateTime.now());
    }
}
