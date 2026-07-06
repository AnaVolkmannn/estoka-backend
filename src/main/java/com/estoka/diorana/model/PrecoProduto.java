// src/main/java/com/estoka/diorana/model/PrecoProduto.java
package com.estoka.diorana.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(
    name = "precos_produto",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_produto", "mes_referencia", "ano_referencia"})
)
public class PrecoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    private Produto produto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal preco;

    @Column(precision = 5, scale = 2)
    private BigDecimal ipi;

    @Column(precision = 12, scale = 2)
    private BigDecimal frete;

    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia; // 1-12

    @Column(name = "ano_referencia", nullable = false)
    private Integer anoReferencia;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro = LocalDateTime.now();
}