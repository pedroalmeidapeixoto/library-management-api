package com.biblioteca.dto.exemplar;

import com.biblioteca.model.enums.StatusExemplar;
import lombok.Data;

@Data
public class ExemplarResponseDTO {

    private Long id;
    private Long idLivro;
    private StatusExemplar status;
}
