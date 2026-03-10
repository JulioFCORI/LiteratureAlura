package com.literalura.service;

import com.literalura.dto.AutorRequestDTO;
import com.literalura.dto.AutorResponseDTO;
import com.literalura.infra.RecursoNoEncontradoException;
import com.literalura.model.Autor;
import com.literalura.repository.AutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para operaciones CRUD de Autor y consultas especializadas.
 */
@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional
    public AutorResponseDTO crearAutor(AutorRequestDTO dto) {
        Autor autor = new Autor(dto.nombre(), dto.fechaNacimiento(), dto.fechaFallecimiento());
        return AutorResponseDTO.fromEntity(autorRepository.save(autor));
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> listarTodos() {
        return autorRepository.findAllByOrderByNombreAsc()
                .stream().map(AutorResponseDTO::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public AutorResponseDTO buscarPorId(Long id) {
        return autorRepository.findById(id)
                .map(AutorResponseDTO::fromEntity)
                .orElseThrow(() -> new RecursoNoEncontradoException("Autor", id));
    }

    @Transactional
    public AutorResponseDTO actualizarAutor(Long id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Autor", id));
        autor.setNombre(dto.nombre());
        autor.setFechaNacimiento(dto.fechaNacimiento());
        autor.setFechaFallecimiento(dto.fechaFallecimiento());
        if (dto.fechaNacimiento()    != null) autor.setAnyoNacimiento(dto.fechaNacimiento().getYear());
        if (dto.fechaFallecimiento() != null) autor.setAnyoFallecimiento(dto.fechaFallecimiento().getYear());
        return AutorResponseDTO.fromEntity(autorRepository.save(autor));
    }

    @Transactional
    public void eliminarAutor(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Autor", id);
        }
        autorRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<AutorResponseDTO> autoresVivosEnAnyo(int anyo) {
        List<Autor> porAnyo  = autorRepository.findAutoresVivosEnAnyo(anyo);
        List<Autor> porFecha = autorRepository.findAutoresVivosEnAnyoPorFecha(anyo);
        // Unificar ambos resultados sin duplicados
        return java.util.stream.Stream.concat(porAnyo.stream(), porFecha.stream())
                .distinct()
                .map(AutorResponseDTO::fromEntity)
                .sorted(java.util.Comparator.comparing(AutorResponseDTO::nombre))
                .toList();
    }
}
