package com.estoka.diorana.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class EstoqueMensalResponseDTO {

    private Long id;
    private ProdutoResponseDTO produto;
    private BigDecimal quantidade;
    private YearMonth mesReferencia;
    
}