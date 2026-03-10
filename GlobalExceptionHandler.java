package com.literalura.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Manejador global de excepciones — centraliza y estandariza todos los errores HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Libro con título duplicado → 400 Bad Request */
    @ExceptionHandler(LibroDuplicadoException.class)
    public ResponseEntity<ErrorResponseDTO> handleLibroDuplicado(LibroDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDTO.of(400, "Libro Duplicado", ex.getMessage()));
    }

    /** Recurso no encontrado → 404 Not Found */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDTO.of(404, "Recurso No Encontrado", ex.getMessage()));
    }

    /** Errores de validación de @Valid → 422 Unprocessable Entity */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidacion(MethodArgumentNotValidException ex) {
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ErrorResponseDTO.of(422, "Error de Validación", errores));
    }

    /** Cualquier otro error → 500 Internal Server Error */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDTO.of(500, "Error Interno",
                        "Ocurrió un error inesperado: " + ex.getMessage()));
    }
}
