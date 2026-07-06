package com.estoka.diorana.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FornecedorRequestDTO {

    @NotBlank
    private String nome;

    private String cnpj; // opcional
}