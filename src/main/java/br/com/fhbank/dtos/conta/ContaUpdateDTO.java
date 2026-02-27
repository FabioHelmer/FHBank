package br.com.fhbank.dtos.conta;

import br.com.fhbank.enums.TipoConta;
import jakarta.validation.constraints.NotNull;

public record ContaUpdateDTO(
        @NotNull TipoConta tipo
) {}
