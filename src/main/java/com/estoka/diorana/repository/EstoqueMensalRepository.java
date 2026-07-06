// src/main/java/com/estoka/diorana/repository/EstoqueMensalRepository.java
package com.estoka.diorana.repository;

import com.estoka.diorana.model.EstoqueMensal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstoqueMensalRepository extends JpaRepository<EstoqueMensal, Long> {

    // Usado no upsert: verifica se já existe lançamento de quantidade
    // pra esse produto naquele mês/ano específico.
    Optional<EstoqueMensal> findByProdutoIdAndMesReferenciaAndAnoReferencia(
            Long produtoId, Integer mesReferencia, Integer anoReferencia
    );

    // Usado pra montar a tela de inventário inteira: todos os lançamentos
    // de quantidade de um mês/ano, de uma vez (evita N+1 no service).
    @Query("""
        SELECT e FROM EstoqueMensal e
        WHERE e.mesReferencia = :mes AND e.anoReferencia = :ano
        """)
    List<EstoqueMensal> buscarPorMesEAno(@Param("mes") Integer mes, @Param("ano") Integer ano);
}