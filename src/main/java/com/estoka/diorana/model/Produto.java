package com.estoka.diorana.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_fornecedor", nullable = false)
    private Fornecedor fornecedor;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String unidadeMedida;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean temIpi = false;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean temFrete = false;

    @Column(nullable = false)
    private boolean ativo = true;
}