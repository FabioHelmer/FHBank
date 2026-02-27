package br.com.fhbank.dtos.transacoes;

import br.com.fhbank.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponseDTO(
        UUID id,
        TipoTransacao tipo,
        BigDecimal valor,
        BigDecimal saldoAnterior,
        BigDecimal saldoPosterior,
        String descricao,
        LocalDateTime data
) {}
