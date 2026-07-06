// src/main/java/com/estoka/diorana/dto/FecharMesRequestDTO.java
package com.estoka.diorana.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FecharMesRequestDTO {
    @NotNull
    private Integer mes;

    @NotNull
    private Integer ano;
}