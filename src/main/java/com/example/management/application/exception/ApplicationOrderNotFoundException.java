package com.example.management.application.exception;

/**
 * Excepción de aplicación que representa cuando una orden no se encuentra.
 * Esta excepción actúa como una abstracción de aplicación, evitando que
 * las capas externas dependan directamente de las excepciones del dominio.
 */
public class ApplicationOrderNotFoundException extends RuntimeException {
    public ApplicationOrderNotFoundException(String message) {
        super(message);
    }
}
