// src/main/java/com/estoka/diorana/dto/InventarioItemResponseDTO.java
package com.estoka.diorana.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InventarioItemResponseDTO {
    private Integer produtoId;
    private BigDecimal quantidade;
    private BigDecimal valor;
    private LocalDateTime valorAtualizadoEm;
    private BigDecimal ipi;
    private LocalDateTime ipiAtualizadoEm;
    private BigDecimal frete;
    private LocalDateTime freteAtualizadoEm;
}