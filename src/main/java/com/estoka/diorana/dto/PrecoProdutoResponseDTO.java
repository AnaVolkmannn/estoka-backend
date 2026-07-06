package com.estoka.diorana.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import lombok.Data;

@Data
public class PrecoProdutoResponseDTO {

    private Long id;
    private ProdutoResponseDTO produto;
    private BigDecimal preco;
    private BigDecimal ipi;
    private BigDecimal frete;
    private YearMonth mesReferencia;
    
}