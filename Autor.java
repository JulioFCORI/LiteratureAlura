package com.literalura.model;

import com.literalura.api.DatosAutor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA que representa a un autor.
 * Combina los campos de ambas implementaciones:
 *   - anyoNacimiento / anyoFallecimiento (int) para mapeo directo de Gutendex
 *   - fechaNacimiento / fechaFallecimiento (LocalDate) para CRUD manual
 */
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    /** Año de nacimiento proveniente de Gutendex (ej: 1797) */
    @Column(name = "anyo_nacimiento")
    private Integer anyoNacimiento;

    /** Año de fallecimiento proveniente de Gutendex */
    @Column(name = "anyo_fallecimiento")
    private Integer anyoFallecimiento;

    /** Fecha completa para CRUD manual (nullable) */
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    /** Fecha completa para CRUD manual (nullable) */
    @Column(name = "fecha_fallecimiento")
    private LocalDate fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    // ── Constructors ──────────────────────────────────────────────
    public Autor() {}

    /** Constructor desde Record de API Gutendex */
    public Autor(DatosAutor datos) {
        this.nombre            = datos.nombre();
        this.anyoNacimiento    = datos.anyoNacimiento();
        this.anyoFallecimiento = datos.anyoFallecimiento();
    }

    /** Constructor para creación manual vía DTO */
    public Autor(String nombre, LocalDate fechaNacimiento, LocalDate fechaFallecimiento) {
        this.nombre             = nombre;
        this.fechaNacimiento    = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
        this.anyoNacimiento     = fechaNacimiento    != null ? fechaNacimiento.getYear()    : null;
        this.anyoFallecimiento  = fechaFallecimiento != null ? fechaFallecimiento.getYear() : null;
    }

    // ── Getters & Setters ─────────────────────────────────────────
    public Long getId()                                        { return id; }
    public String getNombre()                                  { return nombre; }
    public void setNombre(String nombre)                       { this.nombre = nombre; }
    public Integer getAnyoNacimiento()                         { return anyoNacimiento; }
    public void setAnyoNacimiento(Integer a)                   { this.anyoNacimiento = a; }
    public Integer getAnyoFallecimiento()                      { return anyoFallecimiento; }
    public void setAnyoFallecimiento(Integer a)                { this.anyoFallecimiento = a; }
    public LocalDate getFechaNacimiento()                      { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate f)                { this.fechaNacimiento = f; }
    public LocalDate getFechaFallecimiento()                   { return fechaFallecimiento; }
    public void setFechaFallecimiento(LocalDate f)             { this.fechaFallecimiento = f; }
    public List<Libro> getLibros()                             { return libros; }
    public void setLibros(List<Libro> libros)                  { this.libros = libros; }

    @Override
    public String toString() {
        return "Autor{id=%d, nombre='%s', nacimiento=%s, fallecimiento=%s}".formatted(
                id, nombre,
                anyoNacimiento    != null ? anyoNacimiento.toString()    : "?",
                anyoFallecimiento != null ? anyoFallecimiento.toString() : "vivo/sin datos");
    }
}
