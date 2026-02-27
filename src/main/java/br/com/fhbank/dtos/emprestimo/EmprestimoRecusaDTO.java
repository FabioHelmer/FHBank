package br.com.fhbank.dtos.emprestimo;

import jakarta.validation.constraints.NotBlank;

public record EmprestimoRecusaDTO(
        @NotBlank String motivo
) {}
