package br.com.fhbank.services;

import br.com.fhbank.dtos.conta.ContaCreateDTO;
import br.com.fhbank.dtos.conta.ContaResponseDTO;
import br.com.fhbank.dtos.conta.ContaUpdateDTO;
import br.com.fhbank.enums.TipoConta;
import br.com.fhbank.exception.BusinessException;
import br.com.fhbank.exception.EntityNotFoundException;
import br.com.fhbank.models.Conta;
import br.com.fhbank.models.Usuario;
import br.com.fhbank.repositories.ContaRepository;
import br.com.fhbank.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;

    public ContaService(ContaRepository contaRepository,
                        UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }


    @Transactional
    public ContaResponseDTO criarConta(ContaCreateDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.idUsuario())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (contaRepository.existsByUsuarioId(usuario.getId())) {
            throw new BusinessException("Usuário já possui conta");
        }

        Conta conta = Conta.builder()
                .usuario(usuario)
                .tipo(dto.tipo())
                .saldo(BigDecimal.ZERO)
                .build();

        return toResponse(contaRepository.save(conta));
    }


    @Transactional(readOnly = true)
    public ContaResponseDTO buscarPorId(UUID id) {
        return contaRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrado"));
    }


    @Transactional
    public ContaResponseDTO atualizar(UUID contaId, ContaUpdateDTO dto) {

        Conta conta = contaRepository.findById(contaId)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        conta.setTipo(dto.tipo());

        return toResponse(conta);
    }


    @Transactional
    public void deletar(UUID idConta) {

        Conta conta = contaRepository.findById(idConta)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));

        if (conta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new BusinessException("Conta com saldo não pode ser removida");
        }

        contaRepository.delete(conta);
    }


    @Transactional(readOnly = true)
    public List<ContaResponseDTO> listarTodas() {
        return contaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContaResponseDTO> listarPorUsuario(UUID usuarioId) {
        return contaRepository.findAllByUsuarioId(usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ContaResponseDTO> listarPorTipo(TipoConta tipo) {
        return contaRepository.findAllByTipo(tipo)
                .stream()
                .map(this::toResponse)
                .toList();
    }


    private ContaResponseDTO toResponse(Conta conta) {
        return new ContaResponseDTO(
                conta.getId(),
                conta.getUsuario().getId(),
                conta.getTipo(),
                conta.getSaldo()
        );
    }
}