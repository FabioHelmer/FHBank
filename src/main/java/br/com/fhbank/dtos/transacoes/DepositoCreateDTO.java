package br.com.fhbank.dtos.transacoes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record DepositoCreateDTO(

        @NotNull
        UUID idConta,

        String descricao,

        @Positive
        BigDecimal valor
) {
}
