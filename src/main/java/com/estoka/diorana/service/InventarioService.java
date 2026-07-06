// src/main/java/com/estoka/diorana/service/InventarioService.java
package com.estoka.diorana.service;

import com.estoka.diorana.dto.*;
import com.estoka.diorana.exception.MesFechadoException;
import com.estoka.diorana.model.*;
import com.estoka.diorana.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueMensalRepository estoqueMensalRepository;
    private final PrecoProdutoRepository precoProdutoRepository;
    private final InventarioMesRepository inventarioMesRepository;

    // ─── Consulta principal da tela ────────────────────────────────────────

    public InventarioResponseDTO buscarInventario(Integer mes, Integer ano) {
        List<Produto> produtosAtivos = produtoRepository.findByAtivoTrue();

        boolean fechado = inventarioMesRepository
                .findByMesReferenciaAndAnoReferencia(mes, ano)
                .map(InventarioMes::isFechado)
                .orElse(false);

        // Quantidades lançadas NESTE mês específico (não herda de outros meses)
        Map<Integer, EstoqueMensal> estoquePorProduto = estoqueMensalRepository
                .buscarPorMesEAno(mes, ano)
                .stream()
                .collect(Collectors.toMap(e -> e.getProduto().getId(), e -> e));

        // Preço/IPI/Frete: pega o registro mais recente de cada produto até este mês (herança)
        Map<Integer, PrecoProduto> precoPorProduto = precoProdutoRepository
                .buscarHistoricoDeTodosAte(mes, ano)
                .stream()
                .collect(Collectors.toMap(
                        p -> p.getProduto().getId(),
                        p -> p,
                        (existente, novo) -> existente // já vem ordenado do mais recente pro mais antigo
                ));

        List<InventarioItemResponseDTO> itens = produtosAtivos.stream()
                .map(produto -> montarItem(produto, estoquePorProduto.get(produto.getId()), precoPorProduto.get(produto.getId())))
                .toList();

        InventarioResponseDTO response = new InventarioResponseDTO();
        response.setFechado(fechado);
        response.setItens(itens);
        return response;
    }

    private InventarioItemResponseDTO montarItem(Produto produto, EstoqueMensal estoque, PrecoProduto preco) {
        InventarioItemResponseDTO dto = new InventarioItemResponseDTO();
        dto.setProdutoId(produto.getId());
        dto.setQuantidade(estoque != null ? estoque.getQuantidade() : null);

        if (preco != null) {
            dto.setValor(preco.getPreco());
            dto.setValorAtualizadoEm(preco.getDataRegistro());
            dto.setIpi(preco.getIpi());
            dto.setIpiAtualizadoEm(preco.getIpi() != null ? preco.getDataRegistro() : null);
            dto.setFrete(preco.getFrete());
            dto.setFreteAtualizadoEm(preco.getFrete() != null ? preco.getDataRegistro() : null);
        }

        return dto;
    }

    // ─── Upserts ────────────────────────────────────────────────────────────

    @Transactional
    public void salvarQuantidade(QuantidadeRequestDTO dto) {
        validarMesAberto(dto.getMes(), dto.getAno());

        EstoqueMensal estoque = estoqueMensalRepository
                .findByProdutoIdAndMesReferenciaAndAnoReferencia(dto.getProdutoId(), dto.getMes(), dto.getAno())
                .orElseGet(() -> {
                    EstoqueMensal novo = new EstoqueMensal();
                    novo.setProduto(buscarProduto(dto.getProdutoId()));
                    novo.setMesReferencia(dto.getMes());
                    novo.setAnoReferencia(dto.getAno());
                    return novo;
                });

        estoque.setQuantidade(dto.getQuantidade());
        estoqueMensalRepository.save(estoque);
    }

    @Transactional
    public void salvarValor(ValorRequestDTO dto) {
        validarMesAberto(dto.getMes(), dto.getAno());
        PrecoProduto preco = buscarOuCriarPrecoDoMes(dto.getProdutoId(), dto.getMes(), dto.getAno());
        preco.setPreco(dto.getValor());
        preco.setDataRegistro(LocalDateTime.now());
        precoProdutoRepository.save(preco);
    }

    @Transactional
    public void salvarIpi(IpiRequestDTO dto) {
        validarMesAberto(dto.getMes(), dto.getAno());
        PrecoProduto preco = buscarOuCriarPrecoDoMes(dto.getProdutoId(), dto.getMes(), dto.getAno());
        preco.setIpi(dto.getIpi());
        preco.setDataRegistro(LocalDateTime.now());
        precoProdutoRepository.save(preco);
    }

    @Transactional
    public void salvarFrete(FreteRequestDTO dto) {
        validarMesAberto(dto.getMes(), dto.getAno());
        PrecoProduto preco = buscarOuCriarPrecoDoMes(dto.getProdutoId(), dto.getMes(), dto.getAno());
        preco.setFrete(dto.getFrete());
        preco.setDataRegistro(LocalDateTime.now());
        precoProdutoRepository.save(preco);
    }

    // Encontra o registro de preço JÁ EXISTENTE literalmente neste mês/ano, ou
    // cria um novo copiando os valores herdados do último mês (pra não perder
    // preço/ipi/frete que não estão sendo editados agora).
    private PrecoProduto buscarOuCriarPrecoDoMes(Long produtoId, Integer mes, Integer ano) {
        return precoProdutoRepository
                .findByProdutoIdAndMesReferenciaAndAnoReferencia(produtoId, mes, ano)
                .orElseGet(() -> {
                    PrecoProduto novo = new PrecoProduto();
                    novo.setProduto(buscarProduto(produtoId));
                    novo.setMesReferencia(mes);
                    novo.setAnoReferencia(ano);

                    Optional<PrecoProduto> anterior = precoProdutoRepository
                            .buscarHistoricoAte(produtoId, mes, ano, PageRequest.of(0, 1))
                            .stream()
                            .findFirst();

                    anterior.ifPresent(p -> {
                        novo.setPreco(p.getPreco());
                        novo.setIpi(p.getIpi());
                        novo.setFrete(p.getFrete());
                    });

                    // preco é NOT NULL na entidade — garante um valor mesmo que
                    // nunca tenha existido histórico antes
                    if (novo.getPreco() == null) {
                        novo.setPreco(java.math.BigDecimal.ZERO);
                    }

                    return novo;
                });
    }

    // ─── Fechamento do mês ──────────────────────────────────────────────────

    @Transactional
    public void fecharMes(FecharMesRequestDTO dto) {
        InventarioMes inventarioMes = inventarioMesRepository
                .findByMesReferenciaAndAnoReferencia(dto.getMes(), dto.getAno())
                .orElseGet(() -> {
                    InventarioMes novo = new InventarioMes();
                    novo.setMesReferencia(dto.getMes());
                    novo.setAnoReferencia(dto.getAno());
                    return novo;
                });

        if (inventarioMes.isFechado()) {
            throw new MesFechadoException("Este mês já está fechado.");
        }

        inventarioMes.setFechado(true);
        inventarioMes.setDataFechamento(LocalDateTime.now());
        inventarioMesRepository.save(inventarioMes);
    }

    // ─── Helpers ────────────────────────────────────────────────────────────

    private void validarMesAberto(Integer mes, Integer ano) {
        boolean fechado = inventarioMesRepository
                .findByMesReferenciaAndAnoReferencia(mes, ano)
                .map(InventarioMes::isFechado)
                .orElse(false);

        if (fechado) {
            throw new MesFechadoException("Não é possível editar um mês já fechado.");
        }
    }

    private Produto buscarProduto(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }
}