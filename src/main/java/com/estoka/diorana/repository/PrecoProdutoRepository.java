// src/main/java/com/estoka/diorana/repository/PrecoProdutoRepository.java
package com.estoka.diorana.repository;

import com.estoka.diorana.model.PrecoProduto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PrecoProdutoRepository extends JpaRepository<PrecoProduto, Long> {

    // Usado no upsert: verifica se já existe registro de preço
    // pra esse produto EXATAMENTE naquele mês/ano (não é o "herdado", é o literal).
    Optional<PrecoProduto> findByProdutoIdAndMesReferenciaAndAnoReferencia(
            Long produtoId, Integer mesReferencia, Integer anoReferencia
    );

    // O coração da lógica de herança: pega o registro de preço mais recente
    // de um produto, considerando qualquer mês/ano ATÉ (e incluindo) o mês
    // consultado. Ordenado do mais recente pro mais antigo.
    @Query("""
        SELECT p FROM PrecoProduto p
        WHERE p.produto.id = :produtoId
          AND (p.anoReferencia < :ano
               OR (p.anoReferencia = :ano AND p.mesReferencia <= :mes))
        ORDER BY p.anoReferencia DESC, p.mesReferencia DESC
        """)
    List<PrecoProduto> buscarHistoricoAte(
            @Param("produtoId") Long produtoId,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano,
            Pageable pageable
    );

    // Mesma query acima, mas trazendo todo o histórico de uma vez pra TODOS
    // os produtos, pra evitar N+1 quando o service monta a tela inteira de
    // inventário (uma query só, depois agrupa em memória pelo produtoId).
    @Query("""
        SELECT p FROM PrecoProduto p
        WHERE p.anoReferencia < :ano
           OR (p.anoReferencia = :ano AND p.mesReferencia <= :mes)
        ORDER BY p.produto.id ASC, p.anoReferencia DESC, p.mesReferencia DESC
        """)
    List<PrecoProduto> buscarHistoricoDeTodosAte(
            @Param("mes") Integer mes,
            @Param("ano") Integer ano
    );
}