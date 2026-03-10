package com.literalura.model;

import com.literalura.api.DatosLibro;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa un libro del catálogo.
 * Relación ManyToOne con Autor.
 */
@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 300)
    private String titulo;

    @Column(nullable = false, length = 10)
    private String idioma;

    @Column(name = "descargas")
    private Integer descargas;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    // ── Constructors ──────────────────────────────────────────────
    public Libro() {}

    /** Constructor desde Record de API Gutendex */
    public Libro(DatosLibro datos) {
        this.titulo   = datos.titulo();
        this.idioma   = (datos.idiomas() != null && !datos.idiomas().isEmpty())
                        ? datos.idiomas().get(0)
                        : "desconocido";
        this.descargas = datos.descargas() != null ? datos.descargas() : 0;

        if (datos.autores() != null && !datos.autores().isEmpty()) {
            this.autor = new Autor(datos.autores().get(0));
        }
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                     { return id; }
    public String getTitulo()               { return titulo; }
    public void setTitulo(String titulo)    { this.titulo = titulo; }
    public String getIdioma()               { return idioma; }
    public void setIdioma(String idioma)    { this.idioma = idioma; }
    public Integer getDescargas()           { return descargas; }
    public void setDescargas(Integer d)     { this.descargas = d; }
    public Autor getAutor()                 { return autor; }
    public void setAutor(Autor autor)       { this.autor = autor; }

    @Override
    public String toString() {
        return """
               ─────────────────────────────────
               Título    : %s
               Idioma    : %s
               Descargas : %d
               Autor     : %s
               ─────────────────────────────────""".formatted(
                titulo, idioma, descargas,
                autor != null ? autor.getNombre() : "Sin autor");
    }
}
