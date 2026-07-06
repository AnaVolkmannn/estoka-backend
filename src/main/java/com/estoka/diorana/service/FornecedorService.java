package com.estoka.diorana.service;
import java.util.List;
import com.estoka.diorana.dto.FornecedorRequestDTO;
import com.estoka.diorana.dto.FornecedorResponseDTO;
import com.estoka.diorana.repository.FornecedorRepository;
import com.estoka.diorana.model.Fornecedor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public List<FornecedorResponseDTO> listarTodos() {
        return fornecedorRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public FornecedorResponseDTO buscarPorId(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        return toResponseDTO(fornecedor);
    }

    public FornecedorResponseDTO salvar(FornecedorRequestDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        return toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fornecedor não encontrado"));
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        return toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    public void deletar(Long id) {
        fornecedorRepository.deleteById(id);
    }

    private FornecedorResponseDTO toResponseDTO(Fornecedor fornecedor) {
        FornecedorResponseDTO dto = new FornecedorResponseDTO();
        dto.setId(fornecedor.getId());
        dto.setNome(fornecedor.getNome());
        dto.setCnpj(fornecedor.getCnpj());
        return dto;
    }
}