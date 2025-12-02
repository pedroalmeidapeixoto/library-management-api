package com.biblioteca.dto.usuario;

import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UsuarioDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "Tipo é obrigatório")
    private TipoUsuario tipo;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @Pattern(regexp = "^$|^\\+?[0-9\\s\\-\\(\\)]{10,20}$",
            message = "Telefone deve ser válido ou vazio")
    private String telefone;

    private StatusUsuario status = StatusUsuario.ATIVO;

    // Construtores
    public UsuarioDTO() {}

    public UsuarioDTO(String nome, TipoUsuario tipo, String email) {
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
    }

    public UsuarioDTO(String nome, TipoUsuario tipo, String email, String telefone) {
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.telefone = telefone;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoUsuario getTipo() { return tipo; }
    public void setTipo(TipoUsuario tipo) { this.tipo = tipo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public StatusUsuario getStatus() { return status; }
    public void setStatus(StatusUsuario status) { this.status = status; }
}