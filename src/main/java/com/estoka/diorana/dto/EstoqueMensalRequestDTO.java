package com.estoka.diorana.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;

@Data
public class EstoqueMensalRequestDTO {

    @NotNull
    private Long produtoId;

    @NotNull
    private BigDecimal quantidade;

    @NotNull
    private YearMonth mesReferencia;
    
}