package com.estoka.diorana.dto;

import java.math.BigDecimal;
import java.time.YearMonth;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrecoProdutoRequestDTO {

    @NotNull
    private Long produtoId;

    @NotNull
    private BigDecimal preco;

    private BigDecimal ipi;

    private BigDecimal frete;

    @NotNull
    private YearMonth mesReferencia;
    
}