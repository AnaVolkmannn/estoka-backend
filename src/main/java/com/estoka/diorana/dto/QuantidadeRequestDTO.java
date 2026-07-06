// src/main/java/com/estoka/diorana/dto/QuantidadeRequestDTO.java
package com.estoka.diorana.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class QuantidadeRequestDTO {
    @NotNull
    private Integer mes;

    @NotNull
    private Integer ano;

    @NotNull
    private Long produtoId;

    private BigDecimal quantidade; // pode ser null (limpar o campo)
}