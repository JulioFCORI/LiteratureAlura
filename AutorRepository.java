package com.literalura.repository;

import com.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Autor.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    /** Busca un autor por nombre exacto (case-insensitive). */
    Optional<Autor> findByNombreIgnoreCase(String nombre);

    /** Busca autores cuyo nombre contiene la cadena dada. */
    List<Autor> findByNombreContainingIgnoreCase(String nombre);

    /** Todos los autores ordenados por nombre. */
    List<Autor> findAllByOrderByNombreAsc();

    /**
     * Autores vivos en un año determinado (JPQL).
     *
     * Condición:
     *   - Nació antes o durante ese año  (anyoNacimiento <= anyo)
     *   - No había fallecido aún         (anyoFallecimiento IS NULL  OR  > anyo)
     */
    @Query("""
           SELECT a FROM Autor a
           WHERE a.anyoNacimiento <= :anyo
             AND (a.anyoFallecimiento IS NULL OR a.anyoFallecimiento > :anyo)
           ORDER BY a.nombre ASC
           """)
    List<Autor> findAutoresVivosEnAnyo(@Param("anyo") int anyo);

    /**
     * Versión con fechas completas (LocalDate) para autores creados manualmente.
     *
     * Un autor estaba vivo en año X si:
     *   - YEAR(fechaNacimiento) <= anyo
     *   - Y (fechaFallecimiento IS NULL  OR  YEAR(fechaFallecimiento) > anyo)
     */
    @Query("""
           SELECT a FROM Autor a
           WHERE YEAR(a.fechaNacimiento) <= :anyo
             AND (a.fechaFallecimiento IS NULL OR YEAR(a.fechaFallecimiento) > :anyo)
           ORDER BY a.nombre ASC
           """)
    List<Autor> findAutoresVivosEnAnyoPorFecha(@Param("anyo") int anyo);
}
