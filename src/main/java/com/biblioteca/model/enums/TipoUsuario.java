package com.biblioteca.model.enums;

public enum TipoUsuario {
    ALUNO("Aluno"),
    PROFESSOR("Professor"),
    FUNCIONARIO("Funcionário");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}