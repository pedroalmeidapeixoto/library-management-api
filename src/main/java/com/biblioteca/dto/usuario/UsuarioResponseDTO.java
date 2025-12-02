package com.biblioteca.dto.usuario;

import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class UsuarioResponseDTO {

    private Long id;
    private String nome;
    private TipoUsuario tipo;
    private String email;
    private String telefone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCadastro;

    private StatusUsuario status;

    // Construtores
    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Long id, String nome, TipoUsuario tipo, String email,
                              String telefone, LocalDateTime dataCadastro, StatusUsuario status) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public StatusUsuario getStatus() { return status; }
    public void setStatus(StatusUsuario status) { this.status = status; }
}