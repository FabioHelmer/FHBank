package br.com.fhbank.services;

import br.com.fhbank.dtos.transacoes.DepositoCreateDTO;
import br.com.fhbank.dtos.transacoes.TransferenciaCreateDTO;
import org.springframework.transaction.annotation.Transactional;
import br.com.fhbank.dtos.transacoes.TransacaoResponseDTO;
import br.com.fhbank.enums.TipoTransacao;
import br.com.fhbank.exception.BusinessException;
import br.com.fhbank.exception.EntityNotFoundException;
import br.com.fhbank.models.Conta;
import br.com.fhbank.models.Transacao;
import br.com.fhbank.repositories.ContaRepository;
import br.com.fhbank.repositories.TransacaoRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransacaoService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public TransacaoService(ContaRepository contaRepository,
                            TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public void depositar(DepositoCreateDTO dto) {

        validarValor(dto.valor());

        Conta conta = buscarConta(dto.idConta());

        BigDecimal saldoAnterior = conta.getSaldo();
        BigDecimal saldoPosterior = saldoAnterior.add(dto.valor());

        conta.setSaldo(saldoPosterior);
        registrarTransacao(conta, TipoTransacao.DEPOSITO,
                dto.valor(), saldoAnterior, saldoPosterior,dto.descricao());
    }

    @Transactional
    public void sacar(UUID contaId, BigDecimal valor) {

        validarValor(valor);

        Conta conta = buscarConta(contaId);

        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new BusinessException("Saldo insuficiente");
        }

        BigDecimal saldoAnterior = conta.getSaldo();
        BigDecimal saldoPosterior = saldoAnterior.subtract(valor);

        conta.setSaldo(saldoPosterior);

        registrarTransacao(conta, TipoTransacao.SAQUE,
                valor, saldoAnterior, saldoPosterior, null);
    }

    @Transactional
    public void transferir(TransferenciaCreateDTO dto) {

        if (dto.contaOrigem().equals(dto.contaDestino())) {
            throw new BusinessException("Conta de origem e destino iguais");
        }

        validarValor(dto.valor());

        Conta origem = buscarConta(dto.contaOrigem());
        Conta destino = buscarConta(dto.contaDestino());

        if (origem.getSaldo().compareTo(dto.valor()) < 0) {
            throw new BusinessException("Saldo insuficiente");
        }

        // débito
        BigDecimal saldoOrigemAntes = origem.getSaldo();
        origem.setSaldo(saldoOrigemAntes.subtract(dto.valor()));

        registrarTransacao(origem, TipoTransacao.TRANSFERENCIA_SAIDA,
                dto.valor(), saldoOrigemAntes, origem.getSaldo(),dto.descricao());

        // crédito
        BigDecimal saldoDestinoAntes = destino.getSaldo();
        destino.setSaldo(saldoDestinoAntes.add(dto.valor()));

        registrarTransacao(destino, TipoTransacao.TRANSFERENCIA_ENTRADA,
                dto.valor(), saldoDestinoAntes, destino.getSaldo(),dto.descricao());
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> extrato(UUID contaId) {

        return transacaoRepository
                .findAllByContaIdOrderByDataDesc(contaId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private TransacaoResponseDTO toDTO(Transacao t) {
        return new TransacaoResponseDTO(
                t.getId(),
                t.getTipo(),
                t.getValor(),
                t.getSaldoAnterior(),
                t.getSaldoPosterior(),
                t.getDescricao(),
                t.getData()
        );
    }

    /* ======================
       AUXILIARES
       ====================== */
    private Conta buscarConta(UUID id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Conta não encontrada"));
    }

    private void validarValor(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor deve ser maior que zero");
        }
    }

    private void registrarTransacao(
            Conta conta,
            TipoTransacao tipo,
            BigDecimal valor,
            BigDecimal saldoAnterior,
            BigDecimal saldoPosterior,
            String descricao
    ) {
        Transacao t = Transacao.builder()
                .conta(conta)
                .tipo(tipo)
                .valor(valor)
                .saldoAnterior(saldoAnterior)
                .saldoPosterior(saldoPosterior)
                .descricao(descricao)
                .data(LocalDateTime.now())
                .build();

        transacaoRepository.save(t);
    }
}
