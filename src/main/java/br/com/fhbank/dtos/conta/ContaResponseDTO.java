package br.com.fhbank.dtos.conta;

import br.com.fhbank.enums.TipoConta;

import java.math.BigDecimal;
import java.util.UUID;

public record ContaResponseDTO(
        UUID id,
        UUID idUsuario,
        TipoConta tipo,
        BigDecimal saldo
) {}
