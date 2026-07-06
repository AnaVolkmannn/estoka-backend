// src/main/java/com/estoka/diorana/dto/ValorRequestDTO.java
package com.estoka.diorana.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ValorRequestDTO {
    @NotNull
    private Integer mes;

    @NotNull
    private Integer ano;

    @NotNull
    private Long produtoId;

    private BigDecimal valor;
}