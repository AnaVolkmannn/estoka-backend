package com.estoka.diorana.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProdutoRequestDTO {

    @NotNull
    private Long fornecedorId;

    @NotBlank
    private String nome;

    @NotBlank
    private String unidadeMedida;

    private boolean temIpi;

    private boolean temFrete;

}