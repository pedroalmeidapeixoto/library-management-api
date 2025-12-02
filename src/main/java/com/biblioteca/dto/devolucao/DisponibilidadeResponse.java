package com.biblioteca.dto.devolucao;

public class DisponibilidadeResponse {
    private Integer idExemplar;
    private Boolean disponivel;
    private String mensagem;

    // Construtores, getters e setters
    public DisponibilidadeResponse() {}

    public DisponibilidadeResponse(Integer idExemplar, Boolean disponivel, String mensagem) {
        this.idExemplar = idExemplar;
        this.disponivel = disponivel;
        this.mensagem = mensagem;
    }

    public Integer getIdExemplar() { return idExemplar; }
    public void setIdExemplar(Integer idExemplar) { this.idExemplar = idExemplar; }

    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}