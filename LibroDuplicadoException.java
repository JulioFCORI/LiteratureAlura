package com.literalura.infra;

/** Lanzada al intentar registrar un libro con título ya existente. */
public class LibroDuplicadoException extends RuntimeException {

    public LibroDuplicadoException(String titulo) {
        super("Ya existe un libro con el título: '" + titulo + "'");
    }
}
