package com.biblioteca.model;

import com.biblioteca.model.enums.StatusUsuario;
import com.biblioteca.model.enums.TipoUsuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoUsuario tipo;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatusUsuario status;

    // Construtores
    public Usuario() {
        this.dataCadastro = LocalDateTime.now();
        this.status = StatusUsuario.ATIVO;
    }

    public Usuario(String nome, TipoUsuario tipo, String email) {
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.dataCadastro = LocalDateTime.now();
        this.status = StatusUsuario.ATIVO;
    }

    public Usuario(String nome, TipoUsuario tipo, String email, String telefone) {
        this.nome = nome;
        this.tipo = tipo;
        this.email = email;
        this.telefone = telefone;
        this.dataCadastro = LocalDateTime.now();
        this.status = StatusUsuario.ATIVO;
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