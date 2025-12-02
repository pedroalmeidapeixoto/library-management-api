package com.biblioteca.mapper;

import com.biblioteca.dto.livro.LivroDTO;
import com.biblioteca.dto.livro.LivroResponseDTO;
import com.biblioteca.model.Livro;
import org.springframework.stereotype.Component;

@Component
public class LivroMapper {

    public Livro toEntity(LivroDTO dto) {
        if (dto == null) return null;

        Livro livro = new Livro();
        livro.setTitulo(dto.getTitulo());
        livro.setAnoPublicacao(dto.getAnoPublicacao());
        livro.setEditora(dto.getEditora());
        livro.setGenero(dto.getGenero());

        return livro;
    }

    public LivroResponseDTO toResponseDTO(Livro livro) {
        if (livro == null) return null;

        LivroResponseDTO responseDTO = new LivroResponseDTO();
        responseDTO.setId(livro.getId());
        responseDTO.setTitulo(livro.getTitulo());
        responseDTO.setAnoPublicacao(livro.getAnoPublicacao());
        responseDTO.setEditora(livro.getEditora());
        responseDTO.setGenero(livro.getGenero());
        responseDTO.setDataCadastro(livro.getDataCadastro());

        return responseDTO;
    }

    public void updateEntityFromDTO(LivroDTO dto, Livro livro) {
        if (dto == null || livro == null) return;

        if (dto.getTitulo() != null) livro.setTitulo(dto.getTitulo());
        if (dto.getAnoPublicacao() != null) livro.setAnoPublicacao(dto.getAnoPublicacao());
        if (dto.getEditora() != null) livro.setEditora(dto.getEditora());
        if (dto.getGenero() != null) livro.setGenero(dto.getGenero());
    }
}