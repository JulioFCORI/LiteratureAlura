package com.literalura.repository;

import com.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Libro.
 *
 * Combina Derived Queries y @Query JPQL para cubrir
 * todos los casos de uso de ambas implementaciones.
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // ── Verificación de duplicados ────────────────────────────────

    /** Verifica si existe un libro con ese título (case-insensitive). */
    boolean existsByTituloIgnoreCase(String titulo);

    /** Busca un libro por título exacto (case-insensitive). */
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    // ── Filtro por idioma ─────────────────────────────────────────

    /** Derived Query: filtra por idioma, ordenado por título. */
    List<Libro> findByIdiomaIgnoreCaseOrderByTituloAsc(String idioma);

    /**
     * @Query JPQL explícito — equivalente a la Derived Query anterior.
     * Útil como demostración de JPQL personalizado.
     */
    @Query("""
           SELECT l FROM Libro l
           WHERE l.idioma = :idioma
           ORDER BY l.titulo ASC
           """)
    List<Libro> findLibrosPorIdioma(@Param("idioma") String idioma);

    // ── Todos los libros con JOIN FETCH ───────────────────────────

    /**
     * Trae todos los libros con su autor en una sola query (evita N+1).
     */
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor ORDER BY l.titulo ASC")
    List<Libro> findAllConAutor();

    // ── Libros por nombre de autor ────────────────────────────────

    @Query("""
           SELECT l FROM Libro l
           LEFT JOIN FETCH l.autor a
           WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
           ORDER BY l.titulo ASC
           """)
    List<Libro> findByAutorNombreContaining(@Param("nombre") String nombre);

    // ── Libros de un autor por ID ─────────────────────────────────

    @Query("""
           SELECT l FROM Libro l
           JOIN FETCH l.autor a
           WHERE a.id = :autorId
           ORDER BY l.titulo ASC
           """)
    List<Libro> findByAutorId(@Param("autorId") Long autorId);
}
