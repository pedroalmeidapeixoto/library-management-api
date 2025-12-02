package com.biblioteca.dto.devolucao;

public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;

    // Construtores, getters e setters
    public ErrorResponse(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters e setters...
}