package com.estoka.diorana.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode; // <-- Adicione este import
import org.hibernate.type.SqlTypes;           // <-- Adicione este import

import java.math.BigDecimal;

@Data
@Entity
@Table(
    name = "estoque_mensal",
    uniqueConstraints = @UniqueConstraint(columnNames = {"id_produto", "mes_referencia", "ano_referencia"})
)
public class EstoqueMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_produto", nullable = false)
    @JdbcTypeCode(SqlTypes.INTEGER) // <-- ADICIONE ESTA LINHA AQUI!
    private Produto produto;

    @Column(name = "mes_referencia", nullable = false)
    private Integer mesReferencia; // 1-12

    @Column(name = "ano_referencia", nullable = false)
    private Integer anoReferencia;

    @Column(precision = 12, scale = 3)
    private BigDecimal quantidade;
}