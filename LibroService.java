package com.literalura.service;

import com.literalura.api.ConsumoAPI;
import com.literalura.api.ConvierteDatos;
import com.literalura.api.DatosLibro;
import com.literalura.api.DatosRespuestaGutendex;
import com.literalura.dto.LibroResponseDTO;
import com.literalura.infra.LibroDuplicadoException;
import com.literalura.infra.RecursoNoEncontradoException;
import com.literalura.model.Autor;
import com.literalura.model.Libro;
import com.literalura.repository.AutorRepository;
import com.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Servicio principal de LiterAlura.
 *
 * Flujo de búsqueda:
 *   usuario busca título → Gutendex API → validar duplicado → persistir DB → DTO
 *
 * También cubre CRUD de Libros y todas las consultas de filtrado.
 */
@Service
public class LibroService {

    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos convierteDatos;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    @Value("${gutendex.api.url}")
    private String gutendexUrl;

    public LibroService(ConsumoAPI consumoAPI,
                        ConvierteDatos convierteDatos,
                        LibroRepository libroRepository,
                        AutorRepository autorRepository) {
        this.consumoAPI     = consumoAPI;
        this.convierteDatos = convierteDatos;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    // ─────────────────────────────────────────────────────────────
    //  BÚSQUEDA EN GUTENDEX + PERSISTENCIA
    // ─────────────────────────────────────────────────────────────

    /**
     * Busca un libro por título en Gutendex y lo persiste en la DB.
     *
     * Regla de negocio: lanza LibroDuplicadoException si el título ya existe.
     */
    @Transactional
    public LibroResponseDTO buscarYGuardarLibro(String titulo) {

        // 1. Verificar duplicado ANTES de llamar a la API
        if (libroRepository.existsByTituloIgnoreCase(titulo)) {
            throw new LibroDuplicadoException(titulo);
        }

        // 2. Consultar Gutendex
        String url = gutendexUrl + "?search="
                + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String json = consumoAPI.obtenerDatos(url);

        // 3. Deserializar JSON → Record
        DatosRespuestaGutendex respuesta =
                convierteDatos.obtenerDatos(json, DatosRespuestaGutendex.class);

        if (respuesta.resultados() == null || respuesta.resultados().isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No se encontró ningún libro con el título: \"" + titulo + "\"");
        }

        // 4. Tomar primer resultado y verificar duplicado por título real
        DatosLibro datosLibro = respuesta.resultados().get(0);
        if (libroRepository.existsByTituloIgnoreCase(datosLibro.titulo())) {
            throw new LibroDuplicadoException(datosLibro.titulo());
        }

        // 5. Construir entidad Libro
        Libro libro = new Libro(datosLibro);

        // 6. Reutilizar Autor si ya existe (evitar duplicar autores)
        if (libro.getAutor() != null) {
            Optional<Autor> existente =
                    autorRepository.findByNombreIgnoreCase(libro.getAutor().getNombre());
            existente.ifPresent(libro::setAutor);
        }

        // 7. Persistir y retornar DTO
        return LibroResponseDTO.fromEntity(libroRepository.save(libro));
    }

    // ─────────────────────────────────────────────────────────────
    //  CRUD LIBROS
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> listarTodos() {
        return libroRepository.findAllConAutor()
                .stream().map(LibroResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public LibroResponseDTO buscarPorId(Long id) {
        return libroRepository.findById(id)
                .map(LibroResponseDTO::fromEntity)
                .orElseThrow(() -> new RecursoNoEncontradoException("Libro", id));
    }

    @Transactional
    public void eliminarLibro(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Libro", id);
        }
        libroRepository.deleteById(id);
    }

    // ─────────────────────────────────────────────────────────────
    //  CONSULTAS DE FILTRADO
    // ─────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> listarPorIdioma(String idioma) {
        List<Libro> resultado = libroRepository.findByIdiomaIgnoreCaseOrderByTituloAsc(idioma);
        if (resultado.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No se encontraron libros en el idioma: " + idioma);
        }
        return resultado.stream().map(LibroResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> listarPorAutorId(Long autorId) {
        if (!autorRepository.existsById(autorId)) {
            throw new RecursoNoEncontradoException("Autor", autorId);
        }
        return libroRepository.findByAutorId(autorId)
                .stream().map(LibroResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> listarPorNombreAutor(String nombre) {
        return libroRepository.findByAutorNombreContaining(nombre)
                .stream().map(LibroResponseDTO::fromEntity).toList();
    }

    // Acceso directo a entidad para la clase Principal (consola)
    @Transactional(readOnly = true)
    public Libro buscarEntidadPorTitulo(String titulo) {
        return libroRepository.findByTituloIgnoreCase(titulo)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "Libro con título '" + titulo + "' no encontrado."));
    }
}
