package com.literalura.api;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Componente responsable de realizar peticiones HTTP a APIs externas.
 * Usa el HttpClient nativo de Java 11+ (sin dependencias externas).
 */
@Component
public class ConsumoAPI {

    private final HttpClient client;

    public ConsumoAPI() {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Realiza una petición GET a la URL indicada y devuelve el cuerpo como String.
     *
     * @param url URL completa a consultar
     * @return JSON de respuesta como String
     * @throws RuntimeException si falla la conexión o el servidor retorna error
     */
    public String obtenerDatos(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Error al consultar la API. Código HTTP: " + response.statusCode()
                );
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error de conexión con la API externa: " + e.getMessage(), e);
        }
    }
}
