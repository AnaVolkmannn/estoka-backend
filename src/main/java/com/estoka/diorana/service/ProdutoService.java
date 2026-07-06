package com.estoka.diorana.service;

import java.util.List;
import com.estoka.diorana.dto.FornecedorResponseDTO;
import com.estoka.diorana.dto.ProdutoRequestDTO;
import com.estoka.diorana.dto.ProdutoResponseDTO;
import com.estoka.diorana.repository.FornecedorRepository;
import com.estoka.diorana.repository.ProdutoRepository;
import com.estoka.diorana.model.Fornecedor;
import com.estoka.diorana.model.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return toResponseDTO(produto);
    }

    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setUnidadeMedida(dto.getUnidadeMedida());
        produto.setFornecedor(fornecedor);
        produto.setTemIpi(dto.isTemIpi());
        produto.setTemFrete(dto.isTemFrete());

        return toResponseDTO(produtoRepository.save(produto));
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));

        produto.setNome(dto.getNome());
        produto.setUnidadeMedida(dto.getUnidadeMedida());
        produto.setFornecedor(fornecedor);
        produto.setTemIpi(dto.isTemIpi());
        produto.setTemFrete(dto.isTemFrete());
        // "ativo" não é tocado aqui de propósito — isso é responsabilidade
        // exclusiva de deletar()/reativar()

        return toResponseDTO(produtoRepository.save(produto));
    }

    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    public List<ProdutoResponseDTO> listarInativos() {
        return produtoRepository.findByAtivoFalse()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public void reativar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setAtivo(true);
        produtoRepository.save(produto);
    }

    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        ProdutoResponseDTO dto = new ProdutoResponseDTO();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setUnidadeMedida(produto.getUnidadeMedida());
        dto.setTemIpi(produto.isTemIpi());
        dto.setTemFrete(produto.isTemFrete());

        FornecedorResponseDTO fornecedorDTO = new FornecedorResponseDTO();
        fornecedorDTO.setId(produto.getFornecedor().getId());
        fornecedorDTO.setNome(produto.getFornecedor().getNome());
        fornecedorDTO.setCnpj(produto.getFornecedor().getCnpj());
        dto.setFornecedor(fornecedorDTO);

        return dto;
    }
}