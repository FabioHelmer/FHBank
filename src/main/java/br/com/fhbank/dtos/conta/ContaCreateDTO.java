package br.com.fhbank.dtos.conta;

import br.com.fhbank.enums.TipoConta;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ContaCreateDTO(
        @NotNull UUID idUsuario,
        @NotNull TipoConta tipo
) {}
