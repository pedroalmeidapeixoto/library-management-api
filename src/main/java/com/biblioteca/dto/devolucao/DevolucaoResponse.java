package com.biblioteca.dto.devolucao;

public class DevolucaoResponse {
    private String message;
    private Integer emprestimoId;

    // Construtores, getters e setters
    public DevolucaoResponse() {}

    public DevolucaoResponse(String message, Integer emprestimoId) {
        this.message = message;
        this.emprestimoId = emprestimoId;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Integer getEmprestimoId() { return emprestimoId; }
    public void setEmprestimoId(Integer emprestimoId) { this.emprestimoId = emprestimoId; }
}