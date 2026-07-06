// src/main/java/com/estoka/diorana/dto/InventarioResponseDTO.java
package com.estoka.diorana.dto;

import lombok.Data;
import java.util.List;

@Data
public class InventarioResponseDTO {
    private boolean fechado;
    private List<InventarioItemResponseDTO> itens;
}