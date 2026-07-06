package com.estoka.diorana.dto;

import lombok.Data;

@Data
public class ProdutoResponseDTO {

    private Integer id;
    private String nome;
    private FornecedorResponseDTO fornecedor;
    private String unidadeMedida;
    private boolean temIpi;
    private boolean temFrete;
}