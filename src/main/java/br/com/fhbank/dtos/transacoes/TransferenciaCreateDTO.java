package br.com.fhbank.dtos.transacoes;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferenciaCreateDTO(
        UUID contaOrigem,
        UUID contaDestino,
        BigDecimal valor,
        String descricao
) {
}
