package com.literalura.principal;

import com.literalura.dto.AutorResponseDTO;
import com.literalura.dto.LibroResponseDTO;
import com.literalura.infra.LibroDuplicadoException;
import com.literalura.infra.RecursoNoEncontradoException;
import com.literalura.service.AutorService;
import com.literalura.service.LibroService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Modo consola interactivo de LiterAlura.
 *
 * Para activarlo, descomentar el @Bean en LiteraluraApplication.java:
 *
 *   @Bean
 *   CommandLineRunner run(Principal principal) {
 *       return args -> principal.muestraElMenu();
 *   }
 */
@Component
public class Principal {

    private final LibroService libroService;
    private final AutorService autorService;
    private final Scanner scanner = new Scanner(System.in);

    public Principal(LibroService libroService, AutorService autorService) {
        this.libroService = libroService;
        this.autorService = autorService;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("""
                    
                    ╔═══════════════════════════════════════════╗
                    ║          📚  L I T E R A L U R A          ║
                    ╠═══════════════════════════════════════════╣
                    ║  1 - Buscar libro por título (Gutendex)   ║
                    ║  2 - Listar todos los libros registrados  ║
                    ║  3 - Listar libros por idioma             ║
                    ║  4 - Listar todos los autores             ║
                    ║  5 - Autores vivos en un año determinado  ║
                    ║  6 - Buscar libros por nombre de autor    ║
                    ║  0 - Salir                                ║
                    ╚═══════════════════════════════════════════╝
                    """);

            System.out.print("  Opción: ");
            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  ⚠️  Ingresa un número válido.");
                continue;
            }

            switch (opcion) {
                case 1 -> buscarLibro();
                case 2 -> listarLibros();
                case 3 -> listarPorIdioma();
                case 4 -> listarAutores();
                case 5 -> listarAutoresVivos();
                case 6 -> listarPorAutor();
                case 0 -> System.out.println("\n  👋 ¡Hasta pronto!\n");
                default -> System.out.println("  ⚠️  Opción no reconocida.");
            }
        }
    }

    private void buscarLibro() {
        System.out.print("  🔍 Título a buscar: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isBlank()) { System.out.println("  ⚠️  El título no puede estar vacío."); return; }
        try {
            LibroResponseDTO libro = libroService.buscarYGuardarLibro(titulo);
            System.out.println("\n  ✅ ¡Libro guardado!\n");
            imprimirLibro(libro);
        } catch (LibroDuplicadoException | RecursoNoEncontradoException e) {
            System.out.println("  " + e.getMessage());
        }
    }

    private void listarLibros() {
        List<LibroResponseDTO> libros = libroService.listarTodos();
        if (libros.isEmpty()) { System.out.println("  📭 No hay libros registrados."); return; }
        System.out.println("\n  📚 Libros registrados (" + libros.size() + "):");
        libros.forEach(this::imprimirLibro);
    }

    private void listarPorIdioma() {
        System.out.print("  🌐 Idioma (es / en / fr / de / pt): ");
        String idioma = scanner.nextLine().trim().toLowerCase();
        try {
            List<LibroResponseDTO> libros = libroService.listarPorIdioma(idioma);
            System.out.println("\n  📗 Libros en '" + idioma + "' (" + libros.size() + "):");
            libros.forEach(this::imprimirLibro);
        } catch (RecursoNoEncontradoException e) {
            System.out.println("  " + e.getMessage());
        }
    }

    private void listarAutores() {
        List<AutorResponseDTO> autores = autorService.listarTodos();
        if (autores.isEmpty()) { System.out.println("  📭 No hay autores registrados."); return; }
        System.out.println("\n  ✍️  Autores (" + autores.size() + "):");
        autores.forEach(this::imprimirAutor);
    }

    private void listarAutoresVivos() {
        System.out.print("  📅 Año a consultar (ej: 1850): ");
        try {
            int anyo = Integer.parseInt(scanner.nextLine().trim());
            List<AutorResponseDTO> autores = autorService.autoresVivosEnAnyo(anyo);
            if (autores.isEmpty()) {
                System.out.println("  No se encontraron autores vivos en " + anyo);
                return;
            }
            System.out.println("\n  🧑‍💼 Autores vivos en " + anyo + " (" + autores.size() + "):");
            autores.forEach(this::imprimirAutor);
        } catch (NumberFormatException e) {
            System.out.println("  ⚠️  Ingresa un año válido (número entero).");
        }
    }

    private void listarPorAutor() {
        System.out.print("  ✍️  Nombre del autor (parcial): ");
        String nombre = scanner.nextLine().trim();
        List<LibroResponseDTO> libros = libroService.listarPorNombreAutor(nombre);
        if (libros.isEmpty()) { System.out.println("  No se encontraron libros para ese autor."); return; }
        System.out.println("\n  📚 Libros de autores con '" + nombre + "' (" + libros.size() + "):");
        libros.forEach(this::imprimirLibro);
    }

    private void imprimirLibro(LibroResponseDTO l) {
        System.out.printf("""
                  ─────────────────────────────────
                  Título    : %s
                  Idioma    : %s
                  Descargas : %d
                  Autor     : %s%n""",
                l.titulo(), l.idioma(), l.descargas(),
                l.autor() != null ? l.autor().nombre() : "Sin autor");
    }

    private void imprimirAutor(AutorResponseDTO a) {
        System.out.printf("  • %s  (nac: %s | fall: %s)%n",
                a.nombre(),
                a.fechaNacimiento()    != null ? a.fechaNacimiento().getYear()    : "?",
                a.fechaFallecimiento() != null ? a.fechaFallecimiento().getYear() : "vivo");
    }
}
