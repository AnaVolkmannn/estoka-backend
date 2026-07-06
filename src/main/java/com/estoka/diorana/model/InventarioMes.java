// src/main/java/com/estoka/diorana/model/InventarioMes.java
package com.estoka.diorana.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(
    name = "inventario_mes",
    uniqueConstraints = @UniqueConstraint(columnNames = {"mes_referencia", "ano_referencia"})
)
public class InventarioMes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia;

    @Column(name = "ano_referencia", nullable = false)
    private Integer anoReferencia;

    @Column(nullable = false)
    private boolean fechado = false;

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;
}