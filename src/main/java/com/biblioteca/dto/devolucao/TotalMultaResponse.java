package com.biblioteca.dto.devolucao;

public class TotalMultaResponse {
    private Double totalMultaPendente;

    // Construtores, getters e setters
    public TotalMultaResponse() {}

    public TotalMultaResponse(Double totalMultaPendente) {
        this.totalMultaPendente = totalMultaPendente;
    }

    public Double getTotalMultaPendente() { return totalMultaPendente; }
    public void setTotalMultaPendente(Double totalMultaPendente) {
        this.totalMultaPendente = totalMultaPendente;
    }
}