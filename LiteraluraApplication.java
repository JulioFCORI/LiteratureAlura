package com.literalura;

import com.literalura.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Punto de entrada de LiterAlura.
 *
 * ─────────────────────────────────────────────────────────────────
 *  MODO API REST (por defecto):
 *    Arrancar con: mvn spring-boot:run
 *    Acceder en:   http://localhost:8080/api/libros
 *
 *  MODO CONSOLA INTERACTIVO:
 *    Descomentar el @Bean commandLineRunner de abajo y relanzar.
 * ─────────────────────────────────────────────────────────────────
 */
@SpringBootApplication
public class LiteraluraApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    /**
     * Descomenta para activar el menú de consola al iniciar la app.
     */
    // @Bean
    // CommandLineRunner commandLineRunner(Principal principal) {
    //     return args -> principal.muestraElMenu();
    // }
}
