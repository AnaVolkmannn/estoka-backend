// src/main/java/com/estoka/diorana/repository/InventarioMesRepository.java
package com.estoka.diorana.repository;

import com.estoka.diorana.model.InventarioMes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventarioMesRepository extends JpaRepository<InventarioMes, Long> {
    Optional<InventarioMes> findByMesReferenciaAndAnoReferencia(Integer mes, Integer ano);
}