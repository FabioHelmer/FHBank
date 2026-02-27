package br.com.fhbank.services;

import br.com.fhbank.dtos.emprestimo.EmprestimoResponseDTO;
import br.com.fhbank.dtos.emprestimo.EmprestimoSolicitacaoDTO;
import br.com.fhbank.enums.StatusEmprestimo;
import br.com.fhbank.enums.TipoTransacao;
import br.com.fhbank.exception.BusinessException;
import br.com.fhbank.exception.EntityNotFoundException;
import br.com.fhbank.models.Conta;
import br.com.fhbank.models.Emprestimo;
import br.com.fhbank.models.Transacao;
import br.com.fhbank.models.Usuario;
import br.com.fhbank.repositories.ContaRepository;
import br.com.fhbank.repositories.EmprestimoRepository;
import br.com.fhbank.repositories.TransacaoRepository;
import br.com.fhbank.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final UsuarioRepository usuarioRepository;

    private static BigDecimal taxaJuros = new BigDecimal(3.00);

    public EmprestimoService(EmprestimoRepository emprestimoRepository,
                             ContaRepository contaRepository,
                             TransacaoRepository transacaoRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /* ======================
       SOLICITAR (USUÁRIO)
       ====================== */
    @Transactional
    public EmprestimoResponseDTO solicitar(UUID usuarioId, EmprestimoSolicitacaoDTO dto) {

        validarSolicitacao(dto);


        Conta conta = contaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));

        BigDecimal juros = dto.valorSolicitado()
                .multiply(taxaJuros)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal valorTotal = dto.valorSolicitado().add(juros);

        Emprestimo e = new Emprestimo();
        e.setConta(conta);
        e.setValorSolicitado(dto.valorSolicitado());
        e.setTaxaJuros(taxaJuros);
        e.setValorTotal(valorTotal);
        e.setParcelas(dto.parcelas());
        // status e data via @PrePersist

        return toDTO(emprestimoRepository.save(e));
    }

    /* ======================
       LISTAR PENDENTES (ADMIN)
       ====================== */
    @Transactional(readOnly = true)
    public List<EmprestimoResponseDTO> listarPendentes() {
        return emprestimoRepository.findAllByStatus(StatusEmprestimo.PENDENTE)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* ======================
       APROVAR (ADMIN)
       ====================== */
    @Transactional
    public void aprovar(UUID emprestimoId, UUID idAdmin) {

        Emprestimo e = buscar(emprestimoId);

        if (e.getStatus() != StatusEmprestimo.PENDENTE) {
            throw new BusinessException("Empréstimo não está pendente");
        }

        Conta conta = e.getConta();

        BigDecimal saldoAntes = conta.getSaldo();
        BigDecimal saldoDepois = saldoAntes.add(e.getValorSolicitado());

        conta.setSaldo(saldoDepois);

        // crédito do empréstimo
        Transacao t = new Transacao();
        t.setConta(conta);
        t.setTipo(TipoTransacao.DEPOSITO);
        t.setValor(e.getValorSolicitado());
        t.setSaldoAnterior(saldoAntes);
        t.setSaldoPosterior(saldoDepois);
        t.setData(LocalDateTime.now());
        transacaoRepository.save(t);

        Usuario admin = usuarioRepository.findById(idAdmin)
                .orElseThrow(() -> new EntityNotFoundException("Admin não encontrado"));

        e.setStatus(StatusEmprestimo.APROVADO);
        e.setAprovadoPor(admin);
        e.setDataResposta(LocalDateTime.now());
    }

    /* ======================
       RECUSAR (ADMIN)
       ====================== */
    @Transactional
    public void recusar(UUID emprestimoId, UUID idAdmin, String motivo) {

        Emprestimo e = buscar(emprestimoId);

        if (e.getStatus() != StatusEmprestimo.PENDENTE) {
            throw new BusinessException("Empréstimo não está pendente");
        }

        Usuario admin = usuarioRepository.findById(idAdmin)
                .orElseThrow(() -> new EntityNotFoundException("Admin não encontrado"));

        e.setStatus(StatusEmprestimo.RECUSADO);
        e.setMotivoRecusa(motivo);
        e.setAprovadoPor(admin);
        e.setDataResposta(LocalDateTime.now());
    }

    /* ======================
       DÉBITO MENSAL (SIMULADO)
       ====================== */
    @Transactional
    public void debitarParcela(UUID emprestimoId) {

        Emprestimo e = buscar(emprestimoId);

        if (e.getStatus() != StatusEmprestimo.APROVADO) {
            return;
        }

        BigDecimal valorParcela = e.getValorTotal()
                .divide(BigDecimal.valueOf(e.getParcelas()), 2, RoundingMode.HALF_UP);

        Conta conta = e.getConta();

        if (conta.getSaldo().compareTo(valorParcela) < 0) {
            throw new BusinessException("Saldo insuficiente para parcela");
        }

        BigDecimal saldoAntes = conta.getSaldo();
        BigDecimal saldoDepois = saldoAntes.subtract(valorParcela);

        conta.setSaldo(saldoDepois);

        Transacao t = new Transacao();
        t.setConta(conta);
        t.setTipo(TipoTransacao.SAQUE);
        t.setValor(valorParcela);
        t.setSaldoAnterior(saldoAntes);
        t.setSaldoPosterior(saldoDepois);
        t.setData(LocalDateTime.now());

        transacaoRepository.save(t);
    }

    /* ====================== */
    private Emprestimo buscar(UUID id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Empréstimo não encontrado"));
    }

    private void validarSolicitacao(EmprestimoSolicitacaoDTO dto) {
        if (dto.valorSolicitado().compareTo(BigDecimal.ZERO) <= 0)
            throw new BusinessException("Valor inválido");

        if (taxaJuros.compareTo(BigDecimal.ZERO) < 0)
            throw new BusinessException("Taxa de juros inválida");

        if (dto.parcelas() <= 0)
            throw new BusinessException("Parcelas inválidas");
    }

    private EmprestimoResponseDTO toDTO(Emprestimo e) {
        return new EmprestimoResponseDTO(
                e.getId(),
                e.getValorSolicitado(),
                e.getValorTotal(),
                e.getTaxaJuros(),
                e.getParcelas(),
                e.getStatus()
        );
    }
}
