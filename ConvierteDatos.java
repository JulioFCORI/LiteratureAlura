package com.literalura.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

/**
 * Componente que convierte JSON (String) a objetos Java usando Jackson.
 * Diseñado como genérico para reutilizarse con cualquier tipo.
 */
@Component
public class ConvierteDatos {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convierte un JSON String al tipo Java indicado.
     *
     * @param json  cadena JSON de entrada
     * @param clase clase destino (ej: DatosRespuestaGutendex.class)
     * @param <T>   tipo genérico inferido en tiempo de compilación
     * @return instancia del tipo T con los datos del JSON
     */
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return mapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON a " + clase.getSimpleName() + ": " + e.getMessage(), e);
        }
    }
}
